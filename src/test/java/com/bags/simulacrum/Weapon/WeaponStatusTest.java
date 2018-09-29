package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.Status.*;
import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;

public class WeaponStatusTest {

    private WeaponStatus subject;
    private FiringProperties fakeFiringProperties;
    private double fakeDeltaT;
    private double fakeSpoolSlowdownRatio;
    private double fakeAutoFireRate;
    private double fakeBurstFireRate;

    @Before
    public void before() {
        fakeFiringProperties = new FiringProperties();
        subject = new WeaponStatus(fakeFiringProperties);
        fakeDeltaT = 0.01;
        fakeSpoolSlowdownRatio = 2.0;
        fakeAutoFireRate = 15.0;
        fakeBurstFireRate = 7.83;
    }

    @Test
    public void itDoesNotSlowDownFireRateForAutoWeapons() {
        subject = new WeaponStatus(setupAutoWeapon(FiringProperties.TriggerType.AUTO, fakeAutoFireRate, 2.5, 200));

        int autoTime = 0;
        int firedCount = 0;

        for (int i = 0; i < 8; i++) {
            FiringStatus status = subject.progressTime(fakeDeltaT);

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
    public void weaponStatusIsFiredImmediatelyAfterReload() {
        subject = new WeaponStatus(setupAutoWeapon(FiringProperties.TriggerType.AUTO, fakeAutoFireRate, 0.01, 1));

        int autoCount = 0;
        int firedCount = 0;
        int reloadCount = 0;

        for (int i = 0; i < 3; i++) {
            FiringStatus status = subject.progressTime(fakeDeltaT);

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
        subject = new WeaponStatus(setupSpoolWeapon(FiringProperties.TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 200, 5, 1.0));

        int spoolTime = 0;
        int firedCount = 0;

        for (int i = 0; i < 15; i++) {
            FiringStatus status = subject.progressTime(fakeDeltaT);

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
    public void burstFiresInBursts() {
        subject = new WeaponStatus(setupBurstWeapon(FiringProperties.TriggerType.BURST, fakeBurstFireRate, 2.5, 45, 3));

        int burstTime = 0;
        int firedCount = 0;

        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < 93; i++) {
            FiringStatus status = subject.progressTime(fakeDeltaT);

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

//    @Test
//    public void test() {
//        subject = new Status(setupSpoolWeapon(FiringProperties.TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 200, 8, 1.0));
//
//        DecimalFormat df = new DecimalFormat("0.00");
//
//        double runningTime = 0.0;
//        long startTime = System.currentTimeMillis();
//        //for (int j = 0; j < 30000; j++) {
//        for (int i = 0; i < 6000; i++) {
//            FiringStatus status = subject.progressTime(0.01);
//            runningTime += 0.01;
//            System.out.println("Current time: " + df.format(runningTime) + " Current status: " + status.getClass());
//        }
//        //}
//
//        // System.out.println(System.currentTimeMillis() - startTime);
//
//    }

    private FiringProperties setupSpoolWeapon(FiringProperties.TriggerType triggerType, double fireRate, double reloadTime, int magazineSize, int spoolThreshold, double spoolingDecrease) {
        FiringProperties props = new FiringProperties();
        props.setFireRate(fireRate);
        props.setReloadTime(reloadTime);
        props.setMagazineSize(magazineSize);
        props.setSpoolThreshold(spoolThreshold);
        props.setTriggerType(triggerType);
        props.loadMagazine();
        props.setSpoolingSpeedDecreaseModifier(spoolingDecrease);

        return props;
    }

    private FiringProperties setupAutoWeapon(FiringProperties.TriggerType triggerType, double fireRate, double reloadTime, int magazineSize) {
        FiringProperties props = new FiringProperties();
        props.setFireRate(fireRate);
        props.setReloadTime(reloadTime);
        props.setMagazineSize(magazineSize);
        props.setTriggerType(triggerType);
        props.loadMagazine();

        return props;
    }

    private FiringProperties setupBurstWeapon(FiringProperties.TriggerType triggerType, double fireRate, double reloadTime, int magazineSize, int burstCount) {
        FiringProperties props = new FiringProperties();
        props.setFireRate(fireRate);
        props.setReloadTime(reloadTime);
        props.setMagazineSize(magazineSize);
        props.setTriggerType(triggerType);
        props.setBurstCount(burstCount);
        props.loadMagazine();

        return props;
    }

}