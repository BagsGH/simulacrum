package com.bags.simulacrum.Damage;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static com.bags.simulacrum.Damage.DamageType.PUNCTURE;
import static org.junit.Assert.assertEquals;

public class DamageSourceTest {

    private DamageSource subject;

    @Before
    public void setup() {
        subject = new DamageSource();
    }

    @Test
    public void itCanHaveSetterTestCoverage() {
        subject.setAoe(75.0);
        subject.setDelay(2.0);
        subject.setDamages(Collections.singletonList(new Damage(PUNCTURE)));
        subject.setDamageSourceType(DamageSourceType.PROJECTILE);

        assertEquals(75.0, subject.getAoe(), 0.0);
        assertEquals(2.0, subject.getDelay(), 0.0);
        assertEquals(PUNCTURE, subject.getDamages().get(0).getType());
        assertEquals(DamageSourceType.PROJECTILE, subject.getDamageSourceType());
    }

    @Test
    public void itCanHaveConstructorTestCoverage_1() {
        subject = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(PUNCTURE)));

        assertEquals(PUNCTURE, subject.getDamages().get(0).getType());
        assertEquals(DamageSourceType.PROJECTILE, subject.getDamageSourceType());
    }

    @Test
    public void itCanHaveConstructorTestCoverage_2() {
        subject = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(PUNCTURE)), 2.0, 75.0);

        assertEquals(PUNCTURE, subject.getDamages().get(0).getType());
        assertEquals(DamageSourceType.PROJECTILE, subject.getDamageSourceType());
        assertEquals(75.0, subject.getAoe(), 0.0);
        assertEquals(2.0, subject.getDelay(), 0.0);
    }

    @Test
    public void itCanHaveCopyConstructorTestCoverage() {
        DamageSource copy = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(PUNCTURE)), 2.0, 75.0);

        subject = copy.copyWithoutDamages();

        assertEquals(DamageSourceType.PROJECTILE, subject.getDamageSourceType());
        assertEquals(75.0, subject.getAoe(), 0.0);
        assertEquals(2.0, subject.getDelay(), 0.0);
    }

    @Test
    public void itCanAddDamagesToList() {
        subject.addDamage(new Damage(PUNCTURE));

        assertEquals(PUNCTURE, subject.getDamages().get(0).getType());
    }

}