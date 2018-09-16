package com.bags.simulacrum.Armor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HealthTest {

    private Health subject;

    @Test
    public void itCanHaveConstructorTestCoverage() {
        subject = new Health(HealthClass.MACHINERY, 200.0);

        assertEquals(HealthClass.MACHINERY, subject.getHealthClass());
        assertEquals(200.0, subject.getHealthValue(), 0.0);
        assertEquals(200.0, subject.getHealthValueMax(), 0.0);
    }

    @Test
    public void itCanHaveSettersTestCoverage() {
        subject = new Health(HealthClass.MACHINERY, 200.0);

        subject.setHealthClass(HealthClass.INFESTED_FLESH);
        subject.setHealthValue(250.0);
        subject.setHealthValueMax(375.0);

        assertEquals(HealthClass.INFESTED_FLESH, subject.getHealthClass());
        assertEquals(250.0, subject.getHealthValue(), 0.0);
        assertEquals(375.0, subject.getHealthValueMax(), 0.0);
    }

}