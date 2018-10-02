package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;

import java.util.Collections;

import static com.bags.simulacrum.Damage.DamageSourceType.PROJECTILE;
import static com.bags.simulacrum.Damage.DamageType.TOXIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class PoisonTest extends StatusDotTest {

    @Override
    protected void setupDefaultSubject() {
        fakeDuration = 3.0;
        fakeNumberOfTicks = 4;
        fakeDeltaTime = 0.01;
        fakeDamagePerTick = 11;
        fakeDamageType = TOXIN;
        Poison poisonProc = new Poison();
        poisonProc.setDuration(fakeDuration);
        poisonProc.setNumberOfDamageTicks(fakeNumberOfTicks);
        poisonProc.setDamagePerTick(fakeDamagePerTick);
        poisonProc.setDamageType(fakeDamageType);
        poisonProc.setupTimers();
        when(statusFactory.getStatusProc(any(), eq(fakeDamageType))).thenReturn(poisonProc);
        subject = (Poison) statusFactory.getStatusProc(new DamageSource(PROJECTILE, Collections.singletonList(new Damage(TOXIN, 50.0))), fakeDamageType);
    }
}
