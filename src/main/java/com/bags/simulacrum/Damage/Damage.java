package com.bags.simulacrum.Damage;

import lombok.Data;

@Data
public class Damage {

    private DamageType damageType;
    private double damageValue;
    private double modAddedDamageRatio;

    public Damage(DamageType damageType, double damageValue, double modAddedDamageRatio) {
        this.damageType = damageType;
        this.damageValue = damageValue;
        this.modAddedDamageRatio = modAddedDamageRatio;
    }

    public Damage(DamageType damageType, double damageValue) {
        this.damageType = damageType;
        this.damageValue = damageValue;
    }

    public Damage(DamageType damageType) {
        this.damageType = damageType;
    }

    public Damage(Damage damage) {
        this.damageType = damage.getDamageType();
        this.damageValue = damage.getDamageValue();
        this.modAddedDamageRatio = damage.getModAddedDamageRatio();
    }

    public Damage copy() {
        return new Damage(this.damageType, this.damageValue, this.modAddedDamageRatio);
    }

    public Damage() {

    }
}
