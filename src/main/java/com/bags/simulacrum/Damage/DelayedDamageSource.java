package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Simulation.HitProperties;
import lombok.Data;

@Data
public class DelayedDamageSource {

    private DamageSource damageSource;
    private double delay;
    private double progress;
    private HitProperties hitProperties;

    public DelayedDamageSource(DamageSource damageSource, HitProperties hitProperties, double delay) {
        this.damageSource = damageSource;
        this.delay = delay;
        this.progress = 0.0;
        this.hitProperties = hitProperties;
    }

    public DelayedDamageSource copy() {
        DelayedDamageSource copy = new DelayedDamageSource(this.damageSource.copy(), this.hitProperties.copy(), this.delay);
        copy.setProgress(this.progress);
        return copy;
    }

    public void progressTime(double deltaTime) {
        this.progress += deltaTime;
    }

    public boolean delayOver() {
        return progress >= delay;
    }
}
