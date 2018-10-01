package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStatusProperties;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType;

/**
 * This status basically exists as a reset/starting point for each 'clip' fired by the Weapon.
 * This status is entered after reloading or at the creation of a WeaponState. Classes using
 * WeaponState will be unaware of this status as it's 'skipped' over whenever initiated by the
 * creator of it usually (if not always) immediately calling progressTime on it.
 */
public class Ready implements FiringState {
    private FireStatusProperties fireStatusProperties;

    public Ready(FireStatusProperties fireStatusProperties) {
        this.fireStatusProperties = fireStatusProperties;
    }

    @Override
    public FiringState progressTime(double deltaTime) {
        FiringState status;
        if (fireStatusProperties.getTriggerType().equals(TriggerType.CHARGE)) {
            status = new Charging(fireStatusProperties);
        } else if (fireStatusProperties.getTriggerType().equals(TriggerType.AUTOSPOOL)) {
            status = new Spooling(fireStatusProperties);
        } else if (fireStatusProperties.getTriggerType().equals(TriggerType.BURST)) {
            status = new Bursting(fireStatusProperties);
        } else {
            status = new Auto(fireStatusProperties);
        }
        return status.progressTime(deltaTime);
    }
}