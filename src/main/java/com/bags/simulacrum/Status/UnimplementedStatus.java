package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.ArrayList;

import static com.bags.simulacrum.Damage.DamageSourceType.DOT;

@Data
public class UnimplementedStatus extends Status {

    private double duration;
    private int damageTicks;
    private double value;
    private DamageType damageType;

    public UnimplementedStatus(DamageType damageType, double duration, double value, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.value = value;
        this.damageTicks = damageTicks;
    }

    public UnimplementedStatus() {

    }

    @Override
    public DamageSource apply(Target target) {
        return new DamageSource(DOT, new ArrayList<>());
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

    @Override
    public void removeStatusEffects() {

    }
}
