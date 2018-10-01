package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Damage.DelayedDamageSource;
import com.bags.simulacrum.Status.Status;
import lombok.Data;

import java.util.List;

@Data
public class FiredWeaponMetrics {

    private List<HitProperties> hitPropertiesList;
    private DamageMetrics damageMetrics;
    private List<DelayedDamageSource> delayedDamageSources;
    private List<Status> statuses;


    public FiredWeaponMetrics(List<HitProperties> hitPropertiesList, DamageMetrics damageMetrics, List<Status> statuses, List<DelayedDamageSource> delayedDamageSources) {
        this.hitPropertiesList = hitPropertiesList;
        this.damageMetrics = damageMetrics;
        this.delayedDamageSources = delayedDamageSources;
        this.statuses = statuses;
    }
}
