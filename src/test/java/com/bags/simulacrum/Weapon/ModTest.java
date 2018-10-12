package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import org.junit.Before;
import org.junit.Test;

import static com.bags.simulacrum.Damage.DamageType.HEAT;
import static com.bags.simulacrum.Damage.DamageType.PUNCTURE;
import static org.junit.Assert.assertEquals;

public class ModTest {

    private Mod subject;

    @Before
    public void setUp() {
        subject = new Mod();
    }

    @Test
    public void itCanHaveSetterTestCoverage() {
//        subject.setName("Serration");
//        subject.setLevel(1);
//        subject.setDrain(1);
//        subject.setRarity("Rare");
//        subject.setPolarity(Polarity.V);
        subject.setRangeLimitIncrease(1.25);
        subject.setFireRateIncrease(1.25);
        subject.setAccuracyIncrease(1.25);
        subject.setMagazineSizeIncrease(1.25);
        subject.setMaxAmmoIncrease(1.25);
        subject.setReloadTimeIncrease(1.25);
        subject.setCriticalChanceIncrease(1.25);
        subject.setCriticalDamageIncrease(1.25);
        subject.setStatusChanceIncrease(1.25);
        subject.setHeadshotMultiplierIncrease(1.25);
        subject.setDamageIncrease(1.25);
        subject.setMultishotIncrease(1.25);
//        subject.setIndex(0);
        subject.setDamage(new Damage(HEAT, 0.0, 0.75));

        assertEquals(1.25, subject.getRangeLimitIncrease(), 0.0);
        assertEquals(1.25, subject.getFireRateIncrease(), 0.0);
        assertEquals(1.25, subject.getAccuracyIncrease(), 0.0);
        assertEquals(1.25, subject.getMagazineSizeIncrease(), 0.0);
        assertEquals(1.25, subject.getMaxAmmoIncrease(), 0.0);
        assertEquals(1.25, subject.getReloadTimeIncrease(), 0.0);
        assertEquals(1.25, subject.getCriticalChanceIncrease(), 0.0);
        assertEquals(1.25, subject.getCriticalDamageIncrease(), 0.0);
        assertEquals(1.25, subject.getStatusChanceIncrease(), 0.0);
        assertEquals(1.25, subject.getHeadshotMultiplierIncrease(), 0.0);
        assertEquals(1.25, subject.getDamageIncrease(), 0.0);
//        assertEquals(0, subject.getIndex());
//        assertEquals(1, subject.getDrain());
//        assertEquals(1, subject.getLevel());
//        assertEquals("Serration", subject.getName());
//        assertEquals("Rare", subject.getRarity());
//        assertEquals(Polarity.V, subject.getPolarity());
        assertEquals(HEAT, subject.getDamage().getDamageType());
        assertEquals(0.0, subject.getDamage().getDamageValue(), 0.0);
        assertEquals(0.75, subject.getDamage().getModAddedDamageRatio(), 0.0);
    }

    @Test
    public void itCanHaveConstructorTestCoverage() {
        subject = new Mod(new Damage(PUNCTURE, 0.0, 0.75));

        assertEquals(PUNCTURE, subject.getDamage().getDamageType());
        assertEquals(0.0, subject.getDamage().getDamageValue(), 0.0);
        assertEquals(0.75, subject.getDamage().getModAddedDamageRatio(), 0.0);
    }
}