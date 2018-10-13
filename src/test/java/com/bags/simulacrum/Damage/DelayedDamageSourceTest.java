package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Simulation.HitProperties;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static com.bags.simulacrum.Damage.DamageType.BLAST;
import static com.bags.simulacrum.Damage.DamageType.HEAT;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class DelayedDamageSourceTest {

    private DelayedDamageSource subject;

    @Before
    public void setup() {
        subject = new DelayedDamageSource(new Target(), new DamageSource(DamageSourceType.DELAYED, Collections.singletonList(new Damage(HEAT, 25))), new HitProperties(1, 2.0, 3.0, 4.0), 25.0);
    }

    @Test
    public void test() {
        assertEquals(new Target(), subject.getTarget());
        assertEquals(new DamageSource(DamageSourceType.DELAYED, Collections.singletonList(new Damage(HEAT, 25))), subject.getDamageSource());
        assertEquals(new HitProperties(1, 2.0, 3.0, 4.0), subject.getHitProperties());
        assertEquals(25.0, subject.getDelay(), 0.0);
        assertEquals(0.0, subject.getProgress(), 0.0);

        Target fakeTarget = new Target();
        fakeTarget.setTargetId("banana");
        subject.setTarget(fakeTarget);
        subject.setDamageSource(new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(BLAST, 25))));
        subject.setHitProperties(new HitProperties(4, 3.0, 2.0, 1.0));
        subject.setDelay(1234.0);
        subject.progressTime(0.01);

        assertEquals(fakeTarget, subject.getTarget());
        assertEquals(new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(BLAST, 25))), subject.getDamageSource());
        assertEquals(new HitProperties(4, 3.0, 2.0, 1.0), subject.getHitProperties());
        assertEquals(1234.0, subject.getDelay(), 0.0);
        assertEquals(0.01, subject.getProgress(), 0.0);

        DelayedDamageSource copy = subject.copy();
        assertEquals(copy, subject);
        assertNotSame(copy, subject);

        subject.setDelay(0.0);

        assertTrue(subject.delayOver());
    }


}