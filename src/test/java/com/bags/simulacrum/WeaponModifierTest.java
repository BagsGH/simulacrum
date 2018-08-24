package com.bags.simulacrum;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Weapon.Mod;
import com.bags.simulacrum.Weapon.Weapon;
import com.bags.simulacrum.Weapon.WeaponModifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class WeaponModifierTest {

    @InjectMocks
    private WeaponModifier subject;

    private Weapon fakeWeapon;

    private Mod fakeMod;
    private Mod anotherFakeMod;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        subject = new WeaponModifier();
        fakeWeapon = new Weapon();
        fakeWeapon.setType(Weapon.WeaponType.RIFLE);
        fakeWeapon.setDamageTypes(new ArrayList<>());

        fakeMod = new Mod();
        anotherFakeMod = new Mod();
    }

    @Test
    public void itCopiesTheStaticDataFromOriginalWeaponToModifiedWeapon() {
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
        fakeWeapon.setTriggerType(Weapon.TriggerType.HELD);

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
        fakeWeapon.setType(Weapon.WeaponType.BOW);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(0.179, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculatePositiveChargeTime() {
        fakeMod.setFireRateIncrease(0.90);
        fakeWeapon.setChargeTime(2.0);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));
        fakeWeapon.setType(Weapon.WeaponType.RIFLE);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(1.053, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeChargeTimeForBows() {
        fakeMod.setFireRateIncrease(-0.36);
        fakeWeapon.setChargeTime(0.50);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));
        fakeWeapon.setType(Weapon.WeaponType.BOW);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(0.781, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeChargeTime() {
        fakeMod.setFireRateIncrease(-0.36);
        fakeWeapon.setChargeTime(2.0);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));
        fakeWeapon.setType(Weapon.WeaponType.RIFLE);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(3.125, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexChargeTimeForBows() {
        fakeMod.setFireRateIncrease(-0.36);
        anotherFakeMod.setFireRateIncrease(0.90);
        fakeWeapon.setChargeTime(1.20);
        fakeWeapon.setMods(Arrays.asList(fakeMod, anotherFakeMod));
        fakeWeapon.setType(Weapon.WeaponType.BOW);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(0.492, actualWeaponModified.getChargeTime(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexCharge() {
        fakeMod.setFireRateIncrease(-0.36);
        anotherFakeMod.setFireRateIncrease(0.90);
        fakeWeapon.setChargeTime(2.0);
        fakeWeapon.setMods(Arrays.asList(fakeMod, anotherFakeMod));
        fakeWeapon.setType(Weapon.WeaponType.RIFLE);

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
    public void itCanCorrectlyCombineOneElementalModWithWeaponBaseElement() {
        Damage toxic = new Damage(Damage.DamageType.TOXIN);
        toxic.setModAddedDamageRatio(0.60);
        fakeMod.setDamage(toxic);

        Damage heat = new Damage(Damage.DamageType.HEAT);
        heat.setDamageValue(35.0);
        fakeWeapon.setDamageTypes(Collections.singletonList(heat));
        fakeWeapon.addMod(fakeMod);

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        assertEquals(Damage.DamageType.GAS, actualWeaponModified.getDamageTypes().get(0).getType());
    }

    @Test
    public void itCanCorrectlyCalculatePositiveDamage() {
        Damage impact = new Damage(Damage.DamageType.IMPACT);
        impact.setDamageValue(35.00);
        fakeWeapon.setDamageTypes(Collections.singletonList(impact));
        fakeMod.setDamageIncrease(1.65);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        Damage actualModifiedImpactDamage = actualWeaponModified.getDamageTypes().get(0);

        assertEquals(92.75, actualModifiedImpactDamage.getDamageValue(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeDamage() {
        Damage impact = new Damage(Damage.DamageType.IMPACT);
        impact.setDamageValue(35.00);
        fakeWeapon.setDamageTypes(Collections.singletonList(impact));
        fakeMod.setDamageIncrease(-0.15);
        fakeWeapon.setMods(Collections.singletonList(fakeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        Damage actualModifiedImpactDamage = actualWeaponModified.getDamageTypes().get(0);

        assertEquals(29.75, actualModifiedImpactDamage.getDamageValue(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexDamage() {
        Damage impact = new Damage(Damage.DamageType.IMPACT);
        impact.setDamageValue(35.00);
        fakeWeapon.setDamageTypes(Collections.singletonList(impact));
        fakeMod.setDamageIncrease(-0.15);
        anotherFakeMod.setDamageIncrease(1.65);
        fakeWeapon.setMods(Arrays.asList(fakeMod, anotherFakeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        Damage actualModifiedImpactDamage = actualWeaponModified.getDamageTypes().get(0);

        assertEquals(87.5, actualModifiedImpactDamage.getDamageValue(), .001);
    }

    @Test
    public void itCanCorrectlyCalculateDamageAddedBy90PercentToxin() {
        Damage impact = new Damage(Damage.DamageType.IMPACT);
        impact.setDamageValue(35.00);
        fakeWeapon.setDamageTypes(Collections.singletonList(impact));

        Damage toxinModDamage = new Damage(Damage.DamageType.TOXIN);
        toxinModDamage.setModAddedDamageRatio(0.90);
        fakeMod.setDamage(toxinModDamage);

        fakeWeapon.setMods(Arrays.asList(fakeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        Damage actualAddedToxinDamage = actualWeaponModified.getDamageTypes().get(0);
        Damage originalImpactDamage = actualWeaponModified.getDamageTypes().get(1);


        assertEquals(35.0, originalImpactDamage.getDamageValue(), .001);
        assertEquals(31.5, actualAddedToxinDamage.getDamageValue(), .001);
        assertEquals(Damage.DamageType.TOXIN, actualAddedToxinDamage.getType());
        assertEquals(Damage.DamageType.IMPACT, originalImpactDamage.getType());
    }

    @Test
    public void itCanCorrectlyCalculateDamageAddedBy90PercentHeatWhenDefaultWeaponHasHeatDamage() {
        Damage heatDamage = new Damage(Damage.DamageType.HEAT);
        heatDamage.setDamageValue(35.00);
        fakeWeapon.setDamageTypes(Collections.singletonList(heatDamage));

        Damage heatDamageMod = new Damage(Damage.DamageType.HEAT);
        heatDamageMod.setModAddedDamageRatio(0.90);
        fakeMod.setDamage(heatDamageMod);

        fakeWeapon.setMods(Arrays.asList(fakeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        Damage actualAddedDamage = actualWeaponModified.getDamageTypes().get(0);

        assertEquals(66.5, actualAddedDamage.getDamageValue(), .001);
        assertEquals(Damage.DamageType.HEAT, actualAddedDamage.getType());
    }

    @Test
    public void itCanCorrectlyCombineElements() {
        Damage baseWeaponHeatDamage = new Damage(Damage.DamageType.HEAT);
        baseWeaponHeatDamage.setDamageValue(35.00);
        fakeWeapon.setDamageTypes(Collections.singletonList(baseWeaponHeatDamage));

        Damage toxinModDamage = new Damage(Damage.DamageType.TOXIN);
        toxinModDamage.setModAddedDamageRatio(0.90);
        fakeMod.setDamage(toxinModDamage);

        fakeWeapon.setMods(Arrays.asList(fakeMod));

        Weapon actualWeaponModified = subject.modWeapon(fakeWeapon);

        Damage actualGasDamage = actualWeaponModified.getDamageTypes().get(0);


        assertEquals(66.5, actualGasDamage.getDamageValue(), .001);
        assertEquals(Damage.DamageType.GAS, actualGasDamage.getType());
    }

    @Test
    public void itCanCorrectlyCalculatePositiveMultishot() {
        fakeMod.setMultishotIncrease(0.90);
        fakeWeapon.setMultishot(0.00);
        fakeWeapon.setMods(Arrays.asList(fakeMod));

        Weapon actualModdedWeapon = subject.modWeapon(fakeWeapon);

        assertEquals(0.90, actualModdedWeapon.getMultishot(), 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexMultishot() {
        fakeMod.setMultishotIncrease(0.90);
        anotherFakeMod.setMultishotIncrease(0.60);
        fakeWeapon.setMultishot(0.00);
        fakeWeapon.setMods(Arrays.asList(fakeMod, anotherFakeMod));

        Weapon actualModdedWeapon = subject.modWeapon(fakeWeapon);

        assertEquals(01.50, actualModdedWeapon.getMultishot(), 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateIPSDamage() {
        Damage impact = new Damage(Damage.DamageType.IMPACT, 9.0);
        fakeWeapon.setDamageTypes(Arrays.asList(impact));

        Damage modImpact = new Damage(Damage.DamageType.IMPACT, 0.0, 0.20);
        fakeMod.setDamage(modImpact);
        fakeWeapon.setMods(Arrays.asList(fakeMod));

        Weapon actualWeapon = subject.modWeapon(fakeWeapon);

        assertEquals(10.8, actualWeapon.getDamageTypes().get(0).getDamageValue(), 0.001);
        assertEquals(Damage.DamageType.IMPACT, actualWeapon.getDamageTypes().get(0).getType());


    }

}