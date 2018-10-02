package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import lombok.Data;

@Data
public class Ignite extends StatusDot {

    private Ignite(DamageType damageType, Double duration, Integer damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.numberOfDamageTicks = damageTicks;
    }

    public Ignite() {

    }

}
