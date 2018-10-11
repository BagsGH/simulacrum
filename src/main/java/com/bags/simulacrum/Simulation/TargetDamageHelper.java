package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageCalculator;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TargetDamageHelper {

    private final DamageCalculator damageCalculator;

    @Autowired
    public TargetDamageHelper(DamageCalculator damageCalculator) {
        this.damageCalculator = damageCalculator;
    }

    public DamageMetrics applyDamageSourceDamageToTarget(DamageSource damageSource, HitProperties hitProperties, Target target) {
        DamageMetrics damageMetrics = new DamageMetrics();

        List<Health> targetHealthClasses = target.getHealths();
        Health targetHealth = findBaseHealth(targetHealthClasses);
        Health targetShield = findShields(targetHealthClasses);
        Health targetArmor = findArmor(targetHealthClasses);

        for (Damage damage : damageSource.getDamages()) {
            DamageType damageType = damage.getDamageType();
            double damageDealt = damageCalculator.calculateDamage(targetHealth, targetShield, targetArmor, damage, hitProperties);

            if (targetHasNoShields(targetShield)) {
                targetHealth.subtractHealthValue(damageDealt);
                damageMetrics.addDamageToHealth(damageType, damageDealt);
            } else if (damageLessThanTargetShields(targetShield, damageDealt)) {
                targetShield.subtractHealthValue(damageDealt);
                damageMetrics.addDamageToShields(damageType, damageDealt);
            } else if (damageMoreThanTargetShields(targetShield, damageDealt)) {
                damageMetrics.addDamageToShields(damageType, targetShield.getHealthValue());
                double spilloverHealthDamage = handleShieldDamageSpillover(hitProperties, targetHealth, targetShield, targetArmor, damage, damageDealt);
                damageMetrics.addDamageToHealth(damageType, spilloverHealthDamage);
            }
        }

        return damageMetrics;
    }

    private Health findBaseHealth(List<Health> health) {
        return health.stream().filter(h -> !HealthClass.isArmor(h.getHealthClass()) && !HealthClass.isShield(h.getHealthClass())).findFirst().orElse(null);
    }

    private Health findShields(List<Health> health) {
        return health.stream().filter(hc -> HealthClass.isShield(hc.getHealthClass())).findFirst().orElse(null);
    }

    private Health findArmor(List<Health> health) {
        return health.stream().filter(h -> HealthClass.isArmor(h.getHealthClass())).findFirst().orElse(null);
    }

    private boolean targetHasNoShields(Health targetShield) {
        return targetShield.getHealthValue() <= 0;
    }

    private boolean damageLessThanTargetShields(Health targetShield, double damageDealt) {
        return targetShield.getHealthValue() > damageDealt;
    }

    private boolean damageMoreThanTargetShields(Health targetShield, double damageDealt) {
        return targetShield.getHealthValue() > 0 && damageDealt > targetShield.getHealthValue();
    }

    private double handleShieldDamageSpillover(HitProperties hitProperties, Health targetHealth, Health targetShield, Health targetArmor, Damage damage, double damageDealt) {
        double shieldValue = targetShield.getHealthValue();
        targetShield.setHealthValue(0.0);
        double percentSpilloverDamage = 1 - (shieldValue / damageDealt);
        Damage remainingDamage = new Damage(damage.getDamageType(), Math.round(damage.getDamageValue() * percentSpilloverDamage));
        double damageDoneToHealth = damageCalculator.calculateDamage(targetHealth, targetShield, targetArmor, remainingDamage, hitProperties);
        targetHealth.subtractHealthValue(damageDoneToHealth);
        return damageDoneToHealth;
    }
}
