package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FireStatusProperties;

public class Fired implements FiringStatus {
    private FireStatusProperties fireStatusProperties;
    private FiringStatus previousStatus;

    public Fired(FireStatusProperties fireStatusProperties, FiringStatus previousStatus) {
        this.fireStatusProperties = fireStatusProperties;
        this.previousStatus = previousStatus;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        return previousStatus.progressTime(deltaTime);
    }
}