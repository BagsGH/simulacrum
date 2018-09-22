package com.bags.simulacrum.Damage;

import lombok.Data;

@Data
public class DelayedDamageSource {

    private DamageSource damageSource;
    private double delay;

    public DelayedDamageSource(DamageSource damageSource, double delay) {
        this.damageSource = damageSource;
        this.delay = delay;
    }
}
