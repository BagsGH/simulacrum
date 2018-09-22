package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.DamageBonusMapper;
import com.bags.simulacrum.Armor.Health;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DamageCalculator {

    private static final double ARMOR_CONSTANT = 300;
    private static final double HEADCRIT_MULTIPLIER = 2.0;

    private final DamageBonusMapper damageBonusMapper;

    @Autowired
    public DamageCalculator(DamageBonusMapper damageBonusMapper) {
        this.damageBonusMapper = damageBonusMapper;
    }

    public double calculateDamage(Health baseHealth, Health targetShield, Health targetArmor, Damage damage, HitProperties hitProperties) {
        DamageType damageType = damage.getType();
        boolean damagingShields = damagingShields(targetShield, targetShield.getHealthValue(), damageType);

        double shieldMultiplier = damagingShields ? 1 + damageBonusMapper.getBonus(damageType, targetShield.getHealthClass()) : 1.0;
        double healthMultiplier = !damagingShields ? 1 + damageBonusMapper.getBonus(damageType, baseHealth.getHealthClass()) : 1.0;
        double headCritMultiplier = calculateHeadshotAndCriticalMultiplier(hitProperties.getCritLevel(), hitProperties.getHeadshotModifier(), hitProperties.getCriticalDamageMultiplier());

        double damageBonusMultiplier = headCritMultiplier * shieldMultiplier * healthMultiplier * (1 + hitProperties.getBodyPartModifier());
        double armorReduction = !damagingShields ? 1 + ((getArmorAmount(targetArmor) * (1 - getArmorDamageMultiplier(targetArmor, damageType))) / ARMOR_CONSTANT) : 1.0;
        double finalDamageMultiplier = damageBonusMultiplier / armorReduction;

        return Math.round(damage.getDamageValue() * finalDamageMultiplier);
    }

    private double getArmorAmount(Health targetArmor) {
        return targetArmor != null ? targetArmor.getHealthValue() : 0.0;
    }

    private double getArmorDamageMultiplier(Health targetArmor, DamageType damageType) {
        return targetArmor != null ? damageBonusMapper.getBonus(damageType, targetArmor.getHealthClass()) : 0.0;
    }

    private double calculateHeadshotAndCriticalMultiplier(int critLevel, double headshotModifier, double weaponCriticalDamageMultiplier) {
        if (isHeadshot(headshotModifier) && !isCritical(critLevel)) {
            return (1 + headshotModifier);
        }
        if (isCritical(critLevel)) {
            double critModifier = (critLevel * (weaponCriticalDamageMultiplier - 1)) + 1;
            return isHeadshot(headshotModifier) ? (1 + headshotModifier) * HEADCRIT_MULTIPLIER * critModifier : critModifier;
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

}
