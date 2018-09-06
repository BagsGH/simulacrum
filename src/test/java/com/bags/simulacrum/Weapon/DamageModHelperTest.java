package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageSourceType;
import com.bags.simulacrum.Damage.DamageType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class DamageModHelperTest {

    private DamageModHelper subject;

    private Mod fakeMod;
    private Mod anotherFakeMod;
    private List<Mod> fakeModList;

    @Before
    public void setUp() {
        subject = new DamageModHelper();
        fakeMod = new Mod();
        anotherFakeMod = new Mod();
        fakeModList = new ArrayList<>();
    }

    @Test
    public void itCanCorrectlyCombineOneElementalModWithBaseElement() {
        fakeMod.setDamage(new Damage(DamageType.TOXIN, 0.0, 0.60));
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.HEAT, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertEquals(DamageType.GAS, actualModifiedDamageSource.getDamageTypes().get(0).getType());
        assertEquals(damageSource.getDamageSourceType(), actualModifiedDamageSource.getDamageSourceType());
    }

    @Test
    public void itCanCorrectlyCalculatePositiveDamage() {
        fakeMod.setDamageIncrease(1.65);
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 92.75), actualModifiedDamageSource.getDamageTypes(), 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeDamage() {
        fakeMod.setDamageIncrease(-0.15);
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.TOXIN, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.TOXIN, 29.75), actualModifiedDamageSource.getDamageTypes(), 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexDamage() {
        fakeMod.setDamageIncrease(-0.15);
        anotherFakeMod.setDamageIncrease(1.65);
        fakeModList.add(fakeMod);
        fakeModList.add(anotherFakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 87.5), actualModifiedDamageSource.getDamageTypes(), 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateDamageAddedBy90PercentToxin() {
        fakeMod.setDamage(new Damage(DamageType.TOXIN, 0.0, 0.90));
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 35.0), actualModifiedDamageSource.getDamageTypes(), 0.001);
        assertExpectedDamageExists(new Damage(DamageType.TOXIN, 31.5), actualModifiedDamageSource.getDamageTypes(), 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateDamageAddedBy90PercentHeatWhenDefaultSourceHasHeatDamage() {
        fakeMod.setDamage(new Damage(DamageType.HEAT, 0.0, 0.90));
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.HEAT, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.HEAT, 66.5), actualModifiedDamageSource.getDamageTypes(), 0.001);
    }

    @Test
    public void itCanCorrectlyCombineElements() {
        fakeMod.setDamage(new Damage(DamageType.HEAT, 0.0, 0.90));
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.TOXIN, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.GAS, 66.5), actualModifiedDamageSource.getDamageTypes(), 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateIPSDamage() {
        fakeMod.setDamage(new Damage(DamageType.IMPACT, 0.0, 0.20));
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 9.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 10.8), actualModifiedDamageSource.getDamageTypes(), 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateMultipleDamagesOnSameSource() {
        fakeMod.setDamage(new Damage(DamageType.TOXIN, 0.0, 0.90));
        anotherFakeMod.setDamageIncrease(1.65);
        fakeModList.add(fakeMod);
        fakeModList.add(anotherFakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, null);
        damageSource.addDamage(new Damage(DamageType.IMPACT, 35.0, 0.0));
        damageSource.addDamage(new Damage(DamageType.HEAT, 35.0, 0.0));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 92.75), actualModifiedDamageSource.getDamageTypes(), 0.001);
        assertExpectedDamageExists(new Damage(DamageType.GAS, 259.7), actualModifiedDamageSource.getDamageTypes(), 0.001);
    }

    public void assertExpectedDamageExists(Damage damageExpected, List<Damage> actualDamages, double accuracyThreshold) {
        assertTrue(actualDamages.stream().anyMatch(damage -> damage.getType().equals(damageExpected.getType()) && Math.abs(damage.getDamageValue() - damageExpected.getDamageValue()) < accuracyThreshold));
    }
}