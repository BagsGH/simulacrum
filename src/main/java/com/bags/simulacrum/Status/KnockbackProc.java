package com.bags.simulacrum.Status;

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
    public void applyStatusToTarget(Target target) {
    }

    @Override
    public StatusProc withDamageType(DamageType damageType) {
        double duration = StatusProcType.getStatusProcDuration(damageType);
        int ticks = StatusProcType.getStatusProcTicks(damageType);
        return new KnockbackProc(damageType, duration, ticks);
    }
}
