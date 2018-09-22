package com.bags.simulacrum.Damage;

import lombok.Data;

import java.util.List;

@Data
public class FiredWeaponSummary {

    private List<HitProperties> hitPropertiesList;
    private DamageSummary damageSummary;
    private List<DelayedDamageSource> delayedDamageSources;


    public FiredWeaponSummary(List<HitProperties> hitPropertiesList, DamageSummary damageSummary, List<DelayedDamageSource> delayedDamageSources) {
        this.hitPropertiesList = hitPropertiesList;
        this.damageSummary = damageSummary;
        this.delayedDamageSources = delayedDamageSources;
    }
}
