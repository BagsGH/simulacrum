package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStateProperties;

/**
 * This status is used for more than just (full) Auto weapons.
 * While the name is Auto, it's actually used for any weapon that is not a Charging,
 * Spooling, or Burst Weapon for modeling simplicity.
 */
public class Auto implements FiringState {
    private FireStateProperties fireStateProperties;
    private double timeBetweenShots;

    public Auto(FireStateProperties fireStateProperties) {
        this.fireStateProperties = fireStateProperties;
        this.timeBetweenShots = -1.0;
    }

    private double getRefireTime() {
        return 1 / fireStateProperties.getFireRate();
    }

    public Auto(FireStateProperties fireStateProperties, double timeBetweenShots) {
        this.fireStateProperties = fireStateProperties;
        this.timeBetweenShots = timeBetweenShots;
    }

    @Override
    public FiringState progressTime(double deltaTime) {
        timeBetweenShots += deltaTime;
        if (freshMagazine()) {
            timeBetweenShots = 0.0;
            fireStateProperties.subtractAmmo();
            return new Fired(this.fireStateProperties, this);
        }
        if (fireStateProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(fireStateProperties);
        }
        if (timeBetweenShots >= getRefireTime()) {
            fireStateProperties.subtractAmmo();
            timeBetweenShots = 0.0;
            return new Fired(this.fireStateProperties, this);
        }
        return this;
    }

    private boolean freshMagazine() {
        return timeBetweenShots < 0.0;
    }
}