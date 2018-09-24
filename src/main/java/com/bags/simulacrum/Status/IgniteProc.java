package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

@Data
public class IgniteProc implements StatusProc {

    private double duration;
    private int damageTicks;
    private DamageType damageType;

    private static final double ARMOR_REDUCTION_RATIO = 0.25;

    private IgniteProc(DamageType damageType, double duration, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.damageTicks = damageTicks;
    }

    public IgniteProc() {

    }

    @Override
    public void apply(Target target) {
    }

    @Override
    public StatusProc withDamageType(DamageType damageType) {
        double duration = STATUS_PROPERTY_MAPPER.getStatusProcDuration(damageType);
        int ticks = STATUS_PROPERTY_MAPPER.getStatusProcTicks(damageType);

        return new IgniteProc(damageType, duration, ticks);
    }

    @Override
    public boolean applyInstantly() {
        return false;
    }
}
