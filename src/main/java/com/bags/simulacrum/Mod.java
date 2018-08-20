package com.bags.simulacrum;

import lombok.Data;

import java.util.List;

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
    private List<Damage> damageTypes;

    public enum Polarity {
        DASH,
        D,
        V,
        EQUALS
    }

}
