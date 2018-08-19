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
        Weapon modifiedWeapon = new Weapon();

        modifiedWeapon.setAccuracy(calculateModdedAccuracy());
        modifiedWeapon.setFireRate(calculateModdedFireRate());
        modifiedWeapon.setCriticalDamage(calculateCriticalDamage());
        modifiedWeapon.setCriticalChance(calculateCriticalChance());

        return modifiedWeapon;
    }

    private double calculateModdedAccuracy() {
        double accuracyIncrease = weaponMods.stream().filter(mod -> mod.getAccuracyIncrease() != 0.0).mapToDouble(Mod::getAccuracyIncrease).sum();
        return weaponToMod.getAccuracy() + accuracyIncrease;
    }

    private double calculateModdedFireRate() {
        double fireRateIncrease =  weaponMods.stream().filter(mod -> mod.getFireRateIncrease() != 0).mapToDouble(Mod::getFireRateIncrease).sum();
        return weaponToMod.getFireRate() * (1 + fireRateIncrease);
    }

    private double calculateCriticalDamage() {
        double criticalDamageIncrease = weaponMods.stream().filter(mod -> mod.getCriticalDamageIncrease() != 0).mapToDouble(Mod::getCriticalDamageIncrease).sum();
        return weaponToMod.getCriticalDamage() * (1 + criticalDamageIncrease);
    }

    public double calculateCriticalChance() {
        double criticalChanceIncrease = weaponMods.stream().filter(mod -> mod.getCriticalChanceIncrease() != 0).mapToDouble(Mod::getCriticalChanceIncrease).sum();
        return weaponToMod.getCriticalChance() * ( 1 + criticalChanceIncrease);
    }
}
