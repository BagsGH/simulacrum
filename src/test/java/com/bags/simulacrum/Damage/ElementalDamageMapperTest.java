package com.bags.simulacrum.Damage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import static com.bags.simulacrum.Damage.DamageType.*;
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
        assertEquals(GAS, subject.combineElements(HEAT, TOXIN));
    }

    @Test
    public void itCanCombineTwoElementalsOfTheSameType() {
        assertEquals(HEAT, subject.combineElements(HEAT, HEAT));
    }

}