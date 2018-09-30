package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.Status.FiringStatus;
import com.bags.simulacrum.Weapon.Status.Ready;

public class WeaponStatus {

    private FiringStatus firingStatus;

    public WeaponStatus(FireStatusProperties fireStatusProperties) {
        this.firingStatus = new Ready(fireStatusProperties);
    }

    public FiringStatus progressTime(double deltaTime) {
        firingStatus = firingStatus.progressTime(deltaTime);
        return firingStatus;
    }


}
