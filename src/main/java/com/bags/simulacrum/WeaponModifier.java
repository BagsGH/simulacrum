package com.bags.simulacrum;

import org.decimal4j.truncate.DecimalRounding;
import org.decimal4j.util.DoubleRounder;
import org.springframework.stereotype.Component;

import org.decimal4j.*;

import java.text.DecimalFormat;
import java.util.List;

@Component
public class WeaponModifier {

    private List<Mod> weaponMods;
    private Weapon originalWeapon;

    public Weapon modifyWeapon(Weapon weapon) {
        originalWeapon = weapon;
        weaponMods = originalWeapon.getMods();
        Weapon modifiedWeapon = copyWeaponToMod();

        modifiedWeapon.setFireRate(calculateModdedFireRate());
        modifiedWeapon.setAccuracy(calculateModdedAccuracy());
        modifiedWeapon.setMagazineSize(calculateModdedMagazineSize());
        modifiedWeapon.setMaxAmmo(calculateModdedMaxAmmo());
        modifiedWeapon.setReloadTime(calculateModdedReloadTime());
        modifiedWeapon.setChargeTime(calculateModdedChargeTime());
        modifiedWeapon.setCriticalChance(calculateModdedCriticalChance());
        modifiedWeapon.setCriticalDamage(calculateModdedCriticalDamage());
        modifiedWeapon.setStatusChance(calculateModdedStatusChance());

        return modifiedWeapon;
    }

    private Weapon copyWeaponToMod() {
        return new Weapon(originalWeapon.getName(), originalWeapon.getMasteryRank(), originalWeapon.getSlot(), originalWeapon.getType(), originalWeapon.getTriggerType(), originalWeapon.getAmmoType(),
                originalWeapon.getRangeLimit(), originalWeapon.getNoiseLevel(), originalWeapon.getMaxAmmo(), originalWeapon.getDisposition(), originalWeapon.getMods());
    }

    private double calculateModdedFireRate() {
        double fireRateIncrease = weaponMods.stream().filter(mod -> mod.getFireRateIncrease() != 0).mapToDouble(Mod::getFireRateIncrease).sum();
        return DoubleRounder.round(originalWeapon.getFireRate() * (1 + fireRateIncrease), 3);
    }

    private double calculateModdedAccuracy() {
        double accuracyIncrease = weaponMods.stream().filter(mod -> mod.getAccuracyIncrease() != 0.0).mapToDouble(Mod::getAccuracyIncrease).sum();
        return originalWeapon.getAccuracy() + accuracyIncrease;
    }

    private int calculateModdedMagazineSize() {
        double magazineSizeIncrease = weaponMods.stream().filter(mod -> mod.getMagazineSizeIncrease() != 0.0).mapToDouble(Mod::getMagazineSizeIncrease).sum();
        return (int) Math.round((double) originalWeapon.getMagazineSize() * (1 + magazineSizeIncrease));
    }

    private int calculateModdedMaxAmmo() {
        double maxAmmoSizeIncrease = weaponMods.stream().filter(mod -> mod.getMaxAmmoIncrease() != 0.0).mapToDouble(Mod::getMaxAmmoIncrease).sum();
        return (int) Math.round((double) originalWeapon.getMaxAmmo() * (1 + maxAmmoSizeIncrease));
    }

    private double calculateModdedReloadTime() {
        double reloadSpeedIncrease = weaponMods.stream().filter(mod -> mod.getReloadTimeIncrease() != 0.0).mapToDouble(Mod::getReloadTimeIncrease).sum();
        return DoubleRounder.round(originalWeapon.getReloadTime() / (1 + reloadSpeedIncrease), 3);
    }

    private double calculateModdedChargeTime() {
        double chargeTimeIncrease = weaponMods.stream().filter(mod -> mod.getFireRateIncrease() != 0).mapToDouble(mod -> mod.getFireRateIncrease() * weaponTypeFireRateBonusMultiplier(mod.getFireRateIncrease())).sum();
        return DoubleRounder.round(originalWeapon.getChargeTime() / (1 + chargeTimeIncrease), 3);
    }

    /**
     *  If the weapon type is a bow, it gets a 2.0 multiplier to the individual mod's positive fire rate bonus. If it's a negative fire rate bonus mod the multiplier is just 1.0.
     *  Other charge weapons are not affected and always have a multiplier of 1.0.
     *
     * @param fireRateIncrease
     *  The individual mod's raw increase or decrease to fire rate.
     * @return
     *  A multiplier to use when calculating an individual mod's affect on the fire rate.
     */
    private double weaponTypeFireRateBonusMultiplier(double fireRateIncrease) {
        return fireRateIncrease > 0 && originalWeapon.getType().equals(Weapon.WeaponType.BOW) ? 2.0 : 1.0;
    }

    private double calculateModdedCriticalDamage() {
        double criticalDamageIncrease = weaponMods.stream().filter(mod -> mod.getCriticalDamageIncrease() != 0).mapToDouble(Mod::getCriticalDamageIncrease).sum();
        return DoubleRounder.round(originalWeapon.getCriticalDamage() * (1 + criticalDamageIncrease), 3);
    }

    private double calculateModdedCriticalChance() {
        double criticalChanceIncrease = weaponMods.stream().filter(mod -> mod.getCriticalChanceIncrease() != 0).mapToDouble(Mod::getCriticalChanceIncrease).sum();
        return DoubleRounder.round(originalWeapon.getCriticalChance() * (1 + criticalChanceIncrease), 3);
    }

    private double calculateModdedStatusChance() {
        double statusChanceIncrease = weaponMods.stream().filter(mod -> mod.getStatusChanceIncrease() != 0).mapToDouble(Mod::getStatusChanceIncrease).sum();
        return DoubleRounder.round(originalWeapon.getStatusChance() * (1 + statusChanceIncrease), 3);
    }
}
