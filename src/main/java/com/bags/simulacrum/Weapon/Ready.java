package com.bags.simulacrum.Weapon;

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