package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FireStatusProperties;

public class Bursting implements FiringStatus {
    private FireStatusProperties fireStatusProperties;
    private double interBurstFireTime;
    private double timeBetweenBursts;
    private double timeBetweenShots;
    private double intraBurstFireTime;
    private int burstShotProgress;
    private int burstCount;


    public Bursting(FireStatusProperties fireStatusProperties) {
        this.fireStatusProperties = fireStatusProperties;
        this.burstCount = fireStatusProperties.getBurstCount();
        this.intraBurstFireTime = ((1.25 / this.fireStatusProperties.getFireRate()) / this.burstCount);
        this.interBurstFireTime = (1.075 / this.fireStatusProperties.getFireRate()) * 2;
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
            return new Fired(this.fireStatusProperties, this);
        }
        if (freshMagazine()) {
            handleFirstShot();
            return new Fired(this.fireStatusProperties, this);
        }
        if (timeBetweenBursts >= interBurstFireTime) {
            burstShotProgress = 1;
            timeBetweenBursts = 0.0;
            return new Fired(this.fireStatusProperties, this);
        }

        return this;
    }

    private void handleBurstFire() {
        fireStatusProperties.expendAmmo();
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
        fireStatusProperties.expendAmmo();
        burstShotProgress = 1;
    }

    private boolean freshMagazine() {
        return timeBetweenBursts < 0.0;
    }
}