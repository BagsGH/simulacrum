package com.bags.simulacrum;

import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

@Data
public class Weapon {

    private String name;
    private TriggerType triggerType;
    private double effectiveFireRate;
    private double fireRate;
    private double effectiveAccuracy;
    private double accuracy;
    private int effectiveMagazineSize;
    private int magazineSize;
    private double effectiveReloadTime;
    private double reloadTime;
    private Map<DamageType,Double> effectiveDamage;
    private Map<DamageType,Double> damage;
    private double effectiveCriticalChance;
    private double criticalChance;
    private double effectiveCriticalMultiplier;
    private double criticalMultiplier;
    private double effectiveStatusChance;
    private double statusChance;
    private double effectiveHeadshotMultiplier;
    private double headshotMultiplier;
    private double accuracyMultiplier;
    private ArrayList<Mod> mods;

    private int maxAmmo;
    private int masteryRank;
    private Slot slot;
    private WeaponType type;
    private AmmoType ammoType;
    private NoiseLevel noiseLevel;
    private double rangeLimit;
    private Disposition disposition;

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

}
