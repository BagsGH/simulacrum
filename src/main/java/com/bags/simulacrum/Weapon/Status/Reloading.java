package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FireStatusProperties;

public class Reloading implements FiringStatus {
    private FireStatusProperties fireStatusProperties;
    private double reloadingProgress;

    public Reloading(FireStatusProperties fireStatusProperties) {
        this.fireStatusProperties = fireStatusProperties;
        this.reloadingProgress = 0.0;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        reloadingProgress += deltaTime;
        if (reloadingProgress >= fireStatusProperties.getReloadTime()) {
            fireStatusProperties.loadMagazine();
            return new Ready(fireStatusProperties).progressTime(deltaTime);
        }
        return this;
    }
}
