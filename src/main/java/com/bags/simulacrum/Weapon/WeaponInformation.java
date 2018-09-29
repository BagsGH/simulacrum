package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.WeaponInformationEnums.*;
import lombok.Data;

@Data
public class WeaponInformation {

    private int masteryRank;
    private WeaponSlot slot;
    private WeaponClass weaponClass;
    private AmmoType ammoType;
    private NoiseLevel noiseLevel;
    private Disposition disposition;

    public WeaponInformation(WeaponClass weaponClass, WeaponSlot slot, int masteryRank, AmmoType ammoType, NoiseLevel noiseLevel, Disposition disposition) {
        this.masteryRank = masteryRank;
        this.slot = slot;
        this.weaponClass = weaponClass;
        this.ammoType = ammoType;
        this.noiseLevel = noiseLevel;
        this.disposition = disposition;
    }

    public WeaponInformation copy() {
        return new WeaponInformation(this.weaponClass, this.slot, this.masteryRank, this.ammoType, this.noiseLevel, this.disposition);
    }
}
