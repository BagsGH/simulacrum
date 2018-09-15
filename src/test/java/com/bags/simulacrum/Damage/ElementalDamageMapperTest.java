package com.bags.simulacrum.Damage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.assertEquals;

public class ElementalDamageMapperTest {

    @InjectMocks
    private ElementalDamageMapper subject;

    @Before
    public void setup() {
        subject = new ElementalDamageMapper();
    }

    @Test
    public void itCanCombineTwoElementals() {
        assertEquals(DamageType.GAS, subject.combineElements(DamageType.HEAT, DamageType.TOXIN));
    }

    @Test
    public void itCanCombineTwoElementalsOfTheSameType() {
        assertEquals(DamageType.HEAT, subject.combineElements(DamageType.HEAT, DamageType.HEAT));
    }

}