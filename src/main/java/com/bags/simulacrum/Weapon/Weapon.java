package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Weapon {

    private String name;
    private TriggerType triggerType;
    private double fireRate;
    private double accuracy;
    private int magazineSize;
    private double reloadTime;
    private List<Damage> damageTypes;
    private List<Damage> secondaryDamageTypes;
    private double multishot;
    private double criticalChance;
    private double criticalDamage;
    private double statusChance;
    private double headshotMultiplier;
    private double accuracyMultiplier;

    /*TODO: Make this into a class */
    private double chargeTime;
    private double minBonusDamageFromCharging;
    private double maxBonusDamageFromCharging;
    private double minChargePercentage;
    /*                              */

    private List<Mod> mods;

    private int maxAmmo;
    private int masteryRank;
    private Slot slot;
    private WeaponType type;
    private AmmoType ammoType;
    private NoiseLevel noiseLevel;
    private double rangeLimit;
    private Disposition disposition;

    public Weapon() {

    }

    public Weapon(String name, int masteryRank, Slot slot, WeaponType weaponType, TriggerType triggerType, AmmoType ammoType, double rangeLimit, NoiseLevel noiseLevel, int maxAmmo, Disposition disposition, List<Mod> mods) {
        this.name = name;
        this.masteryRank = masteryRank;
        this.slot = slot;
        this.type = weaponType;
        this.triggerType = triggerType;
        this.ammoType = ammoType;
        this.rangeLimit = rangeLimit;
        this.noiseLevel = noiseLevel;
        this.maxAmmo = maxAmmo;
        this.disposition = disposition;
        this.mods = mods;
    }

    public void addMod(Mod mod) {
        if (mods == null) {
            mods = new ArrayList<>();
        }
        mods.add(mod);
    }

    public enum AmmoType {
        RIFLE,
        SNIPER,
        PISTOL,
        SHOTGUN,
        BOW
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
        MELEE,
        BOW
    }

    public enum TriggerType {
        HELD,
        CHARGE,
        DUPLEXAUTO,
        AUTOSPOOL,
        AUTO, SEMIAUTO;
    }

}
