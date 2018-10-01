package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.State.FiringState;
import com.bags.simulacrum.Weapon.State.Ready;

public class WeaponState {

    private FiringState firingState;

    public WeaponState(FireStatusProperties fireStatusProperties) {
        this.firingState = new Ready(fireStatusProperties);
    }

    public FiringState progressTime(double deltaTime) {
        firingState = firingState.progressTime(deltaTime);
        return firingState;
    }


}
