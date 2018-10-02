package com.bags.simulacrum.Damage;

import lombok.Data;

@Data
public class Damage {

    private DamageType type;
    private double damageValue;
    private double modAddedDamageRatio;

    public Damage(DamageType type, double damageValue, double modAddedDamageRatio) {
        this.type = type;
        this.damageValue = damageValue;
        this.modAddedDamageRatio = modAddedDamageRatio;
    }

    public Damage(DamageType type, double damageValue) {
        this.type = type;
        this.damageValue = damageValue;
    }

    public Damage(DamageType type) {
        this.type = type;
    }

    public Damage(Damage damage) {
        this.type = damage.getType();
        this.damageValue = damage.getDamageValue();
        this.modAddedDamageRatio = damage.getModAddedDamageRatio();
    }

    public Damage copy() {
        return new Damage(this.type, this.damageValue, this.modAddedDamageRatio);
    }

    public Damage() {

    }
}
