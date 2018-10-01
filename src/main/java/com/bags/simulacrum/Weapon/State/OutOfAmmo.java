package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStatusProperties;

public class OutOfAmmo implements FiringState {
    private FireStatusProperties fireStatusProperties;

    public OutOfAmmo(FireStatusProperties fireStatusProperties) {
        this.fireStatusProperties = fireStatusProperties;
    }

    @Override
    public FiringState progressTime(double deltaTime) {

        return this;
    }
}