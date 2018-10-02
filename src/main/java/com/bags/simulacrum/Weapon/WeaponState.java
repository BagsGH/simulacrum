package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.State.FiringState;
import com.bags.simulacrum.Weapon.State.Ready;

public class WeaponState {

    private FiringState firingState;

    public WeaponState(FireStateProperties fireStateProperties) {
        this.firingState = new Ready(fireStateProperties);
    }

    public FiringState progressTime(double deltaTime) {
        firingState = firingState.progressTime(deltaTime);
        return firingState;
    }


}
