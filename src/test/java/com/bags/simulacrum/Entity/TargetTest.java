package com.bags.simulacrum.Entity;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class TargetTest {

    Target subject;

    @Before
    public void setUp() throws Exception {
        subject = new Target();
    }

    @Test
    public void itCanHaveSetterTestCoverage() {
        subject.setAbilities(Collections.singletonList("Charge"));
        subject.setWeapons(Collections.singletonList("Rifle"));
        subject.setHealth(Collections.singletonList(new Health(HealthClass.MACHINERY, 200.0)));
        subject.setName("Moa");
        subject.setHeadshotMultiplier(2.0);
        subject.setBaseLevel(1);
        subject.setLevel(1);

        assertEquals(1, subject.getAbilities().size());
        assertEquals(1, subject.getWeapons().size());
        assertEquals(1, subject.getHealth().size());
        assertEquals("Moa", subject.getName());
        assertEquals(2.0, subject.getHeadshotMultiplier(), 0.0);
        assertEquals(1, subject.getBaseLevel());
        assertEquals(1, subject.getLevel());
    }

    @Test
    public void itCanAddHealthToTarget() {
        subject.addHealth(new Health(HealthClass.MACHINERY, 200.0));

        assertEquals(HealthClass.MACHINERY, subject.getHealth().get(0).getHealthClass());
    }
}