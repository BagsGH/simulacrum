package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import org.junit.Test;

import java.util.Collections;

import static com.bags.simulacrum.Damage.DamageSourceType.PROJECTILE;
import static com.bags.simulacrum.Damage.DamageType.ELECTRICITY;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class TeslaChainTest extends StatusDotTest {

    @Test
    @Override
    public void itIsReadyToApplyAfterExpectedTime() {
        for (int i = 0; i < (int) ((fakeDuration / (fakeNumberOfTicks - (subject.applyInstantly() ? 1 : 0))) / fakeDeltaTime); i++) {
            subject.progressTime(fakeDeltaTime);
        }
        assertFalse(subject.checkProgress());
    }

    @Override
    void setupDefaultSubject() {
        fakeDuration = 0.0;
        fakeNumberOfTicks = 1;
        fakeDeltaTime = 0.01;
        fakeDamagePerTick = 55;
        fakeDamageType = ELECTRICITY;
        TeslaChain ignite = new TeslaChain();
        ignite.setDuration(fakeDuration);
        ignite.setNumberOfDamageTicks(fakeNumberOfTicks);
        ignite.setDamagePerTick(fakeDamagePerTick);
        ignite.setDamageType(fakeDamageType);
        ignite.setupTimers();
        when(statusFactory.getStatusProc(any(), eq(fakeDamageType))).thenReturn(ignite);
        subject = (TeslaChain) statusFactory.getStatusProc(new DamageSource(PROJECTILE, Collections.singletonList(new Damage(fakeDamageType, 50.0))), fakeDamageType);
    }
}