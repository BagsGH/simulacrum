package com.bags.simulacrum.Weapon;

public class Spooling implements FiringStatus {
    private FiringProperties firingProperties;
    private double refireTime;
    private double timeBetweenShots;
    private int spoolThreshold;
    private int spoolingProgress;

    public Spooling(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.refireTime = (1 / firingProperties.getFireRate()) * 2;
        this.timeBetweenShots = -1.0;
        this.spoolThreshold = firingProperties.getSpoolThreshold();
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        if (freshMagazine()) {
            timeBetweenShots = 0.0;
            spoolingProgress++;
            firingProperties.expendAmmo();
            return new Fired(this.firingProperties, this);
        }
        if (spoolingProgress >= spoolThreshold) {
            FiringStatus status = new Auto(this.firingProperties, timeBetweenShots);
            return status.progressTime(deltaTime);// maybe also immediately step up time? idk
        }
        if (firingProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(firingProperties);
        }
        if (timeBetweenShots >= refireTime) {
            firingProperties.expendAmmo();
            timeBetweenShots = 0.0;
            spoolingProgress++;
            return new Fired(this.firingProperties, this);
        }
        if (timeBetweenShots < refireTime) {
            timeBetweenShots += deltaTime;
        }
        return this;
    }

    private boolean freshMagazine() {
        return timeBetweenShots < 0.0;
    }
}