package com.bags.simulacrum.Weapon;

public class Bursting implements FiringStatus {
    private FiringProperties firingProperties;
    private double reBurstTime;
    private double timeBetweenBursts;
    private double timeBetweenShots;
    private double burstFireTime;
    private int burstShotProgress;
    private int burstCount;


    public Bursting(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.reBurstTime = (1 / firingProperties.getFireRate());
        this.burstFireTime = (1 / firingProperties.getBurstFireRate());
        this.timeBetweenBursts = -1.0;
        this.timeBetweenShots = 0.0;
        this.burstShotProgress = -1;
        this.burstCount = firingProperties.getBurstCount();
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        if (burstShotProgress > 0) {
            timeBetweenShots += deltaTime;
        } else {
            timeBetweenBursts += deltaTime;
        }

        if (timeBetweenShots >= burstFireTime) {
            handleBurstFire();
            return new Fired(this.firingProperties, this);
        }
        if (freshMagazine()) {
            handleFirstShot();
            return new Fired(this.firingProperties, this);
        }
        if (timeBetweenBursts >= reBurstTime) {
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