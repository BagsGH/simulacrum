package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Damage.DelayedDamageSource;
import com.bags.simulacrum.Status.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FiredWeaponSummary {

    private Map<String, List<HitProperties>> hitPropertiesListMap;
    private Map<String, DamageMetrics> damageMetricsMap;
    private Map<String, List<Status>> statusesAppliedMap;
    private List<DelayedDamageSource> delayedDamageSources;

    public FiredWeaponSummary() {
        this.initializeDefaults();
    }

    public FiredWeaponSummary(List<DelayedDamageSource> delayedDamageSources) {
        this.initializeDefaults();
        this.delayedDamageSources = delayedDamageSources;
    }

    private void initializeDefaults() {
        this.delayedDamageSources = new ArrayList<>();
        this.damageMetricsMap = new HashMap<>();
        this.statusesAppliedMap = new HashMap<>();
        this.hitPropertiesListMap = new HashMap<>();
    }

    public void addHitProperties(String targetName, HitProperties hitProperties) {
        if (this.hitPropertiesListMap.containsKey(targetName)) {
            this.hitPropertiesListMap.get(targetName).add(hitProperties);
        } else {
            List<HitProperties> list = new ArrayList<>();
            list.add(hitProperties);
            this.hitPropertiesListMap.put(targetName, list);
        }
    }

    public void addHitPropertiesMap(Map<String, List<HitProperties>> hitPropertiesListMap) {
        hitPropertiesListMap.keySet().forEach(targetName -> {
            List<HitProperties> newHitProperties = hitPropertiesListMap.get(targetName);
            if (this.hitPropertiesListMap.containsKey(targetName)) {
                this.hitPropertiesListMap.get(targetName).addAll(newHitProperties);
            } else {
                this.hitPropertiesListMap.put(targetName, newHitProperties);
            }
        });
    }

    public void addStatusApplied(String targetName, Status statusApplied) {
        if (this.statusesAppliedMap.containsKey(targetName)) {
            this.statusesAppliedMap.get(targetName).add(statusApplied);
        } else {
            List<Status> list = new ArrayList<>();
            list.add(statusApplied);
            this.statusesAppliedMap.put(targetName, list);
        }
    }

    public void addStatusesAppliedMap(Map<String, List<Status>> statusesAppliedMap) {
        statusesAppliedMap.keySet().forEach(targetName -> {
            List<Status> newAppliedStatuses = statusesAppliedMap.get(targetName);
            if (this.statusesAppliedMap.containsKey(targetName)) {
                this.statusesAppliedMap.get(targetName).addAll(newAppliedStatuses);
            } else {
                this.statusesAppliedMap.put(targetName, newAppliedStatuses);
            }
        });
    }

    public void addDamageToShields(String targetName, Map<DamageType, Double> damageToShields) {
        if (this.damageMetricsMap.containsKey(targetName)) {
            DamageMetrics damageMetricsForTarget = this.damageMetricsMap.get(targetName);
            for (DamageType damageType : damageToShields.keySet()) {
                double addendValue = damageToShields.get(damageType);
                double currentValue = damageMetricsForTarget.getDamageToShields().get(damageType);
                damageMetricsForTarget.getDamageToShields().put(damageType, currentValue + addendValue);
            }
        } else {
            DamageMetrics damageMetrics = new DamageMetrics();
            for (DamageType damageType : damageToShields.keySet()) {
                damageMetrics.getDamageToShields().put(damageType, damageToShields.get(damageType));
            }
            this.damageMetricsMap.put(targetName, damageMetrics);
        }
    }

    public void addDamageToHealth(String targetName, Map<DamageType, Double> damageToHealth) {
        if (this.damageMetricsMap.containsKey(targetName)) {
            DamageMetrics damageMetricsForTarget = this.damageMetricsMap.get(targetName);
            for (DamageType damageType : damageToHealth.keySet()) {
                double addendValue = damageToHealth.get(damageType);
                double currentValue = damageMetricsForTarget.getDamageToHealth().get(damageType);
                damageMetricsForTarget.getDamageToHealth().put(damageType, currentValue + addendValue);
            }
        } else {
            DamageMetrics damageMetrics = new DamageMetrics();
            for (DamageType damageType : damageToHealth.keySet()) {
                damageMetrics.getDamageToHealth().put(damageType, damageToHealth.get(damageType));
            }
            this.damageMetricsMap.put(targetName, damageMetrics);
        }
    }

    public void addStatusDamageToHealth(String targetName, Map<DamageType, Double> statusDamageToHealth) {
        if (this.damageMetricsMap.containsKey(targetName)) {
            DamageMetrics damageMetricsForTarget = this.damageMetricsMap.get(targetName);
            for (DamageType damageType : statusDamageToHealth.keySet()) {
                double addendValue = statusDamageToHealth.get(damageType);
                double currentValue = damageMetricsForTarget.getStatusDamageToHealth().get(damageType);
                damageMetricsForTarget.getStatusDamageToHealth().put(damageType, currentValue + addendValue);
            }
        } else {
            DamageMetrics damageMetrics = new DamageMetrics();
            for (DamageType damageType : statusDamageToHealth.keySet()) {
                damageMetrics.getStatusDamageToHealth().put(damageType, statusDamageToHealth.get(damageType));
            }
            this.damageMetricsMap.put(targetName, damageMetrics);
        }
    }

    public void addStatusDamageToShields(String targetName, Map<DamageType, Double> statusDamageToShields) {
        if (this.damageMetricsMap.containsKey(targetName)) {
            DamageMetrics damageMetricsForTarget = this.damageMetricsMap.get(targetName);
            for (DamageType damageType : statusDamageToShields.keySet()) {
                double addendValue = statusDamageToShields.get(damageType);
                double currentValue = damageMetricsForTarget.getStatusDamageToShields().get(damageType);
                damageMetricsForTarget.getStatusDamageToShields().put(damageType, currentValue + addendValue);
            }
        } else {
            DamageMetrics damageMetrics = new DamageMetrics();
            for (DamageType damageType : statusDamageToShields.keySet()) {
                damageMetrics.getStatusDamageToShields().put(damageType, statusDamageToShields.get(damageType));
            }
            this.damageMetricsMap.put(targetName, damageMetrics);
        }
    }

    public void addDamageMetrics(Map<String, DamageMetrics> damageMetricsMap) {
        for (String targetName : damageMetricsMap.keySet()) {
            DamageMetrics newDamageMetricsForTarget = damageMetricsMap.get(targetName);
            if (this.damageMetricsMap.containsKey(targetName)) {
                DamageMetrics currentMetricsForTarget = this.damageMetricsMap.get(targetName);

                Map<DamageType, Double> newDamageToShieldsForTarget = newDamageMetricsForTarget.getDamageToShields();
                for (DamageType damageType : newDamageToShieldsForTarget.keySet()) {
                    double addendValue = newDamageToShieldsForTarget.get(damageType);
                    double currentValue = currentMetricsForTarget.getDamageToShields().get(damageType);
                    currentMetricsForTarget.getDamageToShields().put(damageType, currentValue + addendValue);
                }

                Map<DamageType, Double> newDamageToHealthForTarget = newDamageMetricsForTarget.getDamageToHealth();
                for (DamageType damageType : newDamageToHealthForTarget.keySet()) {
                    double addendValue = newDamageToHealthForTarget.get(damageType);
                    double currentValue = currentMetricsForTarget.getDamageToHealth().get(damageType);
                    currentMetricsForTarget.getDamageToHealth().put(damageType, currentValue + addendValue);
                }

                Map<DamageType, Double> newStatusDamageToShieldsForTarget = newDamageMetricsForTarget.getStatusDamageToShields();
                for (DamageType damageType : newStatusDamageToShieldsForTarget.keySet()) {
                    double addendValue = newStatusDamageToShieldsForTarget.get(damageType);
                    double currentValue = currentMetricsForTarget.getStatusDamageToShields().get(damageType);
                    currentMetricsForTarget.getStatusDamageToShields().put(damageType, currentValue + addendValue);
                }

                Map<DamageType, Double> newStatusDamageToHealthForTarget = newDamageMetricsForTarget.getStatusDamageToHealth();
                for (DamageType damageType : newStatusDamageToHealthForTarget.keySet()) {
                    double addendValue = newStatusDamageToHealthForTarget.get(damageType);
                    double currentValue = currentMetricsForTarget.getStatusDamageToHealth().get(damageType);
                    currentMetricsForTarget.getStatusDamageToHealth().put(damageType, currentValue + addendValue);
                }
            } else {
                this.damageMetricsMap.put(targetName, newDamageMetricsForTarget);
            }
        }
    }
}
