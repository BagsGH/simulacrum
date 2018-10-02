package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

/**
 * Reflection documentation:
 * https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor
 */
@Data
public abstract class Status {

    StatusFactory STATUS_PROPERTY_MAPPER = new StatusFactory();

    protected double duration;
    protected DamageType damageType;
    protected double damagePerTick;
    protected int numberOfDamageTicks;
    protected double progressToNextTick;
    protected double durationPerTick;
    protected int tickProgress;


    abstract public void apply(Target target);

    abstract public DamageSource getDamageTickDamageSource();

    abstract public void progressTime(double deltaTime);

    abstract public boolean checkProgress();

    abstract public void setupTimers();

    abstract public boolean finished();

    abstract public void removeStatusEffects();
}
