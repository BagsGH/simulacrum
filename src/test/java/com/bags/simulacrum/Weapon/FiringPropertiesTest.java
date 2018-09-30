package com.bags.simulacrum.Weapon;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FiringPropertiesTest {

    private FiringProperties subject;

    @Before
    public void setup() {
        subject = new FiringProperties();
    }

    @Test
    public void itCanExpendAmmo() {
        subject.setMagazineSize(5);
        subject.loadMagazine();
        subject.expendAmmo();

        assertEquals(4, subject.getCurrentMagazineSize());
    }

    @Test
    public void itCanLoadNewMagazine() {
        subject.setMagazineSize(5);
        subject.loadMagazine();
        subject.expendAmmo();
        subject.loadMagazine();

        assertEquals(5, subject.getCurrentMagazineSize());
    }

    @Test
    public void itCanHaveTestCoverage() {
        subject.setAccuracy(0.55);
        subject.setMagazineSize(55);
        subject.setMaxAmmo(110);
        subject.setMinBonusDamageFromCharging(.110);
        subject.setMaxBonusDamageFromCharging(.510);
        subject.setMinChargePercentage(.75);

        assertEquals(0.55, subject.getAccuracy(), 0.0);
        assertEquals(.110, subject.getMinBonusDamageFromCharging(), 0.0);
        assertEquals(.510, subject.getMaxBonusDamageFromCharging(), 0.0);
        assertEquals(.75, subject.getMinChargePercentage(), 0.0);
        assertEquals(55, subject.getMagazineSize());
        assertEquals(110, subject.getMaxAmmo());
    }
}