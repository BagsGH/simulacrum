package com.bags.simulacrum.StatusProc;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

/**
 * Reflection documentation:
 * https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor
 */
@Data
public abstract class StatusProc {

    StatusPropertyMapper STATUS_PROPERTY_MAPPER = new StatusPropertyMapper();

    protected double duration;
    protected int damageTicks;
    protected DamageType damageType;
    protected double totalDamage;

    abstract public void apply(Target target);

    abstract public boolean applyInstantly(); //TODO: Better name
}
