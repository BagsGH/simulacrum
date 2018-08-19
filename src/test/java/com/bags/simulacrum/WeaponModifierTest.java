package com.bags.simulacrum;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class WeaponModifierTest {

    @InjectMocks
    private WeaponModifier subject;

    private Weapon fakeWeapon;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        subject = new WeaponModifier();
        fakeWeapon = new Weapon();
    }

    @Test
    public void itCanCorrectlyCalculatePositiveAccuracy(){
        Mod fakeMod = new Mod();
        fakeMod.setAccuracyIncrease(0.3);
        fakeWeapon.setAccuracy(1.00);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(1.30, actualWeaponModified.getAccuracy(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeAccuracy(){
        Mod fakeMod = new Mod();
        fakeMod.setAccuracyIncrease(-0.3);
        fakeWeapon.setAccuracy(1.00);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(0.70, actualWeaponModified.getAccuracy(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexAccuracy(){
        Mod fakeMod = new Mod();
        fakeMod.setAccuracyIncrease(0.3);
        Mod fakeMod2 = new Mod();
        fakeMod2.setAccuracyIncrease(-0.55);
        fakeWeapon.setAccuracy(1.00);
        fakeWeapon.setMods(Arrays.asList(fakeMod, fakeMod2));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(0.75, actualWeaponModified.getAccuracy(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveFireRate()
    {
        Mod fakeFireRateMod = new Mod();
        fakeFireRateMod.setFireRateIncrease(0.90);
        fakeWeapon.setFireRate(8.0);
        fakeWeapon.setMods(Collections.singletonList(fakeFireRateMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(15.20, actualWeaponModified.getFireRate(), .01);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeFireRate()
    {
        Mod fakeFireRateMod = new Mod();
        fakeFireRateMod.setFireRateIncrease(-0.36);
        fakeWeapon.setFireRate(8.0);
        fakeWeapon.setMods(Collections.singletonList(fakeFireRateMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(5.12, actualWeaponModified.getFireRate(), .01);
    }

    @Test
    public void itCanCorrectlyCalculateComplexFireRate()
    {
        Mod fakeFireRateMod = new Mod();
        fakeFireRateMod.setFireRateIncrease(-0.36);
        Mod fakeFireRateMod2 = new Mod();
        fakeFireRateMod2.setFireRateIncrease(0.90);
        fakeWeapon.setFireRate(8.0);
        fakeWeapon.setMods(Arrays.asList(fakeFireRateMod, fakeFireRateMod2));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(12.32, actualWeaponModified.getFireRate(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveCriticalDamage()
    {
        Mod fakeCriticalDamageMod = new Mod();
        fakeCriticalDamageMod.setCriticalDamageIncrease(1.20);
        fakeWeapon.setCriticalDamage(2.5);
        fakeWeapon.setMods(Collections.singletonList(fakeCriticalDamageMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(5.5, actualWeaponModified.getCriticalDamage(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexCriticalDamage()
    {
        Mod fakeCriticalDamageMod = new Mod();
        fakeCriticalDamageMod.setCriticalDamageIncrease(1.20);
        Mod fakeCriticalDamageMod2 = new Mod();
        fakeCriticalDamageMod2.setCriticalDamageIncrease(0.15);
        fakeWeapon.setCriticalDamage(2.5);
        fakeWeapon.setMods(Arrays.asList(fakeCriticalDamageMod, fakeCriticalDamageMod2));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(5.875, actualWeaponModified.getCriticalDamage(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexCriticalChance()
    {
        Mod fakeCriticalChanceMod = new Mod();
        fakeCriticalChanceMod.setCriticalChanceIncrease(1.50);
        Mod fakeCriticalChanceMod2 = new Mod();
        fakeCriticalChanceMod2.setCriticalChanceIncrease(0.08);
        fakeWeapon.setCriticalChance(0.17);
        fakeWeapon.setMods(Arrays.asList(fakeCriticalChanceMod, fakeCriticalChanceMod2));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(.438, actualWeaponModified.getCriticalChance(), .001);
    }
}