package com.bags.simulacrum.Status;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Entity.Target;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static com.bags.simulacrum.Armor.HealthClass.*;
import static com.bags.simulacrum.Damage.DamageSourceType.PROJECTILE;
import static com.bags.simulacrum.Damage.DamageType.CORROSIVE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class CorrosionTest {

    private Corrosion subject;

    @Mock
    private StatusFactory statusFactory;

    private double fakeDuration;

    private int fakeNumberOfTicks;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fakeDuration = 999999.0;
        fakeNumberOfTicks = 0;
        setupDefaultSubject();
    }

    @Test
    public void itReducesTargetArmorWhenApplied() {
        Target fakeTarget = new Target();
        fakeTarget.setHealths(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200), new Health(ALLOY, 200)));

        subject.apply(fakeTarget);

        assertEquals(150.0, fakeTarget.getHealths().get(2).getHealthValue(), 0.0);
    }

    @Test
    public void itReducesArmorBasedOnCurrentArmor() {
        Target fakeTarget = new Target();
        fakeTarget.setHealths(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200), new Health(ALLOY, 200)));

        subject.apply(fakeTarget);
        subject.apply(fakeTarget);

        assertEquals(112.5, fakeTarget.getArmor().getHealthValue(), 0.0);
    }

    @Test
    public void itDoesNotAffectTargetsWithoutArmor() {
        Target fakeTarget = new Target();
        Target expectedTarget = new Target();
        fakeTarget.setHealths(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200)));
        expectedTarget.setHealths(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200)));

        subject.apply(fakeTarget);

        assertEquals(expectedTarget, fakeTarget);
    }

    @Test
    public void itDoesNotReturnAnyDamages() {
        Target fakeTarget = new Target();
        fakeTarget.setHealths(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200)));
        DamageSource actualDamageSource = subject.apply(fakeTarget);

        assertEquals(0, actualDamageSource.getDamages().size());
    }

    private void setupDefaultSubject() {
        Corrosion corr = new Corrosion();
        corr.setDuration(fakeDuration);
        corr.setNumberOfDamageTicks(fakeNumberOfTicks);
        corr.setDamagePerTick(0);
        corr.setDamageType(CORROSIVE);
        corr.setupTimers();
        when(statusFactory.getStatusProc(any(), eq(CORROSIVE))).thenReturn(corr);
        subject = (Corrosion) statusFactory.getStatusProc(new DamageSource(PROJECTILE, Collections.singletonList(new Damage(CORROSIVE, 50.0))), CORROSIVE);
    }
}