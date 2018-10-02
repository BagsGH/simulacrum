package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import lombok.Data;

@Data
public class Mod {

    private String name;
    private int level;
    private int drain;
    private String rarity;
    private Polarity polarity;
    private double rangeLimitIncrease;
    private double fireRateIncrease;
    private double accuracyIncrease;
    private double magazineSizeIncrease;
    private double maxAmmoIncrease;
    private double reloadTimeIncrease;
    private double criticalChanceIncrease;
    private double criticalDamageIncrease;
    private double statusChanceIncrease;
    private double headshotMultiplierIncrease; //TODO: no mods increse this I believe, but check.
    private double damageIncrease;
    private Damage damage;
    private double multishotIncrease;
    private int index;

    public enum Polarity { //TODO: own class
        DASH,
        D,
        V,
        EQUALS
    }

    public Mod() {

    }

    public Mod(Damage damage) {
        this.damage = damage;
    }

    //TODO: factory
    public Mod copy() {
        Mod copy = new Mod(this.damage.copy());
        copy.setName(this.name);
        return copy;
    }
}
