package com.bags.simulacrum.Damage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DamageTest {

    private Damage subject;

    @Before
    public void setUp() {
        subject = new Damage();
    }

    @Test
    public void itCanHaveSetterTestCoverage() {
        subject.setType(DamageType.PUNCTURE);
        subject.setDamageValue(5.0);
        subject.setModAddedDamageRatio(0.75);

        assertEquals(DamageType.PUNCTURE, subject.getType());
        assertEquals(5.0, subject.getDamageValue(), 0.001);
        assertEquals(0.75, subject.getModAddedDamageRatio(), 0.001);
    }

    @Test
    public void itCanHaveConstructorTestCoverage_1() {
        subject = new Damage(DamageType.PUNCTURE);

        assertEquals(DamageType.PUNCTURE, subject.getType());
    }

    @Test
    public void itCanHaveConstructorTestCoverage_2() {
        subject = new Damage(DamageType.PUNCTURE, 5.0);

        assertEquals(DamageType.PUNCTURE, subject.getType());
        assertEquals(5.0, subject.getDamageValue(), 0.001);
    }

    @Test
    public void itCanHaveConstructorTestCoverage_3() {
        subject = new Damage(DamageType.PUNCTURE, 5.0, 0.75);

        assertEquals(DamageType.PUNCTURE, subject.getType());
        assertEquals(5.0, subject.getDamageValue(), 0.001);
        assertEquals(0.75, subject.getModAddedDamageRatio(), 0.001);
    }

    @Test
    public void itCanHaveCopyConstructorTestCoverage_3() {
        subject = new Damage(new Damage(DamageType.PUNCTURE, 5.0, 0.75));

        assertEquals(DamageType.PUNCTURE, subject.getType());
        assertEquals(5.0, subject.getDamageValue(), 0.001);
        assertEquals(0.75, subject.getModAddedDamageRatio(), 0.001);
    }
}