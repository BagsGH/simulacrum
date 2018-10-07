package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Damage.DelayedDamageSource;
import com.bags.simulacrum.Status.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class FiredWeaponSummary {

    private Map<String, List<HitProperties>> hitPropertiesListMap;
    private List<HitProperties> hitPropertiesList;

    private Map<String, DamageMetrics> damageMetricsMap;
    private DamageMetrics damageMetrics;
    private List<DelayedDamageSource> delayedDamageSources;
    private Map<String, List<Status>> statusesAppliedMap;
    private List<Status> statusesApplied;

    public FiredWeaponSummary() {

    }

    public FiredWeaponSummary(List<HitProperties> hitPropertiesList, DamageMetrics damageMetrics, List<Status> statusesApplied, List<DelayedDamageSource> delayedDamageSources) {
        this.hitPropertiesList = hitPropertiesList;
        this.damageMetrics = damageMetrics;
        this.delayedDamageSources = delayedDamageSources;
        this.statusesApplied = statusesApplied;
    }

    public FiredWeaponSummary getEmptySummary() {
        return new FiredWeaponSummary(new ArrayList<>(), new DamageMetrics.DamageMetricsBuilder().withDamageToShields().withDamageToHealth().withStatusDamageToHealth().withStatusDamageToShields().build(), new ArrayList<>(), new ArrayList<>());
    }

    public void addHitPropertiesList(List<HitProperties> hitPropertiesList) {
        this.hitPropertiesList.addAll(hitPropertiesList);
    }

    public void addHitPropertiesList(Map<String, List<HitProperties>> hitPropertiesListMap) {
        hitPropertiesListMap.keySet().forEach(targetName -> {
            List<HitProperties> newHitProperties = hitPropertiesListMap.get(targetName);
            if (this.hitPropertiesListMap.containsKey(targetName)) {
                this.hitPropertiesListMap.get(targetName).addAll(newHitProperties);
            } else {
                this.hitPropertiesListMap.put(targetName, newHitProperties);
            }
        });
    }

    public void addStatusesApplied(Map<String, List<Status>> statusesAppliedMap) {
        statusesAppliedMap.keySet().forEach(targetName -> {
            List<Status> newAppliedStatuses = statusesAppliedMap.get(targetName);
            if (this.statusesAppliedMap.containsKey(targetName)) {
                this.statusesAppliedMap.get(targetName).addAll(newAppliedStatuses);
            } else {
                this.statusesAppliedMap.put(targetName, newAppliedStatuses);
            }
        });
    }

    public void addStatusesApplied(List<Status> statusesApplied) {
        this.statusesApplied.addAll(statusesApplied);
    }

    public void addStatusesApplied(String targetName, List<Status> statusesApplied) {
        if (this.statusesAppliedMap.containsKey(targetName)) {
            this.statusesAppliedMap.get(targetName).addAll(statusesApplied);
        } else {
            this.statusesAppliedMap.put(targetName, statusesApplied);
        }
    }

    public void addDamageToShields(Map<DamageType, Double> damageToShields) {
        for (DamageType damageType : damageToShields.keySet()) {
            double addendValue = damageToShields.get(damageType);
            double currentValue = this.damageMetrics.getDamageToShields().get(damageType);
            this.damageMetrics.getDamageToShields().put(damageType, currentValue + addendValue);
        }
    }

    public void addDamageToHealth(Map<DamageType, Double> damageToHealth) {
        for (DamageType damageType : damageToHealth.keySet()) {
            double addendValue = damageToHealth.get(damageType);
            double currentValue = this.damageMetrics.getDamageToHealth().get(damageType);
            this.damageMetrics.getDamageToHealth().put(damageType, currentValue + addendValue);
        }
    }

    public void addDamageToShields(String targetName, Map<DamageType, Double> damageToShields) {
        DamageMetrics damageMetricsForTarget = this.damageMetricsMap.get(targetName);
        for (DamageType damageType : damageToShields.keySet()) {
            double addendValue = damageToShields.get(damageType);
            double currentValue = damageMetricsForTarget.getDamageToShields().get(damageType);
            damageMetricsForTarget.getDamageToShields().put(damageType, currentValue + addendValue);
        }
    }

    public void addDamageToHealth(String targetName, Map<DamageType, Double> damageToHealth) {
        DamageMetrics damageMetricsForTarget = this.damageMetricsMap.get(targetName);
        for (DamageType damageType : damageToHealth.keySet()) {
            double addendValue = damageToHealth.get(damageType);
            double currentValue = damageMetricsForTarget.getDamageToHealth().get(damageType);
            damageMetricsForTarget.getDamageToHealth().put(damageType, currentValue + addendValue);
        }
    }

    public void addStatusDamageToShields(Map<DamageType, Double> statusDamageToShields) {
        for (DamageType damageType : statusDamageToShields.keySet()) {
            double addendValue = statusDamageToShields.get(damageType);
            double currentValue = this.damageMetrics.getStatusDamageToShields().get(damageType);
            this.damageMetrics.getStatusDamageToShields().put(damageType, currentValue + addendValue);
        }
    }

    public void addStatusDamageToHealth(Map<DamageType, Double> statusDamageToHealth) {
        for (DamageType damageType : statusDamageToHealth.keySet()) {
            double addendValue = statusDamageToHealth.get(damageType);
            double currentValue = this.damageMetrics.getStatusDamageToHealth().get(damageType);
            this.damageMetrics.getStatusDamageToHealth().put(damageType, currentValue + addendValue);
        }
    }

    public void addStatusDamageToHealth(String targetName, Map<DamageType, Double> statusDamageToHealth) {
        DamageMetrics damageMetricsForTarget = this.damageMetricsMap.get(targetName);
        for (DamageType damageType : statusDamageToHealth.keySet()) {
            double addendValue = statusDamageToHealth.get(damageType);
            double currentValue = damageMetricsForTarget.getStatusDamageToHealth().get(damageType);
            damageMetricsForTarget.getStatusDamageToHealth().put(damageType, currentValue + addendValue);
        }
    }

    public void addStatusDamageToShields(String targetName, Map<DamageType, Double> statusDamageToShields) {
        DamageMetrics damageMetricsForTarget = this.damageMetricsMap.get(targetName);
        for (DamageType damageType : statusDamageToShields.keySet()) {
            double addendValue = statusDamageToShields.get(damageType);
            double currentValue = damageMetricsForTarget.getStatusDamageToShields().get(damageType);
            damageMetricsForTarget.getStatusDamageToShields().put(damageType, currentValue + addendValue);
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

    public void addDamageMetrics(DamageMetrics damageMetrics) {
        Map<DamageType, Double> damageToShields = damageMetrics.getDamageToShields();
        for (DamageType damageType : damageToShields.keySet()) {
            double addendValue = damageToShields.get(damageType);
            double currentValue = this.damageMetrics.getDamageToShields().get(damageType);
            this.damageMetrics.getDamageToShields().put(damageType, currentValue + addendValue);
        }

        Map<DamageType, Double> damageToHealth = damageMetrics.getDamageToHealth();
        for (DamageType damageType : damageToHealth.keySet()) {
            double addendValue = damageToHealth.get(damageType);
            double currentValue = this.damageMetrics.getDamageToHealth().get(damageType);
            this.damageMetrics.getDamageToHealth().put(damageType, currentValue + addendValue);
        }

        Map<DamageType, Double> statusDamageToShields = damageMetrics.getStatusDamageToShields();
        for (DamageType damageType : statusDamageToShields.keySet()) {
            double addendValue = statusDamageToShields.get(damageType);
            double currentValue = this.damageMetrics.getStatusDamageToShields().get(damageType);
            this.damageMetrics.getStatusDamageToShields().put(damageType, currentValue + addendValue);
        }

        Map<DamageType, Double> statusDamageToHealth = damageMetrics.getStatusDamageToHealth();
        for (DamageType damageType : statusDamageToHealth.keySet()) {
            double addendValue = statusDamageToHealth.get(damageType);
            double currentValue = this.damageMetrics.getStatusDamageToHealth().get(damageType);
            this.damageMetrics.getStatusDamageToHealth().put(damageType, currentValue + addendValue);
        }
    }
}
