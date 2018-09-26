package com.bags.simulacrum.Weapon;

public class Charging implements FiringStatus {
    private FiringProperties firingProperties;
    private double chargingProgress;

    public Charging(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.chargingProgress = 0.0;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        chargingProgress += deltaTime;
        if (chargingProgress == firingProperties.getReloadTime()) {
            //if charging, return charging
            // if spooling, return spooling
            return new Fired(firingProperties, this); //if it returns this to the caller, the caller knows it just fired
        }

        return this;
    }
}