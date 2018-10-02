package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStateProperties;

/**
 * The status for a Weapon while it is in the spooling part of its fire pattern.
 * Weapons that are spooling fire at a reduced rate until they fire enough shots.
 * With each shot while spooling the weapon gains a portion of speed. After firing enough
 * it will transition to the (full) Auto state.
 */
public class Spooling implements FiringState {
    private FireStateProperties fireStateProperties;
    private double timeBetweenShots;
    private int spoolingThreshold;
    private int spoolingProgress;
    private double spoolingProgressBonus;

    public Spooling(FireStateProperties fireStateProperties) {
        this.fireStateProperties = fireStateProperties;
        this.timeBetweenShots = -1.0;
        this.spoolingThreshold = fireStateProperties.getSpoolThreshold();
    }

    @Override
    public FiringState progressTime(double deltaTime) {
        timeBetweenShots += deltaTime;
        if (freshMagazine()) {
            timeBetweenShots = 0.0;
            fireStateProperties.subtractAmmo();
            return new Fired(this.fireStateProperties, this);
        }
        if (spoolingProgress >= spoolingThreshold) {
            FiringState status = new Auto(this.fireStateProperties, timeBetweenShots);
            return status.progressTime(deltaTime);
        }
        if (fireStateProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(fireStateProperties);
        }
        if (timeBetweenShots + spoolingProgressBonus >= getRefireTime()) {
            fireStateProperties.subtractAmmo();
            timeBetweenShots = 0.0;
            spoolingProgress++;
            spoolingProgressBonus = spoolingProgress * getSpoolingSpeedStepUp();
            return new Fired(this.fireStateProperties, this);
        }
        return this;
    }

    private double getSpoolingSpeedStepUp() {
        return (1 / fireStateProperties.getFireRate()) / this.spoolingThreshold;
    }

    private double getRefireTime() {
        return (1 / fireStateProperties.getFireRate()) * (1 + fireStateProperties.getSpoolingSpeedDecreaseModifier());
    }

    private boolean freshMagazine() {
        return timeBetweenShots < 0.0;
    }
}