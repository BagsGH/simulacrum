package com.bags.simulacrum.Damage;

import lombok.Data;

@Data
public class HitProperties {

    private double criticalDamageModifier;
    private double headshotModifier;
    private int critLevel;
    private double bodyPartModifier;

    public HitProperties(int critLevel, double criticalDamageModifier, double headshotModifier, double bodyPartModifier) {
        this.criticalDamageModifier = criticalDamageModifier;
        this.headshotModifier = headshotModifier;
        this.critLevel = critLevel;
        this.bodyPartModifier = bodyPartModifier;
    }
}
