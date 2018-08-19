package com.bags.simulacrum;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class WeaponModifierTest {

    @InjectMocks
    private WeaponModifier subject;

    private Weapon fakeWeapon;

    private Mod fakeMod;
    private Mod anotherFakeMod;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        subject = new WeaponModifier();
        fakeWeapon = new Weapon();

        fakeMod = new Mod();
        anotherFakeMod = new Mod();
    }

    @Test
    public void itCopiesTheFluffDataFromOriginalWeaponToModifiedWeapon()
    {
        fakeWeapon.setName("someWeaponName");
        fakeWeapon.setMasteryRank(57);
        fakeWeapon.setSlot(Weapon.Slot.PRIMARY);
        fakeWeapon.setType(Weapon.WeaponType.RIFLE);
        fakeWeapon.setAmmoType(Weapon.AmmoType.RIFLE);
        fakeWeapon.setRangeLimit(2375.0);
        fakeWeapon.setNoiseLevel(Weapon.NoiseLevel.SILENT);
        fakeWeapon.setMaxAmmo(1234);
        fakeWeapon.setDisposition(Weapon.Disposition.STRONG);
        fakeWeapon.setMods(new ArrayList<>());

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(fakeWeapon, actualWeaponModified);
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
    public void itCanCorrectlyCalculatePositiveCriticalChance()
    {
        Mod fakeCriticalChanceMod = new Mod();
        fakeCriticalChanceMod.setCriticalChanceIncrease(1.50);
        fakeWeapon.setCriticalChance(0.17);
        fakeWeapon.setMods(Collections.singletonList(fakeCriticalChanceMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(.425, actualWeaponModified.getCriticalChance(), .001);
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

    @Test
    public void itCanCorrectlyCalculatePositiveReloadTime()
    {
        Mod fakeReloadTimeMod = new Mod();
        fakeReloadTimeMod.setReloadTimeIncrease(0.30);
        fakeWeapon.setReloadTime(1.70);
        fakeWeapon.setMods(Collections.singletonList(fakeReloadTimeMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(1.308, actualWeaponModified.getReloadTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeReloadTime()
    {
        Mod fakeReloadTimeMod = new Mod();
        fakeReloadTimeMod.setReloadTimeIncrease(-0.33);
        fakeWeapon.setReloadTime(1.70);
        fakeWeapon.setMods(Collections.singletonList(fakeReloadTimeMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(2.537, actualWeaponModified.getReloadTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexReloadTime()
    {
        Mod fakeReloadTimeMod = new Mod();
        fakeReloadTimeMod.setReloadTimeIncrease(-0.33);
        Mod fakeReloadTimeMod2 = new Mod();
        fakeReloadTimeMod2.setReloadTimeIncrease(0.55);
        fakeWeapon.setReloadTime(1.70);
        fakeWeapon.setMods(Arrays.asList(fakeReloadTimeMod, fakeReloadTimeMod2));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(1.393, actualWeaponModified.getReloadTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveStatusChance()
    {
        Mod fakeStatusChanceMod = new Mod();
        fakeStatusChanceMod.setStatusChanceIncrease(0.40);
        fakeWeapon.setStatusChance(0.29);
        fakeWeapon.setMods(Collections.singletonList(fakeStatusChanceMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(.406, actualWeaponModified.getStatusChance(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexStatusChance()
    {
        Mod fakeStatusChanceMod = new Mod();
        fakeStatusChanceMod.setStatusChanceIncrease(0.60);
        Mod fakeStatusChanceMod2 = new Mod();
        fakeStatusChanceMod2.setStatusChanceIncrease(0.60);
        fakeWeapon.setStatusChance(0.29);
        fakeWeapon.setMods(Arrays.asList(fakeStatusChanceMod, fakeStatusChanceMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(.638, actualWeaponModified.getStatusChance(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveMagazineSize()
    {
        fakeMod.setMagazineSizeIncrease(0.66);
        fakeWeapon.setMagazineSize(200);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(332, actualWeaponModified.getMagazineSize());
    }

    @Test
    public void itCanCorrectlyCalculateNegativeMagazineSize()
    {
        fakeMod.setMagazineSizeIncrease(-0.10);
        fakeWeapon.setMagazineSize(200);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(180, actualWeaponModified.getMagazineSize());
    }

    @Test
    public void itCanCorrectlyCalculateComplexMagazineSize()
    {
        fakeMod.setMagazineSizeIncrease(-0.10);
        anotherFakeMod.setMagazineSizeIncrease(0.66);
        fakeWeapon.setMagazineSize(200);
        fakeWeapon.setMods(Arrays.asList(fakeMod, anotherFakeMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(312, actualWeaponModified.getMagazineSize());
    }

    @Test
    public void itCanCorrectlyCalculatePositiveMaxAmmo()
    {
        fakeMod.setMaxAmmoIncrease(0.30);
        fakeWeapon.setMaxAmmo(800);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));

        Weapon actualWeaponModified = subject.modifyWeapon(fakeWeapon);

        assertEquals(1040, actualWeaponModified.getMaxAmmo());
    }

}