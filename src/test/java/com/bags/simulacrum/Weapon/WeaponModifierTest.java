package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageSourceType;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class WeaponModifierTest {

    @Mock
    private DamageModHelper damageModHelperMock;

    @InjectMocks
    private WeaponModifier subject;

    private Weapon fakeWeapon;
    private Mod fakeMod;
    private Mod anotherFakeMod;

    private WeaponInformation fakeWeaponInformation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        fakeWeaponInformation = new WeaponInformation(WeaponClass.RIFLE, WeaponSlot.PRIMARY, 9, AmmoType.RIFLE, NoiseLevel.ALARMING, Disposition.MILD);
        fakeWeapon = new Weapon();
        fakeWeapon.setFireStatusProperties(new FireStatusProperties());
        fakeWeapon.setWeaponInformation(fakeWeaponInformation);

        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 5.0, 0.0)));
        fakeWeapon.addDamageSource(damageSource);

        DamageSource fakeDamageSourceReturned = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 5.0, 0.0)));
        when(damageModHelperMock.calculateDamageSources(eq(damageSource), any())).thenReturn(fakeDamageSourceReturned);

        fakeMod = new Mod();
        anotherFakeMod = new Mod();
    }

    @Test
    public void itCopiesTheStaticDataFromOriginalWeaponToModifiedWeapon() {
        WeaponInformation fakeWeaponInformation = new WeaponInformation(WeaponClass.RIFLE, WeaponSlot.PRIMARY, 57, AmmoType.RIFLE, NoiseLevel.SILENT, Disposition.STRONG);
        fakeWeapon.setName("someWeaponName");
        fakeWeapon.setWeaponInformation(fakeWeaponInformation);
        fakeWeapon.setRangeLimit(2375.0);
        fakeWeapon.setMaxAmmo(1234);
        fakeWeapon.setMods(new ArrayList<>());
        fakeWeapon.setTriggerType(FireStatusProperties.TriggerType.HELD);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(fakeWeapon, actualWeaponModified);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveAccuracy() {
        Mod fakeMod = new Mod();
        fakeMod.setAccuracyIncrease(0.3);
        fakeWeapon.setAccuracy(1.00);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(1.30, actualWeaponModified.getAccuracy(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeAccuracy() {
        Mod fakeMod = new Mod();
        fakeMod.setAccuracyIncrease(-0.3);
        fakeWeapon.setAccuracy(1.00);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(0.70, actualWeaponModified.getAccuracy(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexAccuracy() {
        Mod fakeMod = new Mod();
        fakeMod.setAccuracyIncrease(0.3);
        Mod fakeMod2 = new Mod();
        fakeMod2.setAccuracyIncrease(-0.55);
        fakeWeapon.setAccuracy(1.00);
        fakeWeapon.setMods(Arrays.asList(fakeMod, fakeMod2));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(0.75, actualWeaponModified.getAccuracy(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveFireRate() {
        Mod fakeFireRateMod = new Mod();
        fakeFireRateMod.setFireRateIncrease(0.90);
        fakeWeapon.setFireRate(8.0);
        fakeWeapon.setMods(Collections.singletonList(fakeFireRateMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(15.200, actualWeaponModified.getFireRate(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeFireRate() {
        Mod fakeFireRateMod = new Mod();
        fakeFireRateMod.setFireRateIncrease(-0.36);
        fakeWeapon.setFireRate(8.0);
        fakeWeapon.setMods(Collections.singletonList(fakeFireRateMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(5.120, actualWeaponModified.getFireRate(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexFireRate() {
        Mod fakeFireRateMod = new Mod();
        fakeFireRateMod.setFireRateIncrease(-0.36);
        Mod fakeFireRateMod2 = new Mod();
        fakeFireRateMod2.setFireRateIncrease(0.90);
        fakeWeapon.setFireRate(8.0);
        fakeWeapon.setMods(Arrays.asList(fakeFireRateMod, fakeFireRateMod2));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(12.320, actualWeaponModified.getFireRate(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveChargeTimeForBows() {
        fakeMod.setFireRateIncrease(0.90);
        fakeWeapon.setChargeTime(0.5);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));
        fakeWeapon.getWeaponInformation().setWeaponClass(WeaponClass.BOW);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(0.179, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveChargeTime() {
        fakeMod.setFireRateIncrease(0.90);
        fakeWeapon.setChargeTime(2.0);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));
        fakeWeapon.getWeaponInformation().setWeaponClass(WeaponClass.RIFLE);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(1.053, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeChargeTimeForBows() {
        fakeMod.setFireRateIncrease(-0.36);
        fakeWeapon.setChargeTime(0.50);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));
        fakeWeapon.getWeaponInformation().setWeaponClass(WeaponClass.BOW);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(0.781, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeChargeTime() {
        fakeMod.setFireRateIncrease(-0.36);
        fakeWeapon.setChargeTime(2.0);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));
        fakeWeapon.getWeaponInformation().setWeaponClass(WeaponClass.RIFLE);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(3.125, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexChargeTimeForBows() {
        fakeMod.setFireRateIncrease(-0.36);
        anotherFakeMod.setFireRateIncrease(0.90);
        fakeWeapon.setChargeTime(1.20);
        fakeWeapon.setMods(Arrays.asList(fakeMod, anotherFakeMod));
        fakeWeapon.getWeaponInformation().setWeaponClass(WeaponClass.BOW);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(0.492, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexCharge() {
        fakeMod.setFireRateIncrease(-0.36);
        anotherFakeMod.setFireRateIncrease(0.90);
        fakeWeapon.setChargeTime(2.0);
        fakeWeapon.setMods(Arrays.asList(fakeMod, anotherFakeMod));
        fakeWeapon.getWeaponInformation().setWeaponClass(WeaponClass.RIFLE);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(1.299, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveCriticalDamage() {
        Mod fakeCriticalDamageMod = new Mod();
        fakeCriticalDamageMod.setCriticalDamageIncrease(1.20);
        fakeWeapon.setCriticalDamage(2.5);
        fakeWeapon.setMods(Collections.singletonList(fakeCriticalDamageMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(5.5, actualWeaponModified.getCriticalDamage(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexCriticalDamage() {
        Mod fakeCriticalDamageMod = new Mod();
        fakeCriticalDamageMod.setCriticalDamageIncrease(1.20);
        Mod fakeCriticalDamageMod2 = new Mod();
        fakeCriticalDamageMod2.setCriticalDamageIncrease(0.15);
        fakeWeapon.setCriticalDamage(2.5);
        fakeWeapon.setMods(Arrays.asList(fakeCriticalDamageMod, fakeCriticalDamageMod2));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(5.875, actualWeaponModified.getCriticalDamage(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveCriticalChance() {
        Mod fakeCriticalChanceMod = new Mod();
        fakeCriticalChanceMod.setCriticalChanceIncrease(1.50);
        fakeWeapon.setCriticalChance(0.17);
        fakeWeapon.setMods(Collections.singletonList(fakeCriticalChanceMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(.425, actualWeaponModified.getCriticalChance(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexCriticalChance() {
        Mod fakeCriticalChanceMod = new Mod();
        fakeCriticalChanceMod.setCriticalChanceIncrease(1.50);
        Mod fakeCriticalChanceMod2 = new Mod();
        fakeCriticalChanceMod2.setCriticalChanceIncrease(0.08);
        fakeWeapon.setCriticalChance(0.17);
        fakeWeapon.setMods(Arrays.asList(fakeCriticalChanceMod, fakeCriticalChanceMod2));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(.439, actualWeaponModified.getCriticalChance(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveReloadTime() {
        Mod fakeReloadTimeMod = new Mod();
        fakeReloadTimeMod.setReloadTimeIncrease(0.30);
        fakeWeapon.setReloadTime(1.70);
        fakeWeapon.setMods(Collections.singletonList(fakeReloadTimeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(1.308, actualWeaponModified.getReloadTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeReloadTime() {
        Mod fakeReloadTimeMod = new Mod();
        fakeReloadTimeMod.setReloadTimeIncrease(-0.33);
        fakeWeapon.setReloadTime(1.70);
        fakeWeapon.setMods(Collections.singletonList(fakeReloadTimeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(2.537, actualWeaponModified.getReloadTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexReloadTime() {
        Mod fakeReloadTimeMod = new Mod();
        fakeReloadTimeMod.setReloadTimeIncrease(-0.33);
        Mod fakeReloadTimeMod2 = new Mod();
        fakeReloadTimeMod2.setReloadTimeIncrease(0.55);
        fakeWeapon.setReloadTime(1.70);
        fakeWeapon.setMods(Arrays.asList(fakeReloadTimeMod, fakeReloadTimeMod2));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(1.393, actualWeaponModified.getReloadTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveStatusChance() {
        Mod fakeStatusChanceMod = new Mod();
        fakeStatusChanceMod.setStatusChanceIncrease(0.40);
        fakeWeapon.setStatusChance(0.29);
        fakeWeapon.setMods(Collections.singletonList(fakeStatusChanceMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(.406, actualWeaponModified.getStatusChance(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexStatusChance() {
        Mod fakeStatusChanceMod = new Mod();
        fakeStatusChanceMod.setStatusChanceIncrease(0.60);
        Mod fakeStatusChanceMod2 = new Mod();
        fakeStatusChanceMod2.setStatusChanceIncrease(0.60);
        fakeWeapon.setStatusChance(0.29);
        fakeWeapon.setMods(Arrays.asList(fakeStatusChanceMod, fakeStatusChanceMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(.638, actualWeaponModified.getStatusChance(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveMagazineSize() {
        fakeMod.setMagazineSizeIncrease(0.66);
        fakeWeapon.setMagazineSize(200);
        fakeWeapon.addMod(fakeMod);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(332, actualWeaponModified.getMagazineSize());
    }

    @Test
    public void itCanCorrectlyCalculateNegativeMagazineSize() {
        fakeMod.setMagazineSizeIncrease(-0.10);
        fakeWeapon.setMagazineSize(200);
        fakeWeapon.addMod(fakeMod);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(180, actualWeaponModified.getMagazineSize());
    }

    @Test
    public void itCanCorrectlyCalculateComplexMagazineSize() {
        fakeMod.setMagazineSizeIncrease(-0.10);
        anotherFakeMod.setMagazineSizeIncrease(0.66);
        fakeWeapon.setMagazineSize(200);
        fakeWeapon.setMods(Arrays.asList(fakeMod, anotherFakeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(312, actualWeaponModified.getMagazineSize());
    }

    @Test
    public void itCanCorrectlyCalculatePositiveMaxAmmo() {
        fakeMod.setMaxAmmoIncrease(0.30);
        fakeWeapon.setMaxAmmo(800);
        fakeWeapon.addMod(fakeMod);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(1040, actualWeaponModified.getMaxAmmo());
    }

    @Test
    public void itCanCorrectlyCalculatePositiveMultishot() {
        fakeMod.setMultishotIncrease(0.90);
        fakeWeapon.setMultishot(1.00);
        fakeWeapon.setMods(Arrays.asList(fakeMod));

        Weapon actualModdedWeapon = subject.modWeapon(fakeWeapon);

        assertEquals(1.90, actualModdedWeapon.getMultishot(), 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexMultishot() {
        fakeMod.setMultishotIncrease(0.90);
        anotherFakeMod.setMultishotIncrease(0.60);
        fakeWeapon.setMultishot(1.00);
        fakeWeapon.setMods(Arrays.asList(fakeMod, anotherFakeMod));

        Weapon actualModdedWeapon = subject.modWeapon(fakeWeapon);

        assertEquals(2.50, actualModdedWeapon.getMultishot(), 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateMultipleDamagesOnMultipleDamageSources() {
        fakeMod.setDamage(new Damage(DamageType.TOXIN, 0.0, 0.90));
        anotherFakeMod.setDamageIncrease(1.65);

        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, null);
        damageSource.addDamage(new Damage(DamageType.IMPACT, 35.0, 0.0));
        damageSource.addDamage(new Damage(DamageType.HEAT, 35.0, 0.0));

        DamageSource damageSource2 = new DamageSource(DamageSourceType.HIT_AOE, null);
        damageSource2.addDamage(new Damage(DamageType.PUNCTURE, 35.0, 0.0));
        damageSource2.addDamage(new Damage(DamageType.COLD, 35.0, 0.0));

        fakeWeapon.setDamageSources(Arrays.asList(damageSource, damageSource2));
        List<Mod> fakeModList = Arrays.asList(fakeMod, anotherFakeMod);
        fakeWeapon.setMods(fakeModList);

        DamageSource fakeReturnedDamageSource1 = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.IMPACT, 92.75), new Damage(DamageType.GAS, 259.7)));
        DamageSource fakeReturnedDamageSource2 = new DamageSource(DamageSourceType.HIT_AOE, Arrays.asList(new Damage(DamageType.PUNCTURE, 92.75), new Damage(DamageType.VIRAL, 259.7)));

        when(damageModHelperMock.calculateDamageSources(eq(damageSource), eq(fakeModList))).thenReturn(fakeReturnedDamageSource1);
        when(damageModHelperMock.calculateDamageSources(eq(damageSource2), eq(fakeModList))).thenReturn(fakeReturnedDamageSource2);

        Weapon actualModifiedWeapon = subject.modWeapon(fakeWeapon);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 92.75), actualModifiedWeapon.getDamageSources().get(0).getDamages(), 0.001);
        assertExpectedDamageExists(new Damage(DamageType.GAS, 259.7), actualModifiedWeapon.getDamageSources().get(0).getDamages(), 0.001);
        assertEquals(DamageSourceType.PROJECTILE, actualModifiedWeapon.getDamageSources().get(0).getDamageSourceType());
        assertExpectedDamageExists(new Damage(DamageType.PUNCTURE, 92.75), actualModifiedWeapon.getDamageSources().get(1).getDamages(), 0.001);
        assertExpectedDamageExists(new Damage(DamageType.VIRAL, 259.7), actualModifiedWeapon.getDamageSources().get(1).getDamages(), 0.001);
        assertEquals(DamageSourceType.HIT_AOE, actualModifiedWeapon.getDamageSources().get(1).getDamageSourceType());
    }

    @Test
    public void itCanCorrectlyCalculateLenzWithMultipleDamageSources() {
        setupFakeWeaponAsLenz();


        Weapon actualModifiedWeapon = subject.modWeapon(fakeWeapon);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 207.5), actualModifiedWeapon.getDamageSources().get(0).getDamages(), 0.001);
        assertExpectedDamageExists(new Damage(DamageType.BLAST, 373.5), actualModifiedWeapon.getDamageSources().get(0).getDamages(), 0.001);
        assertEquals(DamageSourceType.PROJECTILE, actualModifiedWeapon.getDamageSources().get(0).getDamageSourceType());
        assertExpectedDamageExists(new Damage(DamageType.BLAST, 116.2), actualModifiedWeapon.getDamageSources().get(1).getDamages(), 0.001);
        assertEquals(DamageSourceType.HIT_AOE, actualModifiedWeapon.getDamageSources().get(1).getDamageSourceType());
        assertExpectedDamageExists(new Damage(DamageType.BLAST, 7669.2), actualModifiedWeapon.getDamageSources().get(2).getDamages(), 0.001);
        assertEquals(DamageSourceType.DELAYED_AOE, actualModifiedWeapon.getDamageSources().get(2).getDamageSourceType());
        assertEquals(1.25, actualModifiedWeapon.getCriticalChance(), 0.001);
        assertEquals(4.4, actualModifiedWeapon.getCriticalDamage(), 0.001);
        assertEquals(0.429, actualModifiedWeapon.getChargeTime(), 0.001);
        assertEquals(1.90, actualModifiedWeapon.getMultishot(), 0.001);
    }

    private void setupFakeWeaponAsLenz() {
        DamageSource shotSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 50)));
        DamageSource impactExplosion = new DamageSource(DamageSourceType.HIT_AOE, Collections.singletonList(new Damage(DamageType.COLD, 10)), 0, 10.0);
        DamageSource delayExplosion = new DamageSource(DamageSourceType.DELAYED_AOE, Collections.singletonList(new Damage(DamageType.BLAST, 660)), 2.0, 10.0);

        fakeWeapon.setDamageSources(Arrays.asList(shotSource, impactExplosion, delayExplosion));
        fakeWeapon.setCriticalChance(0.50);
        fakeWeapon.setCriticalDamage(2.0);
        fakeWeapon.setChargeTime(1.2);
        fakeWeapon.setMultishot(1.0);
        fakeWeapon.getWeaponInformation().setWeaponClass(WeaponClass.BOW);

        Mod vileAcceleration = new Mod();
        vileAcceleration.setDamageIncrease(-0.15);
        vileAcceleration.setFireRateIncrease(0.90);

        Mod hellfire = new Mod(new Damage(DamageType.HEAT, 0.0, 0.90));
        Mod cryoRounds = new Mod(new Damage(DamageType.COLD, 0.0, 0.90));
        Mod serration = new Mod();
        serration.setDamageIncrease(1.65);

        Mod heavyCaliber = new Mod();
        heavyCaliber.setDamageIncrease(1.65);
        heavyCaliber.setAccuracyIncrease(-0.55);

        Mod vitalSense = new Mod();
        vitalSense.setCriticalDamageIncrease(1.20);

        Mod pointStrike = new Mod();
        pointStrike.setCriticalChanceIncrease(1.50);

        Mod splitChamber = new Mod();
        splitChamber.setMultishotIncrease(0.90);

        List<Mod> fakeModList = Arrays.asList(vileAcceleration, hellfire, heavyCaliber, splitChamber, serration, cryoRounds, vitalSense, pointStrike);
        fakeWeapon.setMods(fakeModList);

        DamageSource fakeReturnedDamageSource1 = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.IMPACT, 207.5), new Damage(DamageType.BLAST, 373.5)));
        DamageSource fakeReturnedDamageSource2 = new DamageSource(DamageSourceType.HIT_AOE, Collections.singletonList(new Damage(DamageType.BLAST, 116.2)));
        DamageSource fakeReturnedDamageSource3 = new DamageSource(DamageSourceType.DELAYED_AOE, Collections.singletonList(new Damage(DamageType.BLAST, 7669.2)));

        when(damageModHelperMock.calculateDamageSources(eq(shotSource), eq(fakeModList))).thenReturn(fakeReturnedDamageSource1);
        when(damageModHelperMock.calculateDamageSources(eq(impactExplosion), eq(fakeModList))).thenReturn(fakeReturnedDamageSource2);
        when(damageModHelperMock.calculateDamageSources(eq(delayExplosion), eq(fakeModList))).thenReturn(fakeReturnedDamageSource3);
    }

    private void assertExpectedDamageExists(Damage damageExpected, List<Damage> actualDamages, double accuracyThreshold) {
        assertTrue(actualDamages.stream().anyMatch(damage -> damage.getType().equals(damageExpected.getType()) && Math.abs(damage.getDamageValue() - damageExpected.getDamageValue()) < accuracyThreshold));
    }
}