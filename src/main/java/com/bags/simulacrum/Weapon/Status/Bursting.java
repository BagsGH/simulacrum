package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FireStatusProperties;

public class Bursting implements FiringStatus {
    private FireStatusProperties fireStatusProperties;
    private double timeBetweenBursts;
    private double timeBetweenShots;
    private int burstShotProgress;
    private int burstCount;


    public Bursting(FireStatusProperties fireStatusProperties) {
        this.fireStatusProperties = fireStatusProperties;
        this.burstCount = fireStatusProperties.getBurstCount();
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
        if (timeBetweenShots >= getIntraBurstFireTime()) {
            handleBurstFire();
            return new Fired(this.fireStatusProperties, this);
        }
        if (freshMagazine()) {
            handleFirstShot();
            return new Fired(this.fireStatusProperties, this);
        }
        if (timeBetweenBursts >= getInterBurstFireTime()) {
            burstShotProgress = 1;
            timeBetweenBursts = 0.0;
            return new Fired(this.fireStatusProperties, this);
        }

        return this;
    }

    private double getIntraBurstFireTime() {
        return (1.25 / this.fireStatusProperties.getFireRate()) / this.burstCount;
    }

    private double getInterBurstFireTime() {
        return (1.075 / this.fireStatusProperties.getFireRate()) * 2;
    }

    private void handleBurstFire() {
        fireStatusProperties.subtractAmmo();
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
        fireStatusProperties.subtractAmmo();
        burstShotProgress = 1;
    }

    private boolean freshMagazine() {
        return timeBetweenBursts < 0.0;
    }
}