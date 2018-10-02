package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStateProperties;

public class Bursting implements FiringState {
    private FireStateProperties fireStateProperties;
    private double timeBetweenBursts;
    private double timeBetweenShots;
    private int burstShotProgress;
    private int burstCount;


    public Bursting(FireStateProperties fireStateProperties) {
        this.fireStateProperties = fireStateProperties;
        this.burstCount = fireStateProperties.getBurstCount();
        this.timeBetweenBursts = -1.0;
        this.timeBetweenShots = 0.0;
        this.burstShotProgress = -1;
    }

    @Override
    public FiringState progressTime(double deltaTime) {
        if (burstShotProgress > 0) {
            timeBetweenShots += deltaTime;
        } else {
            timeBetweenBursts += deltaTime;
        }
        if (timeBetweenShots >= getIntraBurstFireTime()) {
            handleBurstFire();
            return new Fired(this.fireStateProperties, this);
        }
        if (freshMagazine()) {
            handleFirstShot();
            return new Fired(this.fireStateProperties, this);
        }
        if (timeBetweenBursts >= getInterBurstFireTime()) {
            burstShotProgress = 1;
            timeBetweenBursts = 0.0;
            return new Fired(this.fireStateProperties, this);
        }

        return this;
    }

    private double getIntraBurstFireTime() {
        return (1.25 / this.fireStateProperties.getFireRate()) / this.burstCount;
    }

    private double getInterBurstFireTime() {
        return (1.075 / this.fireStateProperties.getFireRate()) * 2;
    }

    private void handleBurstFire() {
        fireStateProperties.subtractAmmo();
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
        fireStateProperties.subtractAmmo();
        burstShotProgress = 1;
    }

    private boolean freshMagazine() {
        return timeBetweenBursts < 0.0;
    }
}