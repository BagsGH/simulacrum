package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FireStatusProperties;

public class Charging implements FiringStatus {
    private FireStatusProperties fireStatusProperties;
    private double chargingProgress;
    private double chargingTime;

    public Charging(FireStatusProperties fireStatusProperties) {
        this.fireStatusProperties = fireStatusProperties;
        this.chargingProgress = 0.0;
        this.chargingTime = fireStatusProperties.getChargeTime();
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        chargingProgress += deltaTime;
        if (fireStatusProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(fireStatusProperties);
        }
        if (chargingProgress >= chargingTime) {
            chargingProgress = 0.0;
            fireStatusProperties.subtractAmmo();
            return new Fired(fireStatusProperties, this);
        }
        return this;
    }
}