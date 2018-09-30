package com.bags.simulacrum.StatusProc;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

@Data
public class UnimplementedProc implements StatusProc {

    private double duration;
    private int damageTicks;
    private double value;
    private DamageType damageType;

    public UnimplementedProc(DamageType damageType, double duration, double value, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.value = value;
        this.damageTicks = damageTicks;
    }

    public UnimplementedProc() {

    }

    @Override
    public void apply(Target target) {

    }

    @Override
    public StatusProc withDamageType(DamageType damageType) {
        return null;
    }

    @Override
    public boolean applyInstantly() {
        return false;
    }
}
