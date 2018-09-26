package com.bags.simulacrum.Weapon;

public class WeaponStatus {

    private FiringProperties firingProperties;
    private Status status;
    private double reloadProgress;
    private double chargeProgress;
    private double spoolProgress;
    private int currentMagazineSize;
    private FiringStatus firingStatus;

    public WeaponStatus(FiringProperties firingProperties) {
        this.firingProperties = firingProperties;
        this.firingStatus = new Ready(firingProperties);
    }

    //TODO State map?
    public FiringStatus progressTime(double deltaTime) {
        firingStatus = firingStatus.progressTime(deltaTime);
        return firingStatus;
//
//        //SEt it to ready IF enough time has progressed to fire again
//        if (status.equals(Status.FIRED) || status.equals(Status.SPOOLING_FIRED)) {
//            status = Status.READY;
//        }
//
//        if (status.equals(Status.READY) && currentMagazineSize > 0) {
//            if (firingProperties.getTriggerType().equals(FiringProperties.TriggerType.CHARGE)) {
//                chargeProgress = deltaTime;
//                status = Status.CHARGING;
//            } else if (firingProperties.getTriggerType().equals(FiringProperties.TriggerType.AUTOSPOOL)) {
//                spoolProgress = deltaTime;
//                status = Status.SPOOLING_FIRED;
//            } else {
//                currentMagazineSize--;
//                status = Status.FIRED;
//            }
//        } else if (status.equals(Status.READY) && currentMagazineSize <= 0) {
//            reloadProgress = deltaTime;
//            status = Status.RELOADING;
//        } else if (status.equals(Status.CHARGING)) {
//            chargeProgress += deltaTime;
//            if (chargeProgress == firingProperties.getChargeTime()) {
//                currentMagazineSize--;
//                status = Status.FIRED;
//            }
//        } else if (status.equals(Status.RELOADING)) {
//            reloadProgress += deltaTime;
//            if (reloadProgress == firingProperties.getReloadTime()) {
//                currentMagazineSize = firingProperties.getMagazineSize();
//                status = Status.READY;
//            }
//        }
//
//        return status;
    }


    //TODO: Factory class to avoid re-instating these over and over?


    public enum Status {
        RELOADING,
        FIRED,
        CHARGING,
        READY,
        SPOOLING_FIRED
    }

}
