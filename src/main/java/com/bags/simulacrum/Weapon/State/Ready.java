package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStateProperties;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType;

/**
 * This status basically exists as a reset/starting point for each 'clip' fired by the Weapon.
 * This status is entered after reloading or should be the entry point for the State Machine.
 */
public class Ready implements FiringState {
    private FireStateProperties fireStateProperties;

    public Ready(FireStateProperties fireStateProperties) {
        this.fireStateProperties = fireStateProperties;
    }

    @Override
    public FiringState progressTime(double deltaTime) {
        FiringState status;
        if (fireStateProperties.getTriggerType().equals(TriggerType.CHARGE)) {
            status = new Charging(fireStateProperties);
        } else if (fireStateProperties.getTriggerType().equals(TriggerType.AUTOSPOOL)) {
            status = new Spooling(fireStateProperties);
        } else if (fireStateProperties.getTriggerType().equals(TriggerType.BURST)) {
            status = new Bursting(fireStateProperties);
        } else {
            status = new Auto(fireStateProperties);
        }
        return status.progressTime(deltaTime);
    }
}