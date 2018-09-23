package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

@Data
public class Corrosion implements StatusPROC {

    private double duration;
    private int damageTicks;
    private int stacks;
    private DamageType damageType;

    public Corrosion(DamageType damageType, double duration, int stacks, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.stacks = stacks;
        this.damageTicks = damageTicks;
    }

    public Corrosion() {

    }

    @Override
    public void applyStatusToTarget(Target target) {

    }

    @Override
    public StatusPROC withProperties(DamageType damageType, double damage) {
        double duration = StatusPROCType.getStatusPROCDuration(damageType);
        int ticks = StatusPROCType.getStatusPROCTicks(damageType);

        return new Corrosion(damageType, duration, 1, ticks);
    }
}
