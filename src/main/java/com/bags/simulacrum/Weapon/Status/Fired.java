package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FiringProperties;

public class Fired implements FiringStatus {
    private FiringProperties firingProperties;
    private FiringStatus previousStatus;

    public Fired(FiringProperties firingProperties, FiringStatus previousStatus) {
        this.firingProperties = firingProperties;
        this.previousStatus = previousStatus;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        return previousStatus.progressTime(deltaTime);
    }
}