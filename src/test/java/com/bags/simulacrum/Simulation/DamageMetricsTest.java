package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Damage.DamageType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DamageMetricsTest {

    private DamageMetrics subject;

    @Before
    public void setup() {
        subject = new DamageMetrics();
    }

    @Test
    public void itInitializesMemberVariables() {
        assertNotNull(subject.getDamageToShields());
        assertNotNull(subject.getDamageToHealth());
        assertNotNull(subject.getStatusDamageToHealth());
        assertNotNull(subject.getStatusDamageToShields());
    }

    @Test
    public void itCanAddDamageToHealth() {
        subject.addDamageToHealth(DamageType.BLAST, 25.0);

        assertEquals(25.0, subject.getDamageToHealth().get(DamageType.BLAST), 0.0);
    }

    @Test
    public void itCanAddDamageToShields() {
        subject.addDamageToShields(DamageType.BLAST, 25.0);

        assertEquals(25.0, subject.getDamageToShields().get(DamageType.BLAST), 0.0);
    }

    @Test
    public void itCanAddStatusDamageToHealth() {
        subject.addStatusDamageToHealth(DamageType.BLAST, 25.0);

        assertEquals(25.0, subject.getStatusDamageToHealth().get(DamageType.BLAST), 0.0);
    }

    @Test
    public void itCanAddStatusDamageToShields() {
        subject.addStatusDamageToShields(DamageType.BLAST, 25.0);

        assertEquals(25.0, subject.getStatusDamageToShields().get(DamageType.BLAST), 0.0);
    }

    @Test
    public void itCanInitializeEmptyMaps() {
        assertNotNull(DamageMetrics.initialDamageMap());
    }

}