package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FireStatusProperties;

/**
 * The status for a Weapon while it is in the spooling part of its fire pattern.
 * Weapons that are spooling fire at a reduced rate until they fire enough shots.
 * With each shot while spooling the weapon gains a portion of speed. After firing enough
 * it will transition to the (full) Auto state.
 */
public class Spooling implements FiringStatus {
    private FireStatusProperties fireStatusProperties;
    private double timeBetweenShots;
    private int spoolingThreshold;
    private int spoolingProgress;
    private double spoolingProgressBonus;

    public Spooling(FireStatusProperties fireStatusProperties) {
        this.fireStatusProperties = fireStatusProperties;
        this.timeBetweenShots = -1.0;
        this.spoolingThreshold = fireStatusProperties.getSpoolThreshold();
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        timeBetweenShots += deltaTime;
        if (freshMagazine()) {
            timeBetweenShots = 0.0;
            fireStatusProperties.subtractAmmo();
            return new Fired(this.fireStatusProperties, this);
        }
        if (spoolingProgress >= spoolingThreshold) {
            FiringStatus status = new Auto(this.fireStatusProperties, timeBetweenShots);
            return status.progressTime(deltaTime);
        }
        if (fireStatusProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(fireStatusProperties);
        }
        if (timeBetweenShots + (spoolingProgressBonus) >= getRefireTime()) {
            fireStatusProperties.subtractAmmo();
            timeBetweenShots = 0.0;
            spoolingProgress++;
            spoolingProgressBonus = spoolingProgress * getSpoolingSpeedStepUp();
            return new Fired(this.fireStatusProperties, this);
        }
        return this;
    }

    private double getSpoolingSpeedStepUp() {
        return (1 / fireStatusProperties.getFireRate()) / this.spoolingThreshold;
    }

    private double getRefireTime() {
        return (1 / fireStatusProperties.getFireRate()) * (1 + fireStatusProperties.getSpoolingSpeedDecreaseModifier());
    }

    private boolean freshMagazine() {
        return timeBetweenShots < 0.0;
    }
}