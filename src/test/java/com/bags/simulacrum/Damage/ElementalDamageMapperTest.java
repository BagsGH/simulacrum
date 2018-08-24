//package com.bags.simulacrum.Damage;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//
//import static org.junit.Assert.assertEquals;
//
//public class ElementalDamageMapperTest {
//
//    @InjectMocks
//    private ElementalDamageMapper subject;
//
//    @Before
//    public void setup() {
//        subject = new ElementalDamageMapper();
//    }
//
//    @Test
//    public void itCanCombineTwoDifferentElementsCorrectly() {
//        Damage heat = new Damage(DamageType.HEAT);
//        Damage toxin = new Damage(DamageType.TOXIN);
//
//        Damage combined = subject.combineElements(heat, toxin);
//
//        assertEquals(DamageType.GAS, combined.getType());
//    }
//
//    @Test
//    public void itCanCombineTwoIdenticalTypesIntoThemselves() {
//        Damage heat = new Damage(DamageType.HEAT);
//        Damage heat2 = new Damage(DamageType.HEAT);
//
//        Damage combined = subject.combineElements(heat, heat2);
//
//        assertEquals(DamageType.HEAT, combined.getType());
//    }
//
//}