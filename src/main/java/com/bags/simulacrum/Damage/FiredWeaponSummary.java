package com.bags.simulacrum.Damage;

import lombok.Data;

import java.util.List;

@Data
public class FiredWeaponSummary {

    private HitProperties hitProperties;
    private DamageSummary damageSummary;
    private List<DelayedDamageSource> delayedDamageSources;


    public FiredWeaponSummary(HitProperties hitProperties, DamageSummary damageSummary, List<DelayedDamageSource> delayedDamageSources) {
        this.hitProperties = hitProperties;
        this.damageSummary = damageSummary;
        this.delayedDamageSources = delayedDamageSources;
    }
}
