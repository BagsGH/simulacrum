package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

@Data
public class UnimplementedPROC implements StatusPROC {

    private double duration;
    private int damageTicks;
    private double value;
    private DamageType damageType;

    public UnimplementedPROC(DamageType damageType, double duration, double value, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.value = value;
        this.damageTicks = damageTicks;
    }

    public UnimplementedPROC() {

    }

    @Override
    public void applyStatusToTarget(Target target) {

    }

    @Override
    public StatusPROC withProperties(DamageType damageType, double damage) {
        return null;
    }
}
