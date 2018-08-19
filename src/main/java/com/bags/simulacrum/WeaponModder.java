package com.bags.simulacrum;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeaponModder {

    public double calculateAccuracy(double accuracy, List<Mod> weaponMods) {
        double accuracyIncrease = weaponMods.stream().filter(mod -> mod.getAccuracyIncrease() != 0.0).mapToDouble(Mod::getAccuracyIncrease).sum();
        return accuracy + accuracyIncrease;
    }

    public void calculateFireRate(Weapon weaponToMod, List<Mod> weaponMods) {
        double fireRateIncrease =  weaponMods.stream().filter(mod -> mod.getFireRateIncrease() != 0).mapToDouble(Mod::getFireRateIncrease).sum();
        System.out.println(fireRateIncrease);
        if (fireRateIncrease != 0.0) {
            weaponToMod.setFireRate(weaponToMod.getFireRate() * (1 + fireRateIncrease));
        }
    }

    public void calculateCriticalDamage(Weapon weaponToMod, List<Mod> weaponMods) {
        double criticalDamageIncrease = weaponMods.stream().filter(mod -> mod.getCriticalDamageIncrease() != 0).mapToDouble(Mod::getCriticalDamageIncrease).sum();
        if (criticalDamageIncrease != 0.0) {
            weaponToMod.setCriticalDamage(weaponToMod.getCriticalDamage() * (1 + criticalDamageIncrease));
        }
    }

    public void calculateCriticalChance(Weapon weaponToMod, List<Mod> weaponMods) {
        double criticalChanceIncrease = weaponMods.stream().filter(mod -> mod.getCriticalChanceIncrease() != 0).mapToDouble(Mod::getCriticalChanceIncrease).sum();
        if (criticalChanceIncrease != 0.0) {
            weaponToMod.setCriticalChance(weaponToMod.getCriticalChance() * ( 1 + criticalChanceIncrease));
        }
    }
}
