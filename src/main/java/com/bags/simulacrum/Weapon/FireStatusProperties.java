package com.bags.simulacrum.Weapon;

import lombok.Data;

@Data
public class FireStatusProperties {

    private TriggerType triggerType;
    private double fireRate;
    private int magazineSize;
    private double reloadTime;
    private int maxAmmo;
    private int spoolThreshold;
    private double spoolingSpeedDecreaseModifier;
    private double chargeTime;

    private int currentMagazineSize;
    private double percentToCharge;
    private double minChargePercentage;

    private int burstCount;

    public enum TriggerType {
        HELD,
        CHARGE,
        DUPLEXAUTO,
        AUTOSPOOL,
        AUTO, SEMIAUTO, BURST;
    }

    public void expendAmmo() {
        this.currentMagazineSize--;
    }

    public void loadMagazine() {
        this.currentMagazineSize = magazineSize;
    }


}
