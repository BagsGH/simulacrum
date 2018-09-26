package com.bags.simulacrum.Weapon;

import org.junit.Before;
import org.junit.Test;

public class WeaponStatusTest {

    private WeaponStatus subject;
    private FiringProperties fakeFiringProperties;

    @Before
    public void before() {
        fakeFiringProperties = new FiringProperties();
        fakeFiringProperties.setFireRate(0.5);
        fakeFiringProperties.setReloadTime(0.00);
        fakeFiringProperties.setMagazineSize(10);
        fakeFiringProperties.setTriggerType(FiringProperties.TriggerType.AUTO);
        fakeFiringProperties.reloadMagazine();
        subject = new WeaponStatus(fakeFiringProperties);
    }

    @Test
    public void test() {
        double runningTime = 0.0;
        int countfired = 0;
        for (int i = 0; i < 10000; i++) {
            FiringStatus status = subject.progressTime(0.01);
            runningTime += 0.01;
            if (status instanceof Fired) {
                countfired++;
            }
            System.out.println("Current time: " + runningTime + ". Current status: " + status.getClass());

            if (status instanceof Fired || status instanceof Reloading) {
            }
        }

        System.out.println(countfired);
    }

}