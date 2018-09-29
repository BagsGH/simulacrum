package com.bags.simulacrum.Weapon;

/**
 * This status basically exists as a reset/starting point for each 'clip' fired by the Weapon.
 * This status is entered after reloading or at the creation of a WeaponStatus. Classes using
 * WeaponStatus will be unaware of this status as it's 'skipped' over whenever initiated by the
 * creator of it usually (if not always) immediately calling progressTime on it.
 */
public class Ready implements FiringStatus {
    private FiringProperties firingProperties;

    public Ready(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        FiringStatus status;
        if (firingProperties.getTriggerType().equals(FiringProperties.TriggerType.CHARGE)) {
            status = new Charging(firingProperties);
        } else if (firingProperties.getTriggerType().equals(FiringProperties.TriggerType.AUTOSPOOL)) {
            status = new Spooling(firingProperties);
        } else if (firingProperties.getTriggerType().equals(FiringProperties.TriggerType.BURST)) {
            status = new Bursting(firingProperties);
        } else {
            status = new Auto(firingProperties);
        }
        return status.progressTime(deltaTime);
    }
}