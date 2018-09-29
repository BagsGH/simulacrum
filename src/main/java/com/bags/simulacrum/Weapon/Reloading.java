package com.bags.simulacrum.Weapon;

public class Reloading implements FiringStatus {
    private FiringProperties firingProperties;
    private double reloadingProgress;

    public Reloading(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.reloadingProgress = 0.0;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        reloadingProgress += deltaTime;
        if (reloadingProgress >= firingProperties.getReloadTime()) {
            firingProperties.loadMagazine();
            return new Ready(firingProperties).progressTime(deltaTime);
        }
        return this;
    }
}
