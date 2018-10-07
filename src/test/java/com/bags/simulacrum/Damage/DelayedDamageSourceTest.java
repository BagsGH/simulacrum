package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Simulation.HitProperties;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static com.bags.simulacrum.Damage.DamageType.HEAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class DelayedDamageSourceTest {

    private DelayedDamageSource subject;

    @Before
    public void setup() {
        subject = new DelayedDamageSource(null, null, 0.0);
    }

    @Test
    public void itCanCopy() {

        subject.setDamageSource(new DamageSource(DamageSourceType.HIT, Collections.singletonList(new Damage(HEAT, 25))));
        subject.setHitProperties(new HitProperties(1, 1, 1, 1));
        subject.setDelay(subject.getDelay() + 1234.0);
        subject.setProgress(subject.getProgress() + 123.0);

        DelayedDamageSource copy = subject.copy();
        assertEquals(subject, copy);
        assertNotSame(subject, copy);
    }

}