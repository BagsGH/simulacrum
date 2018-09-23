package com.bags.simulacrum.Damage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class FiredWeaponMetricsTest {

    private FiredWeaponMetrics subject;

    @Before
    public void setup() {
        subject = new FiredWeaponMetrics(null, null, null, null);
    }

    @Test
    public void itCanHaveTestCoverage() {
        assertNull(subject.getHitPropertiesList());
        assertNull(subject.getDamageMetrics());
        assertNull(subject.getStatusProcs());
        assertNull(subject.getDelayedDamageSources());
    }

    @Test
    public void itCanHaveTestCoverage_2() {
        subject.setDamageMetrics(null);
        subject.setDelayedDamageSources(null);
        subject.setHitPropertiesList(null);
        subject.setStatusProcs(null);
        assertNull(subject.getHitPropertiesList());
        assertNull(subject.getDamageMetrics());
        assertNull(subject.getDelayedDamageSources());
        assertNull(subject.getStatusProcs());
    }


}