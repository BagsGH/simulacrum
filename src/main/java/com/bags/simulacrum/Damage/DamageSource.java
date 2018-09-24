package com.bags.simulacrum.Damage;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public DamageSource copyWithoutDamages() {
        DamageSource copiedDamageSource = new DamageSource();
        copiedDamageSource.setDamageSourceType(this.damageSourceType);
        copiedDamageSource.setAoe(this.aoe);
        copiedDamageSource.setDelay(this.delay);
        return copiedDamageSource;
    }

    public DamageSource deepCopy() {
        DamageSource copiedDamageSource = this.copyWithoutDamages();
        copiedDamageSource.setDamages(damages.stream().map(Damage::new).collect(Collectors.toList()));
        return copiedDamageSource;
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
