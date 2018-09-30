package com.bags.simulacrum.Weapon;

import lombok.Data;

@Data
public class FiringProperties {

    private TriggerType triggerType;
    private double fireRate;
    private double accuracy;
    private int currentMagazineSize;
    private int magazineSize;
    private double reloadTime;
    private int maxAmmo;
    private int spoolThreshold;
    private double spoolingSpeedDecreaseModifier;

    private double chargeTime;
    private double minBonusDamageFromCharging;
    private double maxBonusDamageFromCharging;
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
