package com.bags.simulacrum.Weapon;

public class Bursting implements FiringStatus {
    private FiringProperties firingProperties;
    private double interBurstFireTime;
    private double timeBetweenBursts;
    private double timeBetweenShots;
    private double intraBurstFireTime;
    private int burstShotProgress;
    private int burstCount;


    public Bursting(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.burstCount = firingProperties.getBurstCount();
        this.intraBurstFireTime = ((1.25 / this.firingProperties.getFireRate()) / this.burstCount);
        this.interBurstFireTime = (1.075 / this.firingProperties.getFireRate()) * 2;
        this.timeBetweenBursts = -1.0;
        this.timeBetweenShots = 0.0;
        this.burstShotProgress = -1;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        if (burstShotProgress > 0) {
            timeBetweenShots += deltaTime;
        } else {
            timeBetweenBursts += deltaTime;
        }
        if (timeBetweenShots >= intraBurstFireTime) {
            handleBurstFire();
            return new Fired(this.firingProperties, this);
        }
        if (freshMagazine()) {
            handleFirstShot();
            return new Fired(this.firingProperties, this);
        }
        if (timeBetweenBursts >= interBurstFireTime) {
            burstShotProgress = 1;
            timeBetweenBursts = 0.0;
            return new Fired(this.firingProperties, this);
        }

        return this;
    }

    private void handleBurstFire() {
        firingProperties.expendAmmo();
        burstShotProgress++;
        timeBetweenShots = 0.0;
        if (burstShotProgress == burstCount) {
            timeBetweenBursts = 0.0;
            burstShotProgress = 0;
        }
    }

    private void handleFirstShot() {
        timeBetweenBursts = 0.0;
        timeBetweenShots = 0.0;
        firingProperties.expendAmmo();
        burstShotProgress = 1;
    }

    private boolean freshMagazine() {
        return timeBetweenBursts < 0.0;
    }
}