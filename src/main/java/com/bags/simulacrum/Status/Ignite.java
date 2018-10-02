package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.Arrays;

import static com.bags.simulacrum.Damage.DamageSourceType.DOT;

@Data
public class Ignite extends Status {

    private Ignite(DamageType damageType, Double duration, Integer damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.numberOfDamageTicks = damageTicks;
    }

    public Ignite() {

    }

    @Override
    public void apply(Target target) {
        this.progressToNextTick = 0.0;
        this.tickProgress++;
    }

    @Override
    public DamageSource getDamageSource() {
        return new DamageSource(DOT, Arrays.asList(new Damage(this.damageType, this.getDamagePerTick())));
    }


    @Override
    public boolean applyInstantly() {
        return true;
    }

    @Override
    public void progressTime(double deltaTime) {
        this.progressToNextTick += deltaTime;
    }

    @Override
    public boolean checkProgress() {
        return this.progressToNextTick >= this.durationPerTick;
    }

    @Override
    public void setupTimers() {
        this.durationPerTick = this.duration / (this.numberOfDamageTicks - (applyInstantly() ? 1 : 0));
        this.progressToNextTick = 0.0;
        this.tickProgress = 0;
    }

    @Override
    public boolean finished() {
        return this.tickProgress >= this.numberOfDamageTicks;
    }

    @Override
    public void removeStatusEffects() {

    }
}
