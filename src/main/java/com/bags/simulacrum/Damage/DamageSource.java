package com.bags.simulacrum.Damage;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DamageSource {

    private DamageSourceType damageSourceType;
    private List<Damage> damages;
    private double delay;
    private double aoe;

    public DamageSource(DamageSourceType damageSourcetype, List<Damage> damages) {
        this.damages = damages;
        this.damageSourceType = damageSourcetype;
    }

    public DamageSource(DamageSourceType damageSourcetype, List<Damage> damages, double delay, double aoe) {
        this.damages = damages;
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

    public void addDamage(Damage damage) {
        if (damages == null) {
            damages = new ArrayList<>();
        }
        damages.add(damage);
    }

}
