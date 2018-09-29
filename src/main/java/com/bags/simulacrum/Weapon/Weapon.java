package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.DamageSource;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Weapon {

    private String name; //TODO: eventually move to fluff
    private TriggerType triggerType;
    private double fireRate;
    private double accuracy;
    private int magazineSize;
    private double reloadTime;
    private List<DamageSource> damageSources;
    private double multishot;
    private double criticalChance;
    private double criticalDamage;
    private double statusChance;
    private double headshotMultiplier;
    private double accuracyMultiplier;
    private int burstCount;
    private double burstSpeed;
    private double burstCooldown;

    private double rangeLimit;

    /*TODO: Make this into a class */
    private double chargeTime;
    private double minBonusDamageFromCharging;
    private double maxBonusDamageFromCharging;
    private double minChargePercentage;
    /*                              */

    private List<Mod> mods;
    private int maxAmmo;

    private WeaponMetaData weaponMetaData;

    public Weapon() {

    }

    public Weapon(String name, WeaponMetaData weaponMetaData, TriggerType triggerType, double rangeLimit, int maxAmmo, List<Mod> mods) {
        this.triggerType = triggerType;
        this.maxAmmo = maxAmmo;
        this.weaponMetaData = weaponMetaData;
        this.name = name;
        this.rangeLimit = rangeLimit;
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

    public void addDamageSource(DamageSource damageSource) {
        if (damageSources == null) {
            damageSources = new ArrayList<>();
        }
        damageSources.add(damageSource);
    }

}
