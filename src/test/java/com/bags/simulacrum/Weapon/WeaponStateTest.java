package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.State.*;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType;
import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;

public class WeaponStateTest {

    private WeaponState subject;
    private double fakeDeltaT;
    private double fakeSpoolSlowdownRatio;
    private double fakeAutoFireRate;
    private double fakeBurstFireRate;
    private double fakeChargingTime;

    @Before
    public void before() {
        FireStatusProperties fakeFireStatusProperties = new FireStatusProperties();
        subject = new WeaponState(fakeFireStatusProperties);
        fakeDeltaT = 0.01;
        fakeSpoolSlowdownRatio = 2.0;
        fakeAutoFireRate = 15.0;
        fakeBurstFireRate = 7.83;
        fakeChargingTime = 0.25;
    }

    @Test
    public void itDoesNotSlowDownFireRateForAutoWeapons() {
        subject = new WeaponState(setupAutoWeapon(TriggerType.AUTO, fakeAutoFireRate, 2.5, 200));

        int autoTime = 0;
        int firedCount = 0;

        for (int i = 0; i < 8; i++) {
            FiringState status = subject.progressTime(fakeDeltaT);

            if (status instanceof Auto) {
                autoTime++;
            }
            if (status instanceof Fired) {
                firedCount++;
            }
        }

        int expectedAutoTime = (int) Math.floor((1 / fakeAutoFireRate) / fakeDeltaT);

        assertEquals(expectedAutoTime, autoTime);
        assertEquals(2, firedCount);
    }

    @Test
    public void weaponStatusIsFiredImmediatelyAfterReloadForAuto() {
        subject = new WeaponState(setupAutoWeapon(TriggerType.AUTO, fakeAutoFireRate, 0.01, 1));

        int autoCount = 0;
        int firedCount = 0;
        int reloadCount = 0;

        for (int i = 0; i < 3; i++) {
            FiringState status = subject.progressTime(fakeDeltaT);

            if (status instanceof Auto) {
                autoCount++;
            }
            if (status instanceof Fired) {
                firedCount++;
            }

            if (status instanceof Reloading) {
                reloadCount++;
            }
        }

        assertEquals(0, autoCount);
        assertEquals(1, reloadCount);
        assertEquals(2, firedCount);
    }

    @Test
    public void itSlowsDownFireRateForSpoolingWeapons() {
        subject = new WeaponState(setupSpoolWeapon(TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 200, 5, 1.0));

        int spoolTime = 0;
        int firedCount = 0;

        for (int i = 0; i < 15; i++) {
            FiringState status = subject.progressTime(fakeDeltaT);

            if (status instanceof Spooling) {
                spoolTime++;
            }
            if (status instanceof Fired) {
                firedCount++;
            }
        }

        int expectedSpoolingTime = (int) Math.floor((1 / fakeAutoFireRate * fakeSpoolSlowdownRatio) / fakeDeltaT);

        assertEquals(expectedSpoolingTime, spoolTime);
        assertEquals(2, firedCount);
    }

    @Test
    public void itReloadsSpoolWeapons() {
        subject = new WeaponState(setupSpoolWeapon(TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 1, 5, 1.0));

        int reloadTime = 0;
        int firedCount = 0;

        for (int i = 0; i < 2; i++) {
            FiringState status = subject.progressTime(fakeDeltaT);

            if (status instanceof Reloading) {
                reloadTime++;
            }
            if (status instanceof Fired) {
                firedCount++;
            }
        }

        assertEquals(1, reloadTime);
        assertEquals(1, firedCount);
    }

    @Test
    public void itChangesFromSpoolingToAutoAfterSpoolingThresholdMet() {
        subject = new WeaponState(setupSpoolWeapon(TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 200, 1, 1.0));

        int spoolTime = 0;
        int firedCount = 0;
        int autoCount = 0;

        for (int i = 0; i < 16; i++) {
            FiringState status = subject.progressTime(fakeDeltaT);
            if (status instanceof Spooling) {
                spoolTime++;
            }
            if (status instanceof Fired) {
                firedCount++;
            }

            if (status instanceof Auto) {
                autoCount++;
            }
        }

        int expectedSpoolingTime = (int) Math.floor((1 / fakeAutoFireRate * fakeSpoolSlowdownRatio) / fakeDeltaT);

        assertEquals(expectedSpoolingTime, spoolTime);
        assertEquals(2, firedCount);
        assertEquals(1, autoCount);
    }

