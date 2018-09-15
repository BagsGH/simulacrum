package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.DamageBonusMapper;
import com.bags.simulacrum.Armor.Health;

public class DamageCalculator {

    private static final double ARMOR_CONSTANT = 300;
    private static final double HEADCRIT_MULTIPLIER = 2.0;
    private DamageBonusMapper damageBonusMapper = new DamageBonusMapper();

    public double calculateDamage(Health baseHealth, Health targetShield, Health targetArmor, double targetHeadshotMultiplier, Damage damage, double weaponCriticalDamageMultiplier, int critLevel, double bodyPartModifier) {
        double targetShieldValue = targetShield != null ? targetShield.getValue() : 0.0;
        DamageType damageType = damage.getType();
        boolean damagingShields = damagingShields(targetShield, targetShieldValue, damageType);

        double shieldMultiplier = damagingShields ? 1 + damageBonusMapper.getBonus(targetShield.getHealthClass(), damageType) : 1.0;
        double healthMultiplier = !damagingShields /*damagingHealth(baseHealth, targetShieldValue, damageType)*/ ? 1 + damageBonusMapper.getBonus(baseHealth.getHealthClass(), damageType) : 1.0;
        double headCritModifier = calculateHeadshotAndCriticalModifier(critLevel, targetHeadshotMultiplier, weaponCriticalDamageMultiplier);

        double baseDamageModifiers = headCritModifier * shieldMultiplier * healthMultiplier * bodyPartModifier;
        double armorReduction = !damagingShields ? 1 + ((getArmorAmount(targetArmor) * (1 - getArmorDamageMultiplier(targetArmor, damageType))) / ARMOR_CONSTANT) : 1.0;
        double finalDamageModifier = baseDamageModifiers / armorReduction;

        return Math.round(damage.getDamageValue() * finalDamageModifier);
    }

    private double getArmorAmount(Health targetArmor) {
        return targetArmor != null ? targetArmor.getValue() : 0.0;
    }

    private double getArmorDamageMultiplier(Health targetArmor, DamageType damageType) {
        return targetArmor != null ? damageBonusMapper.getBonus(targetArmor.getHealthClass(), damageType) : 0.0;
    }

    private double calculateHeadshotAndCriticalModifier(int critLevel, double targetHeadshotMultiplier, double weaponCriticalDamageMultiplier) {
        if (isHeadshot(targetHeadshotMultiplier) && !isCritical(critLevel)) {
            return targetHeadshotMultiplier;
        }
        if (isCritical(critLevel)) {
            double critModifier = (critLevel * (weaponCriticalDamageMultiplier - 1)) + 1;
            return isHeadshot(targetHeadshotMultiplier) ? targetHeadshotMultiplier * HEADCRIT_MULTIPLIER * critModifier : critModifier;
        }
        return 1.0;
    }

    private boolean isCritical(int critLevel) {
        return critLevel != 0;
    }

    private boolean isHeadshot(double headshotMultiplier) {
        return headshotMultiplier != 0;
    }

    private boolean damagingShields(Health targetShield, double targetShieldValue, DamageType damageType) {
        return targetShield != null && targetShieldValue > 0 && !damageType.equals(DamageType.GAS);
    }

    //TODO: Caller can use these.
//    private boolean isTargetCorpus(Target.Faction faction) {
//        return faction.equals(Target.Faction.CORPUS);
//    }
//
//    private Health findArmor(List<Health> health) {
//        return health.stream().filter(h -> HealthClass.isArmor(h.getHealthClass())).findFirst().orElse(null);
//    }
//
//    private Health findShields(List<Health> health) {
//        return health.stream().filter(hc -> HealthClass.isShield(hc.getHealthClass())).findFirst().orElse(null);
//    }
//
//    private Health findBaseHealth(List<Health> health) {
//        return health.stream().filter(h -> !HealthClass.isArmor(h.getHealthClass()) && !HealthClass.isShield(h.getHealthClass())).findFirst().orElse(null);
//    }
}
