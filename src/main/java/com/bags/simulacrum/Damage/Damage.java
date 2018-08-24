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

    public Damage() {

    }

    public Damage(DamageType type) {
        this.type = type;
    }

    public Damage(Damage damage) {
        this.type = damage.getType();
        this.damageValue = damage.getDamageValue();
        this.modAddedDamageRatio = damage.getModAddedDamageRatio();
    }

    public enum DamageType {
        PUNCTURE,
        IMPACT,
        SLASH,
        ELECTRICITY,
        COLD,
        HEAT,
        TOXIN,
        TRUE,
        VOID,
        BLAST,
        CORROSIVE,
        GAS,
        MAGNETIC,
        RADIATION,
        VIRAL
    }

    public static boolean isElemental(Damage damage) {
        return damage.getType().equals(DamageType.ELECTRICITY) || damage.getType().equals(DamageType.COLD) || damage.getType().equals(DamageType.HEAT) || damage.getType().equals(DamageType.TOXIN) || damage.getType().equals(DamageType.BLAST) ||
                damage.getType().equals(DamageType.CORROSIVE) || damage.getType().equals(DamageType.GAS) || damage.getType().equals(DamageType.MAGNETIC) || damage.getType().equals(DamageType.RADIATION) || damage.getType().equals(DamageType.VIRAL);
    }

    public static boolean isIPS(Damage damage) {
        return damage.getType().equals(DamageType.IMPACT) || damage.getType().equals(DamageType.PUNCTURE) || damage.getType().equals(DamageType.SLASH);
    }

}
