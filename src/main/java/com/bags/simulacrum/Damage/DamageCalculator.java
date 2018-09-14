package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.DamageBonusMapper;
import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Entity.Enemy;

import java.util.List;

public class DamageCalculator {

    private static final double ARMOR_CONSTANT = 300;
    private static final double HEADCRIT_MULTIPLIER = 2.0;
    private DamageBonusMapper damageBonusMapper = new DamageBonusMapper();

    /*
        1.) Weapon *fires*
        2.) Calculate damage from instant damage source (based on enemy health types, current armor, current shields
            a.) Enemy with shield remaining takes no armor reduction to that damage
            b.) Damage types have bonuses vs shields

     */

    public double calculateDamage(Damage damage, Health baseHealth, Health targetShield, Health targetArmor, boolean isCorpus, double weaponCriticalDamageMultiplier, int critLevel, double headshotMultiplier) {
//        List<Health> targetHealths = target.getHealth(); //TODO: This should probably be the responsibility of the caller.
//        Health baseHealth = findBaseHealth(targetHealths);
//        Health targetShield = findShields(targetHealths);//TODO: This should probably be the responsibility of the caller.
//        Health targetArmor = findArmor(targetHealths);//TODO: This should probably be the responsibility of the caller.
        double targetShieldValue = targetShield != null ? targetShield.getValue() : 0.0;

        DamageType damageType = damage.getType();
        double baseDamage = damage.getDamageValue();
        double damageModifier;
        boolean damagingShields = damagingShields(targetShield, targetShieldValue, damageType);

        double shieldMultiplier = damagingShields ? 1 + damageBonusMapper.getBonus(targetShield.getHealthClass(), damageType) : 1.0;
        double healthMultiplier = !damagingShields /*damagingHealth(baseHealth, targetShieldValue, damageType)*/ ? 1 + damageBonusMapper.getBonus(baseHealth.getHealthClass(), damageType) : 1.0; //TODO: replace with !damagingShields?
        double headCritModifier = calculateHeadshotAndCriticalModifier(critLevel, headshotMultiplier, isCorpus, weaponCriticalDamageMultiplier, damagingShields);

        double allModifiers = headCritModifier * shieldMultiplier * healthMultiplier;
        double armorAmount = targetHasArmor(targetArmor) ? targetArmor.getValue() : 0.0;
        double armorMultiplier = targetHasArmor(targetArmor) ? damageBonusMapper.getBonus(targetArmor.getHealthClass(), damageType) : 0.0;
        double armorReduction = 1 + ((armorAmount * (1 - armorMultiplier)) / ARMOR_CONSTANT);

        damageModifier = allModifiers / armorReduction;

        return baseDamage * damageModifier;
    }


    private double calculateHeadshotAndCriticalModifier(int critLevel, double headshotMultiplier, boolean isCorpusNoHeadshots, double weaponCriticalDamageMultiplier, boolean damagingShields) {
        if (isHeadshot(headshotMultiplier, isCorpusNoHeadshots, damagingShields) && !isCritical(critLevel)) { //TODO: Check that shields do block headshots
            return headshotMultiplier;
        }
        if (isCritical(critLevel)) {
            double critModifier = (critLevel * (weaponCriticalDamageMultiplier - 1)) + 1;
            return isHeadshot(headshotMultiplier, isCorpusNoHeadshots, damagingShields) ? headshotMultiplier * HEADCRIT_MULTIPLIER * critModifier : critModifier;
        }
        return 1.0;
    }

    private boolean isCritical(int critLevel) {
        return critLevel != 0;
    }

    private boolean isHeadshot(double headshotMultiplier, boolean isCorpusNoHeadshots, boolean damagingShields) {
        return headshotMultiplier != 0 && !isCorpusNoHeadshots && !damagingShields;
    }

//    private boolean damagingHealth(Health baseHealth, double targetShieldValue, DamageType damageType) {
//        return baseHealth != null && (targetShieldValue <= 0 || damageType.equals(DamageType.GAS)); //TODO: can health ever be null? There's always one health clas, right?
//    }

    private boolean damagingShields(Health targetShield, double targetShieldValue, DamageType damageType) {
        return targetShield != null && targetShieldValue > 0 && !damageType.equals(DamageType.GAS);
    }

    private boolean targetHasArmor(Health targetArmor) {
        return targetArmor != null;
    }

    private boolean isTargetCorpus(Enemy.Faction faction) {
        return faction.equals(Enemy.Faction.CORPUS);
    }

    private Health findArmor(List<Health> health) {
        return health.stream().filter(h -> HealthClass.isArmor(h.getHealthClass())).findFirst().orElse(null);
    }

    private Health findShields(List<Health> health) {
        return health.stream().filter(hc -> HealthClass.isShield(hc.getHealthClass())).findFirst().orElse(null);
    }

    private Health findBaseHealth(List<Health> health) {
        return health.stream().filter(h -> !HealthClass.isArmor(h.getHealthClass()) && !HealthClass.isShield(h.getHealthClass())).findFirst().orElse(null);
    }
}
