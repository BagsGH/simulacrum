package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Entity.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Helper {

    private final DamageCalculator damageCalculator;

    @Autowired
    public Helper(DamageCalculator damageCalculator) {
        this.damageCalculator = damageCalculator;
    }

    //TODO: make this work on one Damage. or the damage source?
    public Target applyDamageSourceDamageToTarget(DamageSource damageSource, HitProperties hitProperties, Target target) { //TODO: add shot properties for headshot, body shot, etc
        //TODO: calculate metrics to pass to the caller to track total damage, etc.
        List<Health> targetHealthClasses = target.getHealth();
        Health targetHealth = findBaseHealth(targetHealthClasses);
        Health targetShield = findShields(targetHealthClasses);
        Health targetArmor = findArmor(targetHealthClasses);

        for (Damage damage : damageSource.getDamages()) {
            double damageDealt = damageCalculator.calculateDamage(targetHealth, targetShield, targetArmor, hitProperties.getHeadshotModifier(), damage, hitProperties.getWeaponCriticalDamageModifier(), hitProperties.getCritLevel(), hitProperties.getBodyPartModifier()); //TODO: make modifier params more consistent. Some are 1.0 for no bonus, some are 0.0.

            if (targetShield != null && targetShield.getHealthValue() > damageDealt) {
                targetShield.subtractHealthValue(damageDealt);
            } else if (targetShield != null && targetShield.getHealthValue() > 0 && damageDealt > targetShield.getHealthValue()) {
                double shieldValue = targetShield.getHealthValue();
                targetShield.setHealthValue(0.0);
                double percentDamageAppliedToShields = shieldValue / damageDealt;
                Damage remainingDamage = new Damage(damage.getType(), Math.round(damage.getDamageValue() * (1 - percentDamageAppliedToShields)));
                double healthDamageDealt = damageCalculator.calculateDamage(targetHealth, targetShield, targetArmor, hitProperties.getHeadshotModifier(), remainingDamage, hitProperties.getWeaponCriticalDamageModifier(), hitProperties.getCritLevel(), hitProperties.getBodyPartModifier());
                targetHealth.subtractHealthValue(healthDamageDealt);
            } else {
                targetHealth.subtractHealthValue(damageDealt);
            }
        }

        return target;
    }


    private boolean isTargetCorpus(Target.Faction faction) {
        return faction.equals(Target.Faction.CORPUS);
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
