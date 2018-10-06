package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
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
        assertEquals(1, subject.apply(new Target()).getDamages().size());
        assertEquals(fakeDamagePerTick, subject.apply(new Target()).getDamages().get(0).getDamageValue(), 0.0);
        assertEquals(specialCaseType, subject.apply(new Target()).getDamages().get(0).getDamageType());
        assertNull(subject.apply(new Target()).getModifiedInnateDamages());
        assertNull(subject.apply(new Target()).getAddedElementalDamages());
    }

    @Override
    protected void setupDefaultSubject() {
        fakeDuration = 7.0;
        fakeNumberOfTicks = 8;
        fakeDeltaTime = 0.01;
        fakeDamagePerTick = 44;
        fakeDamageType = SLASH;
        this.specialCaseType = TRUE;
        Bleed bleedProc = new Bleed();
        bleedProc.setDuration(fakeDuration);
        bleedProc.setNumberOfDamageTicks(fakeNumberOfTicks);
        bleedProc.setDamagePerTick(fakeDamagePerTick);
        bleedProc.setDamageType(fakeDamageType);
        bleedProc.setupTimers();
        when(statusFactory.getStatusProc(any(), eq(fakeDamageType))).thenReturn(bleedProc);
        subject = (Bleed) statusFactory.getStatusProc(new DamageSource(PROJECTILE, Collections.singletonList(new Damage(SLASH, 50.0))), fakeDamageType);
    }
}