package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStateProperties;

public class Fired implements FiringState {
    private FireStateProperties fireStateProperties;
    private FiringState previousStatus;

    public Fired(FireStateProperties fireStateProperties, FiringState previousStatus) {
        this.fireStateProperties = fireStateProperties;
        this.previousStatus = previousStatus;
    }

    @Override
    public FiringState progressTime(double deltaTime) {
        return previousStatus.progressTime(deltaTime);
    }
}