package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class WeaponTest {

    private Weapon subject;

    @Before
    public void setUp() {
        subject = new Weapon();
        subject.setFireStatusProperties(new FireStatusProperties());
    }

    @Test
    public void itCanHaveSetterTestCoverage() {
        subject.setName("Ignis");
        subject.setTriggerType(TriggerType.CHARGE);
        subject.setFireRate(1.25);
        subject.setAccuracy(1.25);
        subject.setMagazineSize(1);
        subject.setReloadTime(1.25);
        subject.setDamageSources(Collections.singletonList(new DamageSource()));
        subject.setMultishot(1.25);
        subject.setCriticalChance(1.25);
        subject.setCriticalDamage(1.25);
        subject.setStatusChance(1.25);
        subject.setHeadshotMultiplier(1.25);
        subject.setAccuracyMultiplier(1.25);
        subject.setChargeTime(1.25);
        subject.setMods(Collections.singletonList(new Mod()));
        subject.setMaxAmmo(1);
        subject.setRangeLimit(1.25);

        assertEquals("Ignis", subject.getName());
        assertEquals(1.25, subject.getFireRate(), 0.0);
        assertEquals(1.25, subject.getAccuracy(), 0.0);
        assertEquals(1.25, subject.getReloadTime(), 0.0);
        assertEquals(1.25, subject.getMultishot(), 0.0);
        assertEquals(1.25, subject.getCriticalChance(), 0.0);
        assertEquals(1.25, subject.getCriticalDamage(), 0.0);
        assertEquals(1.25, subject.getStatusChance(), 0.0);
        assertEquals(1.25, subject.getHeadshotMultiplier(), 0.0);
        assertEquals(1.25, subject.getAccuracyMultiplier(), 0.0);
        assertEquals(1.25, subject.getChargeTime(), 0.0);
        assertEquals(1.25, subject.getRangeLimit(), 0.0);
        assertEquals(1, subject.getMagazineSize());
        assertEquals(1, subject.getMaxAmmo());
        assertEquals(1, subject.getMods().size());
        assertEquals(1, subject.getDamageSources().size());
    }

    @Test
    public void itCanAddAModToWeapon() {
        subject.addMod(new Mod(new Damage(DamageType.HEAT, 0.0, 0.75)));

        assertEquals(1, subject.getMods().size());
        assertEquals(DamageType.HEAT, subject.getMods().get(0).getDamage().getType());
        assertEquals(0.75, subject.getMods().get(0).getDamage().getModAddedDamageRatio(), 0.0);
        assertEquals(0.0, subject.getMods().get(0).getDamage().getDamageValue(), 0.0);
    }


}