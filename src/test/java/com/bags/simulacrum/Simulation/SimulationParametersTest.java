package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Weapon.Weapon;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SimulationParametersTest {

    private SimulationParameters subject;

    @Before
    public void setup() {
        subject = new SimulationParameters();
    }

    @Test
    public void test() {
        assertNull(subject.getModdedWeapon());
        assertNull(subject.getSimulationTargets());
        assertEquals(0.0, subject.getDuration(), 0.0);
        assertEquals(0.0, subject.getHeadshotChance(), 0.0);
        assertEquals(0, subject.getIterations());
        assertFalse(subject.isLimitAmmo());
        assertFalse(subject.isReplaceDeadTargets());

        subject.setModdedWeapon(new Weapon());
        subject.setSimulationTargets(new SimulationTargets(null, null));
        subject.setDuration(1.0);
        subject.setHeadshotChance(2.0);
        subject.setIterations(3);
        subject.setLimitAmmo(true);
        subject.setReplaceDeadTargets(true);

        assertEquals(1.0, subject.getDuration(), 0.0);
        assertEquals(2.0, subject.getHeadshotChance(), 0.0);
        assertEquals(3, subject.getIterations());
        assertTrue(subject.isLimitAmmo());
        assertTrue(subject.isReplaceDeadTargets());
        assertEquals(new Weapon(), subject.getModdedWeapon());
        assertEquals(new SimulationTargets(null, null), subject.getSimulationTargets());

    }

}