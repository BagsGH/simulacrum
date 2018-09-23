package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

@Data
public class CorrosionProc implements StatusProc {

    private double duration;
    private int damageTicks;
    private int stacks;
    private DamageType damageType;

    public CorrosionProc(DamageType damageType, double duration, int stacks, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.stacks = stacks;
        this.damageTicks = damageTicks;
    }

    public CorrosionProc() {

    }

    @Override
    public void applyStatusToTarget(Target target) {

    }

    @Override
    public StatusProc withProperties(DamageType damageType, double damage) {
        double duration = StatusProcType.getStatusProcDuration(damageType);
        int ticks = StatusProcType.getStatusProcTicks(damageType);

        return new CorrosionProc(damageType, duration, 1, ticks);
    }
}
