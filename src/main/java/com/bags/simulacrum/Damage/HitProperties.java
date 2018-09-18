package com.bags.simulacrum.Damage;

import lombok.Data;

@Data
public class HitProperties {

    private double criticalDamageModifier;
    private double headshotMultiplier;
    private int critLevel;
    private double bodyPartModifier;

    public HitProperties(int critLevel, double criticalDamageModifier, double headshotMultiplier, double bodyPartModifier) {
        this.criticalDamageModifier = criticalDamageModifier;
        this.headshotMultiplier = headshotMultiplier;
        this.critLevel = critLevel;
        this.bodyPartModifier = bodyPartModifier;
    }
}
