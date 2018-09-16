package com.bags.simulacrum.Armor;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HealthClassTest {

    private HealthClass subject;

    @Test
    public void itCanCheckIfItsArmor_1() {
        subject = HealthClass.INFESTED_FLESH;

        assertFalse(HealthClass.isArmor(subject));
    }

    @Test
    public void itCanCheckIfItsArmor_2() {
        subject = HealthClass.FERRITE;

        assertTrue(HealthClass.isArmor(subject));
    }

    @Test
    public void itCanCheckIfItsShields() {
        subject = HealthClass.PROTO_SHIELD;

        assertTrue(HealthClass.isShield(subject));
    }

    @Test
    public void itCanCheckIfItsShields_2() {
        subject = HealthClass.FERRITE;

        assertFalse(HealthClass.isShield(subject));
    }
}