package com.bags.simulacrum.Simulation;

import lombok.Data;

@Data
public class HitProperties {

    private double criticalDamageMultiplier;
    private double headshotModifier;
    private int critLevel;
    private double bodyPartModifier;

    public HitProperties(int critLevel, double criticalDamageMultiplier, double headshotModifier, double bodyPartModifier) {
        this.criticalDamageMultiplier = criticalDamageMultiplier;
        this.headshotModifier = headshotModifier;
        this.critLevel = critLevel;
        this.bodyPartModifier = bodyPartModifier;
    }
}
