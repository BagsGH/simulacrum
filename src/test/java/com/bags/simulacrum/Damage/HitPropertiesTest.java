package com.bags.simulacrum.Damage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HitPropertiesTest {

    private HitProperties subject;

    @Test
    public void itCanHaveSetterTestCoverage() {
        subject = new HitProperties(1, 2.0, 3.0, 4.0);
        subject.setCritLevel(subject.getCritLevel() + 1);
        subject.setCriticalDamageModifier(subject.getCriticalDamageModifier() + 1.0);
        subject.setHeadshotMultiplier(subject.getHeadshotMultiplier() + 1.0);
        subject.setBodyPartModifier(subject.getBodyPartModifier() + 1.0);

        assertEquals(2, subject.getCritLevel());
        assertEquals(3.0, subject.getCriticalDamageModifier(), 0.0);
        assertEquals(4.0, subject.getHeadshotMultiplier(), 0.0);
        assertEquals(5.0, subject.getBodyPartModifier(), 0.0);

    }

}