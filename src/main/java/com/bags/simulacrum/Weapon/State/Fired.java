package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStatusProperties;

public class Fired implements FiringState {
    private FireStatusProperties fireStatusProperties;
    private FiringState previousStatus;

    public Fired(FireStatusProperties fireStatusProperties, FiringState previousStatus) {
        this.fireStatusProperties = fireStatusProperties;
        this.previousStatus = previousStatus;
    }

    @Override
    public FiringState progressTime(double deltaTime) {
        return previousStatus.progressTime(deltaTime);
    }
}