package com.bags.simulacrum.Damage;

import lombok.Data;

import java.util.List;

@Data
public class FiredWeaponMetrics {

    private List<HitProperties> hitPropertiesList;
    private DamageMetrics damageMetrics;
    private List<DelayedDamageSource> delayedDamageSources;


    public FiredWeaponMetrics(List<HitProperties> hitPropertiesList, DamageMetrics damageMetrics, List<DelayedDamageSource> delayedDamageSources) {
        this.hitPropertiesList = hitPropertiesList;
        this.damageMetrics = damageMetrics;
        this.delayedDamageSources = delayedDamageSources;
    }
}
