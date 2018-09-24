package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Damage.DelayedDamageSource;
import com.bags.simulacrum.Status.StatusProc;
import lombok.Data;

import java.util.List;

@Data
public class FiredWeaponMetrics {

    private List<HitProperties> hitPropertiesList;
    private DamageMetrics damageMetrics;
    private List<DelayedDamageSource> delayedDamageSources;
    private List<StatusProc> statusProcs;


    public FiredWeaponMetrics(List<HitProperties> hitPropertiesList, DamageMetrics damageMetrics, List<StatusProc> statusProcs, List<DelayedDamageSource> delayedDamageSources) {
        this.hitPropertiesList = hitPropertiesList;
        this.damageMetrics = damageMetrics;
        this.delayedDamageSources = delayedDamageSources;
        this.statusProcs = statusProcs;
    }
}
