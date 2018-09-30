package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.WeaponInformationEnums.ChargingProperties;
import lombok.Data;

@Data
public class FireStatusProperties {

    private double fireRate;
    private int magazineSize;
    private double reloadTime;
    private int maxAmmo;
    private double chargeTime;
    private int burstCount;
    private TriggerType triggerType;
    private double spoolingSpeedDecreaseModifier;
    private int spoolThreshold;

    private int currentMagazineSize;
    private double percentToCharge;

    private ChargingProperties chargingProperties;

    public void subtractAmmo() {
        this.currentMagazineSize--;
    }

    public void loadMagazine() {
        this.currentMagazineSize = magazineSize;
    }
}
