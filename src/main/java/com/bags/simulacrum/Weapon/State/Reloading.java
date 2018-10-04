package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStateProperties;

public class Reloading implements FiringState {
    private FireStateProperties fireStateProperties;
    private double reloadingProgress;

    public Reloading(FireStateProperties fireStateProperties) {
        this.fireStateProperties = fireStateProperties;
        this.reloadingProgress = 0.0;
    }

    @Override
    public FiringState progressTime(double deltaTime) {
        if (this.fireStateProperties.getCurrentAmmo() <= 0) {
            return new OutOfAmmo(this.fireStateProperties);
        }
        reloadingProgress += deltaTime;
        if (reloadingProgress >= fireStateProperties.getReloadTime()) {
            fireStateProperties.loadMagazine();
            return new Ready(fireStateProperties).progressTime(deltaTime);
        }
        return this;
    }
}
