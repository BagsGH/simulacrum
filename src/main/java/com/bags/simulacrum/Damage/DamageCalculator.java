package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.DamageBonusMapper;
import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Configuration.DamageConfig;
import com.bags.simulacrum.Simulation.HitProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.bags.simulacrum.Damage.DamageType.GAS;

@Component
public class DamageCalculator {

    private final DamageBonusMapper damageBonusMapper;

    private final DamageConfig config;

    @Autowired
    public DamageCalculator(DamageBonusMapper damageBonusMapper, DamageConfig config) {
        this.damageBonusMapper = damageBonusMapper;
        this.config = config;
    }

    @PostConstruct
    public void test() {
        System.out.println("config.getHeadcritMultiplier(): " + config.getHeadcritMultiplier());
        System.out.println("config.getArmorConstant(): " + config.getArmorConstant());
    }

    public double calculateDamage(Health baseHealth, Health targetShield, Health targetArmor, Damage damage, HitProperties hitProperties) {
        DamageType damageType = damage.getType();
        boolean damagingShields = damagingShields(targetShield, targetShield.getHealthValue(), damageType);

        double shieldMultiplier = damagingShields ? 1 + damageBonusMapper.getBonusModifier(damageType, targetShield.getHealthClass()) : 1.0;
        double healthMultiplier = !damagingShields ? 1 + damageBonusMapper.getBonusModifier(damageType, baseHealth.getHealthClass()) : 1.0;
        double headCritMultiplier = calculateHeadshotAndCriticalMultiplier(hitProperties.getCritLevel(), hitProperties.getHeadshotModifier(), hitProperties.getCriticalDamageMultiplier());

        double damageBonusMultiplier = headCritMultiplier * shieldMultiplier * healthMultiplier * (1 + hitProperties.getBodyPartModifier());
        double armorReduction = !damagingShields ? 1 + ((getArmorAmount(targetArmor) * (1 - getArmorDamageMultiplier(targetArmor, damageType))) / config.getArmorConstant()) : 1.0;
        double finalDamageMultiplier = damageBonusMultiplier / armorReduction;

        return Math.round(damage.getDamageValue() * finalDamageMultiplier);
    }

    private double getArmorAmount(Health targetArmor) {
        return targetArmor != null ? targetArmor.getHealthValue() : 0.0;
    }

    private double getArmorDamageMultiplier(Health targetArmor, DamageType damageType) {
        return targetArmor != null ? damageBonusMapper.getBonusModifier(damageType, targetArmor.getHealthClass()) : 0.0;
    }

    private double calculateHeadshotAndCriticalMultiplier(int critLevel, double headshotModifier, double weaponCriticalDamageMultiplier) {
        if (isHeadshot(headshotModifier) && !isCritical(critLevel)) {
            return (1 + headshotModifier);
        }
        if (isCritical(critLevel)) {
            double critModifier = (critLevel * (weaponCriticalDamageMultiplier - 1)) + 1;
            return isHeadshot(headshotModifier) ? (1 + headshotModifier) * config.getHeadcritMultiplier() * critModifier : critModifier;
        }
        return 1.0;
    }

    private boolean isCritical(int critLevel) {
        return critLevel != 0;
    }

    private boolean isHeadshot(double headshotMultiplier) {
        return headshotMultiplier != 0.0;
    }

    private boolean damagingShields(Health targetShield, double targetShieldValue, DamageType damageType) {
        return targetShield != null && targetShieldValue > 0 && !damageType.equals(GAS);
    }

}
