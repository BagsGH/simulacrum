package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStateProperties;

public class OutOfAmmo implements FiringState {
    private FireStateProperties fireStateProperties;

    public OutOfAmmo(FireStateProperties fireStateProperties) {
        this.fireStateProperties = fireStateProperties;
    }

    @Override
    public FiringState progressTime(double deltaTime) {

        return this;
    }
}