package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.DamageBonusMapper;
import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Entity.Enemy;
import com.bags.simulacrum.Weapon.Weapon;

import java.util.ArrayList;
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

    public List<DamageInflicted> calculateDamageDone(DamageSource damageSource, Enemy target, boolean shotCrit, Weapon weapon, int critLevel, boolean headshot) {
        List<DamageInflicted> damageInflicted = new ArrayList<>();
        List<Health> targetHealths = target.getHealth(); //TODO: This should probably be the responsibility of the caller.
        Health baseHealth = findBaseHealth(targetHealths);
        Health targetShield = findShields(targetHealths);//TODO: This should probably be the responsibility of the caller.
        Health targetArmor = findArmor(targetHealths);//TODO: This should probably be the responsibility of the caller.
        double targetShieldValue = targetShield != null ? targetShield.getValue() : 1.0;

        boolean isCorpusNoHeadshots = isTargetCorpus(target);

        for (Damage damage : damageSource.getDamages()) {
            DamageType damageType = damage.getType();
            double baseDamage = damage.getDamageValue();
            double damageModifier = 1.0;
            double shieldMultiplier = damagingShields(targetShield, targetShieldValue) ? damageBonusMapper.getBonus(targetShield.getHealthClass(), damageType) : 0.0;
            double healthMultiplier = damagingHealth(baseHealth, targetShieldValue) ? damageBonusMapper.getBonus(baseHealth.getHealthClass(), damageType) : 0.0;
            double headCritModifier = calculateHeadshotAndCriticalModifier(shotCrit, critLevel, headshot, isCorpusNoHeadshots, target, weapon);

            double allModifiers = headCritModifier * (1 + shieldMultiplier) * (1 + healthMultiplier);
            double armorAmount = targetHasArmor(targetArmor) ? targetArmor.getValue() : 0.0;
            double armorMultiplier = targetHasArmor(targetArmor) ? damageBonusMapper.getBonus(targetArmor.getHealthClass(), damageType) : 0.0;
            double armorReduction = 1 + ((armorAmount * (1 - armorMultiplier)) / 300);

            if (!targetHasArmor(targetArmor)) { //TODO : probably unecessary
                armorReduction = 1.0;
            }
            damageModifier = allModifiers / armorReduction;
            double damageDone = baseDamage * damageModifier;
        }

        return damageInflicted;
    }


    private double calculateHeadshotAndCriticalModifier(boolean shotCrit, int critLevel, boolean headshot, boolean isCorpusNoHeadshots, Enemy target, Weapon weapon) {
        if (headshot && !isCorpusNoHeadshots && !shotCrit) {
            return target.getHeadshotMultiplier();
        }
        if (shotCrit) {
            double critModifier = (critLevel * (weapon.getCriticalDamage() - 1)) + 1;
            if (headshot && !isCorpusNoHeadshots) {
                return critModifier * target.getHeadshotMultiplier() * HEADCRIT_MULTIPLIER;
            }
            return critModifier;
        }
        return 0.0;
    }

    private boolean damagingHealth(Health baseHealth, double targetShieldValue) {
        return baseHealth != null && targetShieldValue <= 0;
    }

    private boolean damagingShields(Health targetShield, double targetShieldValue) {
        return targetShield != null && targetShieldValue > 0;
    }

    private boolean targetHasArmor(Health targetArmor) {
        return targetArmor != null;
    }

    private boolean isTargetCorpus(Enemy target) {
        return target.getFaction().equals(Enemy.Faction.CORPUS);
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
