package com.bags.simulacrum;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeaponModifier {

    private List<Mod> weaponMods;
    private Weapon weaponToMod;

    public Weapon modifyWeapon(Weapon weapon)
    {
        weaponToMod = weapon;
        weaponMods = weaponToMod.getMods();
        Weapon modifiedWeapon = copyWeaponToMod();

        modifiedWeapon.setFireRate(calculateModdedFireRate());
        modifiedWeapon.setAccuracy(calculateModdedAccuracy());
        modifiedWeapon.setMagazineSize(calculateModdedMagazineSize());
        modifiedWeapon.setReloadTime(calculateModdedReloadTime());
        modifiedWeapon.setCriticalChance(calculateModdedCriticalChance());
        modifiedWeapon.setCriticalDamage(calculateModdedCriticalDamage());
        modifiedWeapon.setStatusChance(calculateModdedStatusChance());

        return modifiedWeapon;
    }

    private Weapon copyWeaponToMod() {
        return new Weapon(weaponToMod.getName(), weaponToMod.getMasteryRank(), weaponToMod.getSlot(), weaponToMod.getType(), weaponToMod.getAmmoType(),
                weaponToMod.getRangeLimit(), weaponToMod.getNoiseLevel(), weaponToMod.getMaxAmmo(),weaponToMod.getDisposition(), weaponToMod.getMods());
    }

    private double calculateModdedFireRate() {
        double fireRateIncrease =  weaponMods.stream().filter(mod -> mod.getFireRateIncrease() != 0).mapToDouble(Mod::getFireRateIncrease).sum();
        return weaponToMod.getFireRate() * (1 + fireRateIncrease);
    }

    private double calculateModdedAccuracy() {
        double accuracyIncrease = weaponMods.stream().filter(mod -> mod.getAccuracyIncrease() != 0.0).mapToDouble(Mod::getAccuracyIncrease).sum();
        return weaponToMod.getAccuracy() + accuracyIncrease;
    }

    private int calculateModdedMagazineSize() {
        double magazineSizeIncrease = weaponMods.stream().filter(mod -> mod.getMagazineSizeIncrease() != 0.0).mapToDouble(Mod::getMagazineSizeIncrease).sum();
        return (int) Math.round((double) weaponToMod.getMagazineSize() * (1 + magazineSizeIncrease));
    }

    private double calculateModdedReloadTime() {
        double reloadSpeedIncrease = weaponMods.stream().filter(mod -> mod.getReloadTimeIncrease() != 0.0).mapToDouble(Mod::getReloadTimeIncrease).sum();
        return weaponToMod.getReloadTime() / ( 1 + reloadSpeedIncrease);
    }

    private double calculateModdedCriticalDamage() {
        double criticalDamageIncrease = weaponMods.stream().filter(mod -> mod.getCriticalDamageIncrease() != 0).mapToDouble(Mod::getCriticalDamageIncrease).sum();
        return weaponToMod.getCriticalDamage() * (1 + criticalDamageIncrease);
    }

    private double calculateModdedCriticalChance() {
        double criticalChanceIncrease = weaponMods.stream().filter(mod -> mod.getCriticalChanceIncrease() != 0).mapToDouble(Mod::getCriticalChanceIncrease).sum();
        return weaponToMod.getCriticalChance() * ( 1 + criticalChanceIncrease);
    }

    private double calculateModdedStatusChance() {
        double statusChanceIncrease = weaponMods.stream().filter(mod -> mod.getStatusChanceIncrease() != 0).mapToDouble(Mod::getStatusChanceIncrease).sum();
        return weaponToMod.getStatusChance() * ( 1 + statusChanceIncrease );
    }
}
