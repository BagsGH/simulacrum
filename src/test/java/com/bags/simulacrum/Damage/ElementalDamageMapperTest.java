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
    public void itCanCombineTwoDifferentElementsCorrectly() {
        Damage heat = new Damage(Damage.DamageType.HEAT);
        Damage toxin = new Damage(Damage.DamageType.TOXIN);

        Damage combined = subject.combineElements(heat, toxin);

        assertEquals(Damage.DamageType.GAS, combined.getType());
    }

    @Test
    public void itCanCombeTwoDuplicateTypesIntoThemselves() {
        Damage heat = new Damage(Damage.DamageType.HEAT);
        Damage heat2 = new Damage(Damage.DamageType.HEAT);

        Damage combined = subject.combineElements(heat, heat2);

        assertEquals(Damage.DamageType.HEAT, combined.getType());
    }

}