package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FiringProperties;

public class Charging implements FiringStatus {
    private FiringProperties firingProperties;
    private double chargingProgress;
    private double chargingTime;

    public Charging(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.chargingProgress = 0.0;
        this.chargingTime = firingProperties.getChargeTime();
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        chargingProgress += deltaTime;
        if (firingProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(firingProperties);
        }
        if (chargingProgress >= chargingTime) {
            chargingProgress = 0.0;
            firingProperties.expendAmmo();
            return new Fired(firingProperties, this);
        }
        return this;
    }
}