package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.ArrayList;

import static com.bags.simulacrum.Damage.DamageSourceType.DOT;

@Data
public class Ignite extends Status {

    private static final double ARMOR_REDUCTION_RATIO = 0.25;

    private Ignite(DamageType damageType, Double duration, Integer damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.numberOfDamageTicks = damageTicks;
    }

    public Ignite() {

    }

    @Override
    public void apply(Target target) {
    }

    @Override
    public DamageSource getDamageSource() {
        return new DamageSource(DOT, new ArrayList<>());
    }


    @Override
    public boolean applyInstantly() {
        return false;
    }

    @Override
    public void progressTime(double deltaTime) {

    }

    @Override
    public boolean checkProgress() {
        return false;
    }

    @Override
    public void setupTimers() {

    }

    @Override
    public boolean finished() {
        return false;
    }
}
