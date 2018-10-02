package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DamageMetrics {

    private Target target;
    private Map<DamageType, Double> damageToShields;
    private Map<DamageType, Double> damageToHealth;

    private Map<DamageType, Double> statusDamageToShields;
    private Map<DamageType, Double> statusDamageToHealth;


    public DamageMetrics(Target target, Map<DamageType, Double> damageToHealth, Map<DamageType, Double> damageToShields) {
        this.target = target;
        this.damageToShields = damageToShields;
        this.damageToHealth = damageToHealth;
    }

    public void addToShields(DamageType damageType, double value) {
        double currentValueForType = damageToShields.get(damageType);
        damageToShields.put(damageType, currentValueForType + value);
    }

    public void addToHealth(DamageType damageType, double value) {
        double currentValueForType = damageToHealth.get(damageType);
        damageToHealth.put(damageType, currentValueForType + value);
    }

    public void addToStatusShields(DamageType damageType, double value) {
        double currentValueForType = statusDamageToShields.get(damageType);
        statusDamageToShields.put(damageType, currentValueForType + value);
    }

    public void addToStatusHealth(DamageType damageType, double value) {
        double currentValueForType = statusDamageToHealth.get(damageType);
        statusDamageToHealth.put(damageType, currentValueForType + value);
    }

    public static Map<DamageType, Double> initialDamageMap() {
        Map<DamageType, Double> damageMap = new HashMap<>();
        for (DamageType dt : DamageType.values()) {
            damageMap.put(dt, 0.0);
        }
        return damageMap;
    }
}
