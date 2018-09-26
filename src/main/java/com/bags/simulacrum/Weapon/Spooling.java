package com.bags.simulacrum.Weapon;

public class Spooling implements FiringStatus {
    private FiringProperties firingProperties;
    private double spoolingProgress;

    public Spooling(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.spoolingProgress = 0.0;
    }

    @Override
    public FiringStatus progressTime(double deltaTime) {
        spoolingProgress += deltaTime;
        if (spoolingProgress == firingProperties.getReloadTime()) {
            //if charging, return charging
            // if spooling, return spooling
            return new Fired(firingProperties, this); //if it returns this to the caller, the caller knows it just fired
        }

        return this;
    }
}