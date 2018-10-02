package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Weapon.State.FiringState;
import com.bags.simulacrum.Weapon.State.Ready;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType;
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

    private FireStateProperties fireStateProperties; //TODO: get rid of the below passthrough implementation? But I do like the idea that if you want to change something on the weapon, you tell the weapon to change it...

    public void setFireRate(double newFireRate) {
        this.fireStateProperties.setFireRate(newFireRate);
    }

    public double getFireRate() {
        return this.fireStateProperties.getFireRate();
    }

    public void setMagazineSize(int newMagazineSize) {
        this.fireStateProperties.setMagazineSize(newMagazineSize);
    }

    public int getMagazineSize() {
        return this.fireStateProperties.getMagazineSize();
    }

    public void setReloadTime(double newReloadTime) {
        this.fireStateProperties.setReloadTime(newReloadTime);
    }

    public double getReloadTime() {
        return this.fireStateProperties.getReloadTime();
    }

    public void setChargeTime(double newChargeTime) {
        this.fireStateProperties.getChargingProperties().setChargeTime(newChargeTime);
    }

    public double getChargeTime() {
        return this.fireStateProperties.getChargingProperties().getChargeTime();
    }

    public void setMaxAmmo(int newMaxAmmo) {
        this.fireStateProperties.setMaxAmmo(newMaxAmmo);
    }

    public int getMaxAmmo() {
        return this.fireStateProperties.getMaxAmmo();
    }

    public void setTriggerType(TriggerType newTriggerType) {
        this.fireStateProperties.setTriggerType(newTriggerType);
    }

    private FiringState firingState;

    public TriggerType getTriggerType() {
        return this.fireStateProperties.getTriggerType();
    }

    private List<Mod> mods;
    private WeaponInformation weaponInformation;

    public void initializeFiringState() {
        this.firingState = new Ready(this.fireStateProperties);
    }

    public FiringState firingStateProgressTime(double deltaTime) {
        this.firingState = firingState.progressTime(deltaTime);
        return firingState;
    }

    public Weapon() {
    }

    public Weapon(String name, WeaponInformation weaponInformation, FireStateProperties fireStateProperties, double rangeLimit, List<Mod> mods) {
        this.fireStateProperties = fireStateProperties;
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
