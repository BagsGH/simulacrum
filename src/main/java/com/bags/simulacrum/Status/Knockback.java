package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

@Data
public class Knockback extends Status {

    private Knockback(DamageType damageType, double duration, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.damageTicks = damageTicks;
    }

    public Knockback() {

    }

    @Override
    public void apply(Target target) {
    }

    @Override
    public boolean applyInstantly() {
        return false;
    }
}
