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
    private double headshotMultiplierIncrease;
    private double damageIncrease;
    private Damage damage;
    private int index;

    public enum Polarity {
        DASH,
        D,
        V,
        EQUALS
    }

}
