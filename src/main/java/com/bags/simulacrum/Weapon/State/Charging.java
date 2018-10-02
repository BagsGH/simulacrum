package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStateProperties;

public class Charging implements FiringState {
    private FireStateProperties fireStateProperties;
    private double chargingProgress;

    public Charging(FireStateProperties fireStateProperties) {
        this.fireStateProperties = fireStateProperties;
        this.chargingProgress = 0.0;
    }

    @Override
    public FiringState progressTime(double deltaTime) {
        chargingProgress += deltaTime;
        if (fireStateProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(fireStateProperties);
        }
        if (chargingProgress >= fireStateProperties.getChargeTime()) {
            chargingProgress = 0.0;
            fireStateProperties.subtractAmmo();
            return new Fired(fireStateProperties, this);
        }
        return this;
    }
}