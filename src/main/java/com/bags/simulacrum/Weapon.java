package com.bags.simulacrum;

import lombok.Data;

import java.util.Map;

@Data
public class Weapon {

    private String name;
    private int masteryRank;
    private Slot slot;
    private WeaponType type;
    private TriggerType triggerType;
    private AmmoType ammoType;
    private double rangeLimit;
    private NoiseLevel noiseLevel;
    private double fireRate;
    private double accuracy;
    private int magazineSize;
    private int maxAmmo;
    private double reloadTime;
    private Disposition disposition;
    private Map<String,Double> damage;
    private double criticalChance;
    private double criticalMultiplier;
    private double statusChance;
    private double headshotMultiplier;

    public enum AmmoType {
        RIFLE,
        SNIPER,
        PISTOL,
        SHOTGUN
    }

    public enum NoiseLevel {
        ALARMING,
        SILENT
    }

    public enum Disposition {
        FAINT,
        MILD,
        NEUTRAL,
        STRONG,
        COMPETENT
    }

    public enum Slot {
        PRIMARY,
        SECONDARY,
        MELEE
    }

    public enum WeaponType {
        RIFLE,
        SNIPER,
        PISTOL,
        SHOTGUN,
        MELEE
    }

    public enum TriggerType {
        HELD,
        CHARGE,
        DUPLEXAUTO,
        AUTOSPOOL,
        AUTO
    }

}
