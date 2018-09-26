package com.bags.simulacrum.Weapon;

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

    @Before
    public void before() {
        fakeFiringProperties = new FiringProperties();
        subject = new WeaponStatus(fakeFiringProperties);
        fakeDeltaT = 0.01;
        fakeSpoolSlowdownRatio = 2.0;
        fakeAutoFireRate = 15.0;
    }

    @Test
    public void itDoesNotSlowDownFireRateForAutoWeapons() {
        subject = new WeaponStatus(setupFiringProperties(FiringProperties.TriggerType.AUTO, fakeAutoFireRate, 2.5, 200, 0.0, 5));

        int autoTime = 0;
        int firedCount = 0;

        for (int i = 0; i < 9; i++) {
            FiringStatus status = subject.progressTime(fakeDeltaT);

            System.out.println(status.getClass()); //TODO: is it doing one too many auto / spools?

            if (status instanceof Auto) {
                autoTime++;
            }
            if (status instanceof Fired) {
                firedCount++;
            }
        }

        int expectedAutoTime = (int) Math.ceil((1 / fakeAutoFireRate) / fakeDeltaT);

        assertEquals(expectedAutoTime, autoTime);
        assertEquals(2, firedCount);
    }

    @Test
    public void itSlowsDownFireRateForSpoolingWeapons() {
        subject = new WeaponStatus(setupFiringProperties(FiringProperties.TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 200, 0.0, 5));

        int spoolTime = 0;
        int firedCount = 0;

        for (int i = 0; i < 16; i++) {
            FiringStatus status = subject.progressTime(fakeDeltaT);
            if (status instanceof Spooling) {
                spoolTime++;
            }
            if (status instanceof Fired) {
                firedCount++;
            }
        }

        int expectedSpoolingTime = (int) Math.ceil((1 / fakeAutoFireRate * fakeSpoolSlowdownRatio) / fakeDeltaT);

        assertEquals(expectedSpoolingTime, spoolTime);
        assertEquals(2, firedCount);
    }

    @Test
    public void test() {
        subject = new WeaponStatus(setupFiringProperties(FiringProperties.TriggerType.AUTOSPOOL, fakeAutoFireRate, 2.5, 200, 0.0, 8));

        DecimalFormat df = new DecimalFormat("0.00");

        double runningTime = 0.0;
        for (int i = 0; i < 6000; i++) {
            FiringStatus status = subject.progressTime(0.01);
            runningTime += 0.01;
            System.out.println("Current time: " + df.format(runningTime) + " Current status: " + status.getClass());
        }

    }

    private FiringProperties setupFiringProperties(FiringProperties.TriggerType triggerType, double fireRate, double reloadTime, int magazineSize, double chargeTime, int spoolThreshold) {
        FiringProperties props = new FiringProperties();
        props.setFireRate(fireRate);
        props.setChargeTime(chargeTime);
        props.setReloadTime(reloadTime);
        props.setMagazineSize(magazineSize);
        props.setSpoolThreshold(spoolThreshold);
        props.setTriggerType(triggerType);
        props.reloadMagazine(); //TODO: get rid of this

        return props;
    }

}