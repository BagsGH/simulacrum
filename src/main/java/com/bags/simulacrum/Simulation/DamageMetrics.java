package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Damage.DamageType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DamageMetrics {

    private Map<DamageType, Double> damageToShields;
    private Map<DamageType, Double> damageToHealth;

    private Map<DamageType, Double> statusDamageToShields;
    private Map<DamageType, Double> statusDamageToHealth;

    public DamageMetrics() {
        this.damageToHealth = initialDamageMap();
        this.damageToShields = initialDamageMap();
        this.statusDamageToHealth = initialDamageMap();
        this.statusDamageToShields = initialDamageMap();
    }

    public void addDamageToShields(DamageType damageType, double value) {
        double currentValueForType = damageToShields.get(damageType);
        damageToShields.put(damageType, currentValueForType + value);
    }

    public void addDamageToHealth(DamageType damageType, double value) {
        double currentValueForType = damageToHealth.get(damageType);
        damageToHealth.put(damageType, currentValueForType + value);
    }

    public void addStatusDamageToShields(DamageType damageType, double value) {
        double currentValueForType = statusDamageToShields.get(damageType);
        statusDamageToShields.put(damageType, currentValueForType + value);
    }

    public void addStatusDamageToHealth(DamageType damageType, double value) {
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
