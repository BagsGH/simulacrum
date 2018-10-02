package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;

import java.util.Collections;

import static com.bags.simulacrum.Damage.DamageSourceType.PROJECTILE;
import static com.bags.simulacrum.Damage.DamageType.HEAT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class IgniteTest extends StatusDotTest {

    @Override
    protected void setupDefaultSubject() {
        fakeDuration = 5.0;
        fakeNumberOfTicks = 6;
        fakeDeltaTime = 0.01;
        fakeDamagePerTick = 22;
        fakeDamageType = HEAT;
        Ignite igniteProc = new Ignite();
        igniteProc.setDuration(fakeDuration);
        igniteProc.setNumberOfDamageTicks(fakeNumberOfTicks);
        igniteProc.setDamagePerTick(fakeDamagePerTick);
        igniteProc.setDamageType(fakeDamageType);
        igniteProc.setupTimers();
        when(statusFactory.getStatusProc(any(), eq(fakeDamageType))).thenReturn(igniteProc);
        subject = (Ignite) statusFactory.getStatusProc(new DamageSource(PROJECTILE, Collections.singletonList(new Damage(HEAT, 50.0))), fakeDamageType);
    }
}