package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.ChargingProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Weapon {

    private String name; //TODO: eventually move to fluff. Left here now for ease of debugging.

    private double accuracy;
    private List<DamageSource> damageSources;
    private double multishot;
    private double criticalChance;
    private double criticalDamage;
    private double statusChance;
    private double headshotMultiplier; //TODO: unused, but will be used
    private double accuracyMultiplier; //TODO: how to use
    private double rangeLimit;

    private FireStatusProperties fireStatusProperties; //TODO: get rid of the below passthrough implementation? But I do like the idea that if you want to change something on the weapon, you tell the weapon to change it...

    public void setFireRate(double newFireRate) {
        this.fireStatusProperties.setFireRate(newFireRate);
    }

    public double getFireRate() {
        return this.fireStatusProperties.getFireRate();
    }

    public void setMagazineSize(int newMagazineSize) {
        this.fireStatusProperties.setMagazineSize(newMagazineSize);
    }

    public int getMagazineSize() {
        return this.fireStatusProperties.getMagazineSize();
    }

    public void setReloadTime(double newReloadTime) {
        this.fireStatusProperties.setReloadTime(newReloadTime);
    }

    public double getReloadTime() {
        return this.fireStatusProperties.getReloadTime();
    }

    public void setChargeTime(double newChargeTime) {
        this.fireStatusProperties.setChargeTime(newChargeTime);
    }

    public double getChargeTime() {
        return this.fireStatusProperties.getChargeTime();
    }

    public void setMaxAmmo(int newMaxAmmo) {
        this.fireStatusProperties.setMaxAmmo(newMaxAmmo);
    }

    public int getMaxAmmo() {
        return this.fireStatusProperties.getMaxAmmo();
    }

    public void setTriggerType(TriggerType newTriggerType) {
        this.fireStatusProperties.setTriggerType(newTriggerType);
    }

    public TriggerType getTriggerType() {
        return this.fireStatusProperties.getTriggerType();
    }

    private ChargingProperties chargingProperties;
    private List<Mod> mods;
    private WeaponInformation weaponInformation;
    private WeaponStatus weaponStatus;

    public void initializeWeaponStatus() {
        this.weaponStatus = new WeaponStatus(this.fireStatusProperties);
    }

    public Weapon() {
    }

    public Weapon(String name, WeaponInformation weaponInformation, FireStatusProperties fireStatusProperties, double rangeLimit, List<Mod> mods) {
        this.fireStatusProperties = fireStatusProperties;
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

    public void addDamageSource(DamageSource damageSource) {
        if (damageSources == null) {
            damageSources = new ArrayList<>();
        }
        damageSources.add(damageSource);
    }

}
