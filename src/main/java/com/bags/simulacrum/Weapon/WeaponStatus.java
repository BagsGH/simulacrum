package com.bags.simulacrum.Weapon;

public class WeaponStatus {

    private FiringStatus firingStatus;

    public WeaponStatus(FiringProperties firingProperties) {
        this.firingStatus = new Ready(firingProperties);
    }

    public FiringStatus progressTime(double deltaTime) {
        firingStatus = firingStatus.progressTime(deltaTime);
        return firingStatus;
    }


}
