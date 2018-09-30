package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.ChargingProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Weapon {

    private String name; //TODO: eventually move to fluff

    private double accuracy;
    private List<DamageSource> damageSources;
    private double multishot;
    private double criticalChance;
    private double criticalDamage;
    private double statusChance;
    private double headshotMultiplier;
    private double accuracyMultiplier;
    private double rangeLimit;

    private TriggerType triggerType;
    private double fireRate;
    private int magazineSize;
    private double reloadTime;
    private int burstCount;
    private int maxAmmo;
    private double chargeTime;

    private FireStatusProperties fireStatusProperties;
    private ChargingProperties chargingProperties;

    private List<Mod> mods;

    private WeaponInformation weaponInformation;

    private WeaponStatus weaponStatus;

    public void resetWeaponStatus() {
        this.weaponStatus = new WeaponStatus(this.fireStatusProperties);
    }

    public Weapon() {

    }

    public Weapon(String name, WeaponInformation weaponInformation, TriggerType triggerType, double rangeLimit, int maxAmmo, List<Mod> mods) {
        this.triggerType = triggerType;
        this.maxAmmo = maxAmmo;
        this.weaponInformation = weaponInformation;
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
