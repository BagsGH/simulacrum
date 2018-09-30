package com.bags.simulacrum.Weapon.Status;

import com.bags.simulacrum.Weapon.FireStatusProperties;
import com.bags.simulacrum.Weapon.TriggerType;

/**
 * This status basically exists as a reset/starting point for each 'clip' fired by the Weapon.
 * This status is entered after reloading or at the creation of a WeaponStatus. Classes using
 * WeaponStatus will be unaware of this status as it's 'skipped' over whenever initiated by the
 * creator of it usually (if not always) immediately calling progressTime on it.
 */
public class Ready implements FiringStatus {
    private FireStatusProperties fireStatusProperties;

    public Ready(FireStatusProperties fireStatusProperties) {
        this.fireStatusProperties = fireStatusProperties;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        FiringStatus status;
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