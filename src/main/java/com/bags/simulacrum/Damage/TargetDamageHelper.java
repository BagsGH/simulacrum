package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
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

    public Target applyDamageSourceDamageToTarget(DamageSource damageSource, HitProperties hitProperties, Target target) {
        //TODO: calculate metrics to pass to the caller to track total damage, etc.
        List<Health> targetHealthClasses = target.getHealth();
        Health targetHealth = findBaseHealth(targetHealthClasses);
        Health targetShield = findShields(targetHealthClasses);
        Health targetArmor = findArmor(targetHealthClasses);

        for (Damage damage : damageSource.getDamages()) {
            double damageDealt = damageCalculator.calculateDamage(targetHealth, targetShield, targetArmor, damage, hitProperties); //TODO: make modifier params more consistent. Some are 1.0 for no bonus, some are 0.0.

            if (targetHasNoShields(targetShield)) {
                targetHealth.subtractHealthValue(damageDealt);
            } else if (damageLessThanTargetShields(targetShield, damageDealt)) {
                targetShield.subtractHealthValue(damageDealt);
            } else if (damageMoreThanTargetShields(targetShield, damageDealt)) {
                handleShieldDamageSpillover(hitProperties, targetHealth, targetShield, targetArmor, damage, damageDealt);
            }
        }

        return target;
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

    private void handleShieldDamageSpillover(HitProperties hitProperties, Health targetHealth, Health targetShield, Health targetArmor, Damage damage, double damageDealt) {
        double shieldValue = targetShield.getHealthValue();
        targetShield.setHealthValue(0.0);
        double percentSpilloverDamage = 1 - (shieldValue / damageDealt);
        Damage remainingDamage = new Damage(damage.getType(), Math.round(damage.getDamageValue() * percentSpilloverDamage));
        double damageDoneToHealth = damageCalculator.calculateDamage(targetHealth, targetShield, targetArmor, remainingDamage, hitProperties);
        targetHealth.subtractHealthValue(damageDoneToHealth);
    }
}
