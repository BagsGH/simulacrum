package com.bags.simulacrum.StatusProc;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

@Data
public class KnockbackProc implements StatusProc {

    private double duration;
    private int damageTicks;
    private int stacks;
    private DamageType damageType;

    private static final double ARMOR_REDUCTION_RATIO = 0.25;

    private KnockbackProc(DamageType damageType, double duration, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.damageTicks = damageTicks;
    }

    public KnockbackProc() {

    }

    @Override
    public void apply(Target target) {
    }

    @Override
    public StatusProc withDamageType(DamageType damageType) {
        double duration = STATUS_PROPERTY_MAPPER.getStatusProcDuration(damageType);
        int ticks = STATUS_PROPERTY_MAPPER.getStatusProcTicks(damageType);
        return new KnockbackProc(damageType, duration, ticks);
    }

    @Override
    public boolean applyInstantly() {
        return false;
    }
}
