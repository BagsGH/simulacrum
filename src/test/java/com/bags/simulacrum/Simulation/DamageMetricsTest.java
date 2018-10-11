package com.bags.simulacrum.Simulation;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertNotNull;

public class DamageMetricsTest {

    private DamageMetrics subject;

    @Before
    public void setup() {
        subject = new DamageMetrics();
    }

    @Test
    public void itCanHaveTestCoverage_1() {
        assertNotNull(subject.getDamageToHealth());
        assertNotNull(subject.getDamageToShields());
    }

    @Test
    public void itCanHaveTestCoverage_2() {
        subject.setDamageToHealth(new HashMap<>());
        subject.setDamageToShields(new HashMap<>());

        assertNotNull(subject.getDamageToHealth());
        assertNotNull(subject.getDamageToShields());
    }

}