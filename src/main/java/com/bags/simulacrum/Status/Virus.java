package com.bags.simulacrum.Status;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Entity.Target;

import java.util.ArrayList;

import static com.bags.simulacrum.Damage.DamageSourceType.DOT;

public class Virus extends Status {

    private static final double HEALTH_REDUCTION_RATIO = 0.5;
    private double currentHealthRemoved;
    private double maxHealthRemoved;
    private double durationProgress;
    private Target affectedTarget;

    @Override
    public void apply(Target target) {
        Health targetHealth = target.getHealthHealth();
        double currentHealth = targetHealth.getHealthValue();
        double maxHealth = targetHealth.getHealthValueMax();

        this.currentHealthRemoved = HEALTH_REDUCTION_RATIO * currentHealth;
        this.maxHealthRemoved = HEALTH_REDUCTION_RATIO * maxHealth;

        targetHealth.setHealthValueMax(maxHealth - this.maxHealthRemoved);
        targetHealth.setHealthValue(currentHealth - this.currentHealthRemoved);

        this.affectedTarget = target;
    }

    @Override
    public DamageSource getDamageSource() {
        return new DamageSource(DOT, new ArrayList<>());
    }


    @Override
    public boolean applyInstantly() {
        return true;
    }

    @Override
    public void progressTime(double deltaTime) {
        this.durationProgress += deltaTime;
    }

    @Override
    public boolean checkProgress() {
        return false;
    }

    @Override
    public void setupTimers() {
        durationProgress = 0.0;
    }

    @Override
    public boolean finished() {
        return this.durationProgress >= this.duration;
    }

    @Override
    public void removeStatusEffects() {
        Health targetHealth = this.affectedTarget.getHealthHealth();
        double currentHealth = targetHealth.getHealthValue();
        double maxHealth = targetHealth.getHealthValueMax();

        targetHealth.setHealthValueMax(maxHealth + this.maxHealthRemoved);
        targetHealth.setHealthValue(currentHealth + this.currentHealthRemoved);

    }
}
