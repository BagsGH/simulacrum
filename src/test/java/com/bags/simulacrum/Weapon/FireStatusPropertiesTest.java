package com.bags.simulacrum.Weapon;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FireStatusPropertiesTest {

    private FireStatusProperties subject;

    @Before
    public void setup() {
        subject = new FireStatusProperties();
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
        subject.setMagazineSize(55);
        subject.setMaxAmmo(110);

        assertEquals(55, subject.getMagazineSize());
        assertEquals(110, subject.getMaxAmmo());
    }
}