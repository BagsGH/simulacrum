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

    private double chargeTime;
    private double minBonusDamageFromCharging;
    private double maxBonusDamageFromCharging;
    private double minChargePercentage;

    private boolean fullySpooled;

    public enum TriggerType {
        HELD,
        CHARGE,
        DUPLEXAUTO,
        AUTOSPOOL,
        AUTO, SEMIAUTO;
    }

    public void expendAmmo() {
        this.currentMagazineSize--;
    }

    public void reloadMagazine() {
        this.currentMagazineSize = magazineSize;
    }


}
