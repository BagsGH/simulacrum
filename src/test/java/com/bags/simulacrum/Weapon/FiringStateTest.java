package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.State.*;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.ChargingProperties;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType;
import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;

public class FiringStateTest {

    private FiringState subject;
    private double fakeDeltaT;
    private double fakeSpoolSlowdownRatio;
    private double fakeAutoFireRate;
    private double fakeBurstFireRate;
    private double fakeChargingTime;
    private DecimalFormat df = new DecimalFormat("0.00");

    @Before
    public void before() {
        fakeDeltaT = 0.01;
        fakeSpoolSlowdownRatio = 2.0;
        fakeAutoFireRate = 15.0;
        fakeBurstFireRate = 7.83;
        fakeChargingTime = 0.25;
    }

    @Test
    public void itDoesNotSlowDownFireRateForAutoWeapons() {
        subject = new Ready(setupAutoWeapon(TriggerType.AUTO, fakeAutoFireRate, 2.5, 200));

        int autoTime = 0;
        int firedCount = 0;

        for (int i = 0; i < 8; i++) {
            subject = subject.progressTime(fakeDeltaT);

            if (subject instanceof Auto) {
                autoTime++;
            }
            if (subject instanceof Fired) {
                firedCount++;
            }
        }

        int expectedAutoTime = (int) Math.floor((1 / fakeAutoFireRate) / fakeDeltaT);

        assertEquals(expectedAutoTime, autoTime);
        assertEquals(2, firedCount);
    }

    @Test
    public void weaponStatusIsFiredImmediatelyAfterReloadForAuto() {
        subject = new Ready(setupAutoWeapon(TriggerType.AUTO, fakeAutoFireRate, 0.01, 1));

        int autoCount = 0;
        int firedCount = 0;
        int reloadCount = 0;

        for (int i = 0; i < 3; i++) {
            subject = subject.progressTime(fakeDeltaT);
            System.out.println(subject.getClass());

            if (subject instanceof Auto) {
                autoCount++;
            }
            if (subject instanceof Fired) {
                firedCount++;
            }

            if (subject instanceof Reloading) {
                reloadCount++;
            }
        }

        assertEquals(0, autoCount);
        assertEquals(1, reloadCount);
        assertEquals(2, firedCount);
    }

    @Test
    public void itSlowsDownFireRateForSpoolingWeapons() {
        subject = new Ready(setupSpoolWeapon(TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 200, 5, 1.0));

        int spoolTime = 0;
        int firedCount = 0;

        for (int i = 0; i < 15; i++) {
            subject = subject.progressTime(fakeDeltaT);

            if (subject instanceof Spooling) {
                spoolTime++;
            }
            if (subject instanceof Fired) {
                firedCount++;
            }
        }

        int expectedSpoolingTime = (int) Math.floor((1 / fakeAutoFireRate * fakeSpoolSlowdownRatio) / fakeDeltaT);

        assertEquals(expectedSpoolingTime, spoolTime);
        assertEquals(2, firedCount);
    }

    @Test
    public void itReloadsSpoolWeapons() {
        subject = new Ready(setupSpoolWeapon(TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 1, 5, 1.0));

        int reloadTime = 0;
        int firedCount = 0;

        for (int i = 0; i < 2; i++) {
            subject = subject.progressTime(fakeDeltaT);
            System.out.println(subject.getClass());

            if (subject instanceof Reloading) {
                reloadTime++;
            }
            if (subject instanceof Fired) {
                firedCount++;
            }
        }

        assertEquals(1, reloadTime);
        assertEquals(1, firedCount);
    }

