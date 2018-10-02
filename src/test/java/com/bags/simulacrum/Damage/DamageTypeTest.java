package com.bags.simulacrum.Damage;

import org.junit.Before;
import org.junit.Test;

import static com.bags.simulacrum.Damage.DamageType.GAS;
import static com.bags.simulacrum.Damage.DamageType.HEAT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DamageTypeTest {

    private DamageType subject;

    @Before
    public void setup() {
        subject = GAS;
    }

    @Test
    public void itCanIdentifyIfDamageTypeIsElemental() {
        assertTrue(DamageType.isElemental(subject));
    }

    @Test
    public void itCanIdentifyIfDamageTypeIsIPS() {
        assertFalse(DamageType.isIPS(subject));
    }

    @Test
    public void itCanIdentifyIfDamageTypeIsCombinedElemental_1() {
        assertTrue(DamageType.isCombinedElemental(subject));
    }

    @Test
    public void itCanIdentifyIfDamageTypeIsCombinedElemental_2() {
        subject = HEAT;
        assertFalse(DamageType.isCombinedElemental(subject));
    }
}