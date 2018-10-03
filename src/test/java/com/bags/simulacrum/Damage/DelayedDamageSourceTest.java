package com.bags.simulacrum.Damage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DelayedDamageSourceTest {

    private DelayedDamageSource subject;

    @Before
    public void setup() {
        subject = new DelayedDamageSource(null, null, 0.0);
    }

    @Test
    public void itCanHaveTestCoverage_1() {
        assertNull(subject.getDamageSource());
        assertEquals(0.0, subject.getDelay(), 0.0);
    }

    @Test
    public void itCanHaveTestCoverage_2() {
        subject.setDamageSource(new DamageSource());
        subject.setDelay(1.0);

        assertNotNull(subject.getDamageSource());
        assertEquals(1.0, subject.getDelay(), 0.0);
    }

}