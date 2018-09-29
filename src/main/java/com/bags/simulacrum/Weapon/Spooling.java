package com.bags.simulacrum.Weapon;

/**
 * The status for a Weapon while it is in the spooling part of its fire pattern.
 * Weapons that are spooling fire at a reduced rate until they fire enough shots.
 * With each shot while spooling the weapon gains a portion of speed. After firing enough
 * it will transition to the (full) Auto state.
 */
public class Spooling implements FiringStatus {
    private FiringProperties firingProperties;
    private double refireTime;
    private double timeBetweenShots;
    private int spoolingThreshold;
    private int spoolingProgress;
    private double spoolingSpeedStepUp;
    private double spoolingProgressBonus;

    public Spooling(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.refireTime = (1 / firingProperties.getFireRate()) * (1 + firingProperties.getSpoolingSpeedDecreaseModifier());
        this.timeBetweenShots = -1.0;
        this.spoolingThreshold = firingProperties.getSpoolThreshold();
        this.spoolingSpeedStepUp = (1 / firingProperties.getFireRate()) / this.spoolingThreshold;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        timeBetweenShots += deltaTime;
        if (freshMagazine()) {
            timeBetweenShots = 0.0;
            firingProperties.expendAmmo();
            return new Fired(this.firingProperties, this);
        }
        if (spoolingProgress >= spoolingThreshold) {
            FiringStatus status = new Auto(this.firingProperties, timeBetweenShots);
            return status.progressTime(deltaTime);
        }
        if (firingProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(firingProperties);
        }
        if (timeBetweenShots + (spoolingProgressBonus) >= refireTime) {
            firingProperties.expendAmmo();
            timeBetweenShots = 0.0;
            spoolingProgress++;
            spoolingProgressBonus = spoolingProgress * spoolingSpeedStepUp;
            return new Fired(this.firingProperties, this);
        }
        return this;
    }

    private boolean freshMagazine() {
        return timeBetweenShots < 0.0;
    }
}