package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

@Data
public class Ignite extends Status {

    private static final double ARMOR_REDUCTION_RATIO = 0.25;

    private Ignite(DamageType damageType, Double duration, Integer damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.damageTicks = damageTicks;
    }

    public Ignite() {

    }

    @Override
    public void apply(Target target) {
    }


    @Override
    public boolean applyInstantly() {
        return false;
    }
}