    @Test
    public void burstFiresInBursts() {
        subject = new WeaponState(setupBurstWeapon(TriggerType.BURST, fakeBurstFireRate, 2.5, 45, 3));

        int burstTime = 0;
        int firedCount = 0;

        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < 93; i++) {
            FiringState status = subject.progressTime(fakeDeltaT);

            if (status instanceof Bursting) {
                burstTime++;
            }
            if (status instanceof Fired) {
                firedCount++;
            }
        }

        assertEquals(9, firedCount);
        assertEquals(84, burstTime);
    }

    @Test
    public void itChargesFiresAndThenReloadsABow() {
        subject = new WeaponState(setupChargingWeapon(TriggerType.CHARGE, 0.25, 1, fakeChargingTime));

        int chargingTime = 0;
        int firedCount = 0;
        int reloadTime = 0;

        for (int i = 0; i < 50; i++) {
            FiringState status = subject.progressTime(fakeDeltaT);


            if (status instanceof Charging) {
                chargingTime++;
            }
            if (status instanceof Fired) {
                firedCount++;
            }
            if (status instanceof Reloading) {
                reloadTime++;
            }
        }

        assertEquals(1, firedCount);
        assertEquals((int) (fakeChargingTime * 100) - 1, chargingTime);
        assertEquals(25, reloadTime);
    }

//    @Test
//    public void test() {
//        subject = new State(setupSpoolWeapon(FireStatusProperties.com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 200, 8, 1.0));
//
//        DecimalFormat df = new DecimalFormat("0.00");
//
//        double runningTime = 0.0;
//        long startTime = System.currentTimeMillis();
//        //for (int j = 0; j < 30000; j++) {
//        for (int i = 0; i < 6000; i++) {
//            FiringState status = subject.progressTime(0.01);
//            runningTime += 0.01;
//            System.out.println("Current time: " + df.format(runningTime) + " Current status: " + status.getClass());
//        }
//        //}
//
//        // System.out.println(System.currentTimeMillis() - startTime);
//
//    }

    private FireStatusProperties setupSpoolWeapon(TriggerType triggerType, double fireRate, double reloadTime, int magazineSize, int spoolThreshold, double spoolingDecrease) {
        FireStatusProperties props = new FireStatusProperties();
        props.setFireRate(fireRate);
        props.setReloadTime(reloadTime);
        props.setMagazineSize(magazineSize);
        props.setSpoolThreshold(spoolThreshold);
        props.setTriggerType(triggerType);
        props.loadMagazine();
        props.setSpoolingSpeedDecreaseModifier(spoolingDecrease);

        return props;
    }

    private FireStatusProperties setupAutoWeapon(TriggerType triggerType, double fireRate, double reloadTime, int magazineSize) {
        FireStatusProperties props = new FireStatusProperties();
        props.setFireRate(fireRate);
        props.setReloadTime(reloadTime);
        props.setMagazineSize(magazineSize);
        props.setTriggerType(triggerType);
        props.loadMagazine();

        return props;
    }

    private FireStatusProperties setupBurstWeapon(TriggerType triggerType, double fireRate, double reloadTime, int magazineSize, int burstCount) {
        FireStatusProperties props = new FireStatusProperties();
        props.setFireRate(fireRate);
        props.setReloadTime(reloadTime);
        props.setMagazineSize(magazineSize);
        props.setTriggerType(triggerType);
        props.setBurstCount(burstCount);
        props.loadMagazine();

        return props;
    }

    private FireStatusProperties setupChargingWeapon(TriggerType triggerType, double reloadTime, int magazineSize, double chargeTime) {
        FireStatusProperties props = new FireStatusProperties();
        props.setReloadTime(reloadTime);
        props.setMagazineSize(magazineSize);
        props.setTriggerType(triggerType);
        props.setChargeTime(chargeTime);
        props.loadMagazine();

        return props;
    }

}