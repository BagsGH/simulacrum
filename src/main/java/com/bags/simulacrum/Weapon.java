package com.bags.simulacrum;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Weapon {

    private String name;
    private TriggerType triggerType;
    private double fireRate;
    private double accuracy;
    private int magazineSize;
    private double reloadTime;
    private Map<DamageType, Double> damage;
    private double criticalChance;
    private double criticalDamage;
    private double statusChance;
    private double headshotMultiplier;
    private double accuracyMultiplier;
    private List<Mod> mods;

    private int maxAmmo;
    private int masteryRank;
    private Slot slot;
    private WeaponType type;
    private AmmoType ammoType;
    private NoiseLevel noiseLevel;
    private double rangeLimit;
    private Disposition disposition;

    public Weapon()
    {

    }

    public Weapon(String name, int masteryRank, Slot slot, WeaponType weaponType, AmmoType ammoType, double rangeLimit, NoiseLevel noiseLevel, int maxAmmo, Disposition disposition, List<Mod> mods) {
        this.name = name;
        this.masteryRank = masteryRank;
        this.slot = slot;
        this.type = weaponType;
        this.ammoType = ammoType;
        this.rangeLimit = rangeLimit;
        this.noiseLevel = noiseLevel;
        this.maxAmmo = maxAmmo;
        this.disposition = disposition;
        this.mods = mods;
    }

    public void addMod(Mod mod) {
        mods.add(mod);
    }

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
