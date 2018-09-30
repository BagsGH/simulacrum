package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FireStatusProperties;

/**
 * This status is used for more than just (full) Auto weapons.
 * While the name is Auto, it's actually used for any weapon that is not a Charging,
 * Spooling, or Burst Weapon for modeling simplicity.
 */
public class Auto implements FiringStatus {
    private FireStatusProperties fireStatusProperties;
    private double refireTime;
    private double timeBetweenShots;

    public Auto(FireStatusProperties fireStatusProperties) {
        this.fireStatusProperties = fireStatusProperties;
        this.refireTime = 1 / fireStatusProperties.getFireRate();
        this.timeBetweenShots = -1.0;
    }

    public Auto(FireStatusProperties fireStatusProperties, double timeBetweenShots) {
        this.fireStatusProperties = fireStatusProperties;
        this.refireTime = 1 / fireStatusProperties.getFireRate();
        this.timeBetweenShots = timeBetweenShots;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        timeBetweenShots += deltaTime;
        if (freshMagazine()) {
            timeBetweenShots = 0.0;
            fireStatusProperties.subtractAmmo();
            return new Fired(this.fireStatusProperties, this);
        }
        if (fireStatusProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(fireStatusProperties);
        }
        if (timeBetweenShots >= refireTime) {
            fireStatusProperties.subtractAmmo();
            timeBetweenShots = 0.0;
            return new Fired(this.fireStatusProperties, this);
        }
        return this;
    }

    private boolean freshMagazine() {
        return timeBetweenShots < 0.0;
    }
}