package com.bags.simulacrum.Weapon;

import org.junit.Before;
import org.junit.Test;

public class WeaponStatusTest {

    private WeaponStatus subject;
    private FiringProperties fakeFiringProperties;

    @Before
    public void before() {
        fakeFiringProperties = new FiringProperties();
        fakeFiringProperties.setFireRate(15);
        //fakeFiringProperties.setChargeTime(1.15);
        fakeFiringProperties.setReloadTime(2.50);
        fakeFiringProperties.setMagazineSize(200);
        fakeFiringProperties.setSpoolThreshold(5);
        fakeFiringProperties.setTriggerType(FiringProperties.TriggerType.AUTOSPOOL);
        fakeFiringProperties.reloadMagazine();
        subject = new WeaponStatus(fakeFiringProperties);
    }

    @Test
    public void test() {

        double runningTime = 0.0;
        for (int i = 0; i < 6000; i++) {
            FiringStatus status = subject.progressTime(0.01);
            runningTime += 0.01;
            System.out.println("Current time: " + runningTime + ". Current status: " + status.getClass());
        }

    }

}