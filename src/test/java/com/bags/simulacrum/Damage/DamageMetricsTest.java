package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Entity.Target;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertNull;

public class DamageMetricsTest {

    private DamageMetrics subject;

    @Before
    public void setup() {
        subject = new DamageMetrics(null, null, null);
    }

    @Test
    public void itCanHaveTestCoverage_1() {
        assertNull(subject.getDamageToHealth());
        assertNull(subject.getDamageToShields());
        assertNull(subject.getTarget());
    }

    @Test
    public void itCanHaveTestCoverage_2() {
        subject.setTarget(new Target());
        subject.setDamageToHealth(new HashMap<>());
        subject.setDamageToShields(new HashMap<>());

        assertNotNull(subject.getDamageToHealth());
        assertNotNull(subject.getDamageToShields());
        assertNotNull(subject.getTarget());
    }

}