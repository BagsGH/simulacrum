package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FiringProperties;

public class Charging implements FiringStatus {
    private FiringProperties firingProperties;
    private double chargingProgress;

    public Charging(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.chargingProgress = 0.0;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        chargingProgress += deltaTime;
        if (firingProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(firingProperties);
        }
        if (chargingProgress >= firingProperties.getChargeTime()) {
            chargingProgress = 0.0;
            firingProperties.expendAmmo();
            return new Fired(firingProperties, this);
        }
        return this;
    }
}