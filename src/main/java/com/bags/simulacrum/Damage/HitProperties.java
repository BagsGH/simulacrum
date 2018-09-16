package com.bags.simulacrum.Damage;

import lombok.Data;

@Data
public class HitProperties {

    private double weaponCriticalDamageModifier;
    private double headshotModifier;
    private int critLevel;
    private double bodyPartModifier;

    public HitProperties(double weaponCriticalDamageModifier, double headshotModifier, int critLevel, double bodyPartModifier) {
        this.weaponCriticalDamageModifier = weaponCriticalDamageModifier;
        this.headshotModifier = headshotModifier;
        this.critLevel = critLevel;
        this.bodyPartModifier = bodyPartModifier;
    }
}
