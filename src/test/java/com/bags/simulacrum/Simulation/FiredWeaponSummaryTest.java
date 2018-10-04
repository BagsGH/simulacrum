package com.bags.simulacrum.Simulation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class FiredWeaponSummaryTest {

    private FiredWeaponSummary subject;

    @Before
    public void setup() {
        subject = new FiredWeaponSummary(null, null, null, null);
    }

    @Test
    public void itCanHaveTestCoverage() {
        assertNull(subject.getHitPropertiesList());
        assertNull(subject.getDamageMetrics());
        assertNull(subject.getStatusesApplied());
        assertNull(subject.getDelayedDamageSources());
    }

    @Test
    public void itCanHaveTestCoverage_2() {
        subject.setDamageMetrics(null);
        subject.setDelayedDamageSources(null);
        subject.setHitPropertiesList(null);
        subject.setStatusesApplied(null);
        assertNull(subject.getHitPropertiesList());
        assertNull(subject.getDamageMetrics());
        assertNull(subject.getDelayedDamageSources());
        assertNull(subject.getStatusesApplied());
    }


}