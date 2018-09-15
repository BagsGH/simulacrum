package com.bags.simulacrum.Damage;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class DamageSourceTest {

    private DamageSource subject;

    @Before
    public void setup() {

    }

    @Test
    public void itCanHaveSetterTestCoverage() {
        subject = new DamageSource();

        subject.setAoe(75.0);
        subject.setDelay(2.0);
        subject.setDamages(Collections.singletonList(new Damage(DamageType.PUNCTURE)));
        subject.setDamageSourceType(DamageSourceType.PROJECTILE);

        assertEquals(75.0, subject.getAoe(), 0.001);
        assertEquals(2.0, subject.getDelay(), 0.001);
        assertEquals(DamageType.PUNCTURE, subject.getDamages().get(0).getType());
        assertEquals(DamageSourceType.PROJECTILE, subject.getDamageSourceType());

    }
}