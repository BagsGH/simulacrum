package com.bags.simulacrum.Damage;

import lombok.Data;

import java.util.List;

@Data
public class DamageSource {

    private DamageSourceType damageSourceType;
    private List<Damage> damageTypes;
    private double delay;
    private double aoe;

    public DamageSource(DamageSourceType damageSourcetype, List<Damage> damageTypes) {
        this.damageTypes = damageTypes;
        this.damageSourceType = damageSourcetype;
    }

    public DamageSource(DamageSourceType damageSourcetype, List<Damage> damageTypes, double delay, double aoe) {
        this.damageTypes = damageTypes;
        this.damageSourceType = damageSourcetype;
        this.delay = delay;
        this.aoe = aoe;
    }

    public DamageSource(DamageSource damageSourceToCopy) {
        this.damageSourceType = damageSourceToCopy.getDamageSourceType();
        this.delay = damageSourceToCopy.getDelay();
        this.aoe = damageSourceToCopy.getAoe();
    }

    public DamageSource() {

    }

}
