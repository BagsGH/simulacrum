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

    private List<HitProperties> hitPropertiesList;
    private DamageMetrics damageMetrics;
    private List<DelayedDamageSource> delayedDamageSources;
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

    public void addStatusesApplied(List<Status> statusesApplied) {
        this.statusesApplied.addAll(statusesApplied);
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
            double currentValue = this.damageMetrics.getDamageToShields().get(damageType);
            this.damageMetrics.getDamageToShields().put(damageType, currentValue + addendValue);
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
