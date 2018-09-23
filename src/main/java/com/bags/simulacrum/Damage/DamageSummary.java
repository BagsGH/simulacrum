package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DamageSummary {

    private Target target;
    private Map<DamageType, Double> damageToShields;
    private Map<DamageType, Double> damageToHealth;

    public DamageSummary(Target target, Map<DamageType, Double> damageToHealth, Map<DamageType, Double> damageToShields) {
        this.target = target;
        this.damageToShields = damageToShields;
        this.damageToHealth = damageToHealth;
    }

    public DamageSummary() {

    }

    public void addToShields(DamageType damageType, double value) {
        double currentValueForType = damageToShields.get(damageType);
        damageToShields.put(damageType, currentValueForType + value);
    }

    public void addToHealth(DamageType damageType, double value) {
        double currentValueForType = damageToHealth.get(damageType);
        damageToHealth.put(damageType, currentValueForType + value);
    }

    public static Map<DamageType, Double> initialDamageMap() {
        Map<DamageType, Double> damageMap = new HashMap<>();
        for (DamageType dt : DamageType.values()) {
            damageMap.put(dt, 0.0);
        }
        return damageMap;
    }
}
