package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FireStatusProperties;

public class Charging implements FiringStatus {
    private FireStatusProperties fireStatusProperties;
    private double chargingProgress;

    public Charging(FireStatusProperties fireStatusProperties) {
        this.fireStatusProperties = fireStatusProperties;
        this.chargingProgress = 0.0;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        chargingProgress += deltaTime;
        if (fireStatusProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(fireStatusProperties);
        }
        if (chargingProgress >= fireStatusProperties.getChargeTime()) {
            chargingProgress = 0.0;
            fireStatusProperties.subtractAmmo();
            return new Fired(fireStatusProperties, this);
        }
        return this;
    }
}