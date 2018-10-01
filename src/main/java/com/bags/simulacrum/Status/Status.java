package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

/**
 * Reflection documentation:
 * https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor
 */
@Data
public abstract class Status {

    StatusPropertyMapper STATUS_PROPERTY_MAPPER = new StatusPropertyMapper();

    protected double duration;
    protected int damageTicks;
    protected DamageType damageType;
    protected double totalDamage;
    protected double progressToNextTick;

    abstract public void apply(Target target);

    abstract public boolean applyInstantly(); //TODO: Better name

    abstract public void progressTime(double deltaTime);

    abstract public boolean checkProgress();
}