    @Test
    public void itChangesFromSpoolingToAutoAfterSpoolingThresholdMet() {
        subject = new Ready(setupSpoolWeapon(TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 200, 1, 1.0));

        int spoolTime = 0;
        int firedCount = 0;
        int autoCount = 0;

        for (int i = 0; i < 16; i++) {
            subject = subject.progressTime(fakeDeltaT);
            if (subject instanceof Spooling) {
                spoolTime++;
            }
            if (subject instanceof Fired) {
                firedCount++;
            }

            if (subject instanceof Auto) {
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
        subject = new Ready(setupBurstWeapon(TriggerType.BURST, fakeBurstFireRate, 2.5, 45, 3));

        int burstTime = 0;
        int firedCount = 0;

        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < 93; i++) {
            subject = subject.progressTime(fakeDeltaT);

            if (subject instanceof Bursting) {
                burstTime++;
            }
            if (subject instanceof Fired) {
                firedCount++;
            }
        }

        assertEquals(9, firedCount);
        assertEquals(84, burstTime);
    }

    @Test
    public void itChargesFiresAndThenReloadsABow() {
        subject = new Ready(setupChargingBowWeapon(TriggerType.CHARGE, 0.25, 1, fakeChargingTime));

        int chargingTime = 0;
        int firedCount = 0;
        int reloadTime = 0;

        double time = 0.0;
        for (int i = 0; i < 50; i++) {
            subject = subject.progressTime(fakeDeltaT);

            time += 0.01;
            System.out.println(df.format(time) + " " + subject.getClass());

            if (subject instanceof Charging) {
                chargingTime++;
            }
            if (subject instanceof Fired) {
                firedCount++;
            }
            if (subject instanceof Reloading) {
                reloadTime++;
            }
        }

        assertEquals(1, firedCount);
        assertEquals((int) (fakeChargingTime * 100) - 1, chargingTime);
        assertEquals(25, reloadTime);
    }

//    @Test
//    public void test() {
//        subject = new State(setupSpoolWeapon(FireStateProperties.com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 200, 8, 1.0));
//
//        DecimalFormat df = new DecimalFormat("0.00");
//
//        double runningTime = 0.0;
//        long startTime = System.currentTimeMillis();
//        //for (int j = 0; j < 30000; j++) {
//        for (int i = 0; i < 6000; i++) {
//            subject = subject.firingStateProgressTime(0.01);
//            runningTime += 0.01;
//            System.out.println("Current time: " + df.format(runningTime) + " Current status: " + status.getClass());
//        }
//        //}
//
//        // System.out.println(System.currentTimeMillis() - startTime);
//
//    }

    private FireStateProperties setupSpoolWeapon(TriggerType triggerType, double fireRate, double reloadTime, int magazineSize, int spoolThreshold, double spoolingDecrease) {
        FireStateProperties props = new FireStateProperties.FireStatePropertiesBuilder(triggerType, reloadTime, magazineSize, 200)
                .withFireRate(fireRate)
                .withSpoolThreshold(spoolThreshold)
                .withSpoolingSpeedDecreaseModifier(spoolingDecrease)
                .build();

        return props;
    }

    private FireStateProperties setupAutoWeapon(TriggerType triggerType, double fireRate, double reloadTime, int magazineSize) {
        FireStateProperties props = new FireStateProperties.FireStatePropertiesBuilder(triggerType, reloadTime, magazineSize, 200)
                .withFireRate(fireRate)
                .build();

        return props;
    }

    private FireStateProperties setupBurstWeapon(TriggerType triggerType, double fireRate, double reloadTime, int magazineSize, int burstCount) {
        FireStateProperties props = new FireStateProperties.FireStatePropertiesBuilder(triggerType, reloadTime, magazineSize, 200)
                .withFireRate(fireRate)
                .withBurstCount(burstCount)
                .build();

        return props;
    }

    private FireStateProperties setupChargingBowWeapon(TriggerType triggerType, double reloadTime, int magazineSize, double chargeTime) {
        FireStateProperties props = new FireStateProperties.FireStatePropertiesBuilder(triggerType, reloadTime, magazineSize, 200)
                .withChargingProperties(new ChargingProperties(chargeTime, 0.0, 0.0, 0.0))
                .build();

        return props;
    }

}