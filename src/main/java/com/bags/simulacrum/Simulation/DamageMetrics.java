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


    public DamageMetrics(Map<DamageType, Double> damageToHealth, Map<DamageType, Double> damageToShields) {
        this.damageToShields = damageToShields;
        this.damageToHealth = damageToHealth;
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

    private DamageMetrics(DamageMetricsBuilder builder) {
        if (builder.withDamageToHealth) {
            this.damageToHealth = initialDamageMap();
        }
        if (builder.withDamageToShields) {
            this.damageToShields = initialDamageMap();
        }
        if (builder.withStatusDamageToHealth) {
            this.statusDamageToHealth = initialDamageMap();
        }
        if (builder.withStatusDamageToShields) {
            this.statusDamageToShields = initialDamageMap();
        }
    }

    public static class DamageMetricsBuilder {


        private boolean withDamageToHealth;
        private boolean withDamageToShields;
        private boolean withStatusDamageToHealth;
        private boolean withStatusDamageToShields;

        public DamageMetricsBuilder() {
        }

        public DamageMetricsBuilder withDamageToHealth() {
            this.withDamageToHealth = true;
            return this;
        }

        public DamageMetricsBuilder withDamageToShields() {
            this.withDamageToShields = true;
            return this;
        }

        public DamageMetricsBuilder withStatusDamageToHealth() {
            this.withStatusDamageToHealth = true;
            return this;
        }

        public DamageMetricsBuilder withStatusDamageToShields() {
            this.withStatusDamageToShields = true;
            return this;
        }

        public DamageMetrics build() {
            return new DamageMetrics(this);
        }
    }

}
