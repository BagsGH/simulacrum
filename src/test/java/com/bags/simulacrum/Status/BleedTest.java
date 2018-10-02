package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import org.junit.Test;

import java.util.Collections;

import static com.bags.simulacrum.Damage.DamageSourceType.PROJECTILE;
import static com.bags.simulacrum.Damage.DamageType.SLASH;
import static com.bags.simulacrum.Damage.DamageType.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class BleedTest extends StatusDotTest {

    private DamageType specialCaseType;

    @Test
    @Override
    public void itStoresItsDamagePerTickAndCanReturnTheDamageSource() {
        assertEquals(1, subject.getDamageSource().getDamages().size());
        assertEquals(fakeDamagePerTick, subject.getDamageSource().getDamages().get(0).getDamageValue(), 0.0);
        assertEquals(specialCaseType, subject.getDamageSource().getDamages().get(0).getType());
        assertNull(subject.getDamageSource().getModifiedInnateDamages());
        assertNull(subject.getDamageSource().getAddedElementalDamages());
    }

    @Override
    void setupDefaultSubject() {
        fakeDuration = 7.0;
        fakeNumberOfTicks = 8;
        fakeDeltaTime = 0.01;
        fakeDamagePerTick = 44;
        fakeDamageType = SLASH;
        this.specialCaseType = TRUE;
        Bleed ignite = new Bleed();
        ignite.setDuration(fakeDuration);
        ignite.setNumberOfDamageTicks(fakeNumberOfTicks);
        ignite.setDamagePerTick(fakeDamagePerTick);
        ignite.setDamageType(fakeDamageType);
        ignite.setupTimers();
        when(statusFactory.getStatusProc(any(), eq(fakeDamageType))).thenReturn(ignite);
        subject = (Bleed) statusFactory.getStatusProc(new DamageSource(PROJECTILE, Collections.singletonList(new Damage(SLASH, 50.0))), fakeDamageType);
    }
}