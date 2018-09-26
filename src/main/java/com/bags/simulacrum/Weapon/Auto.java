package com.bags.simulacrum.Weapon;

public class Auto implements FiringStatus {
    private FiringProperties firingProperties;
    private double refireTime;
    private double timeBetweenShots;

    public Auto(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.refireTime = 1 / firingProperties.getFireRate();
        this.timeBetweenShots = -1.0;
    }

    public Auto(FiringProperties firingProperties, double timeBetweenShots) {
        this.firingProperties = firingProperties;
        this.refireTime = 1 / firingProperties.getFireRate();
        this.timeBetweenShots = timeBetweenShots;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        if (freshMagazine()) {
            timeBetweenShots = 0.0;
            firingProperties.expendAmmo();
            return new Fired(this.firingProperties, this);
        }
        if (firingProperties.getCurrentMagazineSize() <= 0) {
            return new Reloading(firingProperties);
        }
        if (timeBetweenShots >= refireTime) {
            firingProperties.expendAmmo();
            timeBetweenShots = 0.0;
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