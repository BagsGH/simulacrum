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
        /*
         * Tesla currently only has one tick, the initial tick, so it should never be ready for the next tick.
         */
        for (int i = 0; i < (int) ((fakeDuration / (fakeNumberOfTicks - (subject.applyInstantly() ? 1 : 0))) / fakeDeltaTime); i++) {
            subject.progressTime(fakeDeltaTime);
        }
        assertFalse(subject.checkProgress());
    }

    @Override
    protected void setupDefaultSubject() {
        fakeDuration = 0.0;
        fakeNumberOfTicks = 1;
        fakeDeltaTime = 0.01;
        fakeDamagePerTick = 55;
        fakeDamageType = ELECTRICITY;
        TeslaChain teslaProc = new TeslaChain();
        teslaProc.setDuration(fakeDuration);
        teslaProc.setNumberOfDamageTicks(fakeNumberOfTicks);
        teslaProc.setDamagePerTick(fakeDamagePerTick);
        teslaProc.setDamageType(fakeDamageType);
        teslaProc.setupTimers();
        when(statusFactory.getStatusProc(any(), eq(fakeDamageType))).thenReturn(teslaProc);
        subject = (TeslaChain) statusFactory.getStatusProc(new DamageSource(PROJECTILE, Collections.singletonList(new Damage(fakeDamageType, 50.0))), fakeDamageType);
    }
}