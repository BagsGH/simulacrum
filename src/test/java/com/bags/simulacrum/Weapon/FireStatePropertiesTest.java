package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FireStatePropertiesTest {

    private FireStateProperties subject;

    @Before
    public void setup() {
        subject = new FireStateProperties.FireStatePropertiesBuilder(TriggerType.HELD, 1.25, 1, 1).build();
    }

    @Test
    public void itCanExpendAmmo() {
        subject.setMagazineSize(5);
        subject.setCurrentAmmo(50);
        subject.loadMagazine();
        subject.subtractAmmo();

        assertEquals(4, subject.getCurrentMagazineSize());
    }

    @Test
    public void itCanLoadNewMagazine() {
        subject.setMagazineSize(5);
        subject.setCurrentAmmo(50);
        subject.loadMagazine();
        subject.subtractAmmo();
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