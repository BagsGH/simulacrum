package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class DamageModHelperTest {

    @InjectMocks
    private DamageModHelper subject;

    @Mock
    private ElementalDamageMapper elementalDamageMapperMock;

    private Mod fakeMod;
    private Mod anotherFakeMod;
    private List<Mod> fakeModList;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fakeMod = new Mod();
        anotherFakeMod = new Mod();
        fakeModList = new ArrayList<>();
        when(elementalDamageMapperMock.combineElements(DamageType.HEAT, DamageType.TOXIN)).thenReturn(DamageType.GAS);
        when(elementalDamageMapperMock.combineElements(DamageType.TOXIN, DamageType.HEAT)).thenReturn(DamageType.GAS);
    }

    @Test
    public void itCanCombineOneElementalModWithBaseElement() {
        fakeMod.setDamage(new Damage(DamageType.TOXIN, 0.0, 0.60));
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.HEAT, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertEquals(DamageType.GAS, actualModifiedDamageSource.getDamages().get(0).getType());
        assertEquals(damageSource.getDamageSourceType(), actualModifiedDamageSource.getDamageSourceType());
    }

    @Test
    public void itCanCalculatePositiveDamage() {
        fakeMod.setDamageIncrease(1.65);
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 92.75), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itCanCalculateNegativeDamage() {
        fakeMod.setDamageIncrease(-0.15);
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.TOXIN, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.TOXIN, 29.75), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itCanCalculateComplexDamage() {
        fakeMod.setDamageIncrease(-0.15);
        anotherFakeMod.setDamageIncrease(1.65);
        fakeModList.add(fakeMod);
        fakeModList.add(anotherFakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 87.5), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itCanCalculateDamageAddedBy90PercentToxin() {
        fakeMod.setDamage(new Damage(DamageType.TOXIN, 0.0, 0.90));
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 35.0), actualModifiedDamageSource.getDamages(), 0.001);
        assertExpectedDamageExists(new Damage(DamageType.TOXIN, 31.5), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itCanCalculateDamageAddedBy90PercentHeatWhenDefaultSourceHasHeatDamage() {
        fakeMod.setDamage(new Damage(DamageType.HEAT, 0.0, 0.90));
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.HEAT, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.HEAT, 66.5), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itCanCombineElements() {
        fakeMod.setDamage(new Damage(DamageType.HEAT, 0.0, 0.90));
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.TOXIN, 35.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.GAS, 66.5), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itCanCalculateIPSDamage() {
        fakeMod.setDamage(new Damage(DamageType.IMPACT, 0.0, 0.20));
        fakeModList.add(fakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 9.0, 0.0)));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 10.8), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itCanCalculateMultipleDamagesOnSameSource() {
        fakeMod.setDamage(new Damage(DamageType.TOXIN, 0.0, 0.90));
        anotherFakeMod.setDamageIncrease(1.65);
        fakeModList.add(fakeMod);
        fakeModList.add(anotherFakeMod);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, null);
        damageSource.addDamage(new Damage(DamageType.IMPACT, 35.0, 0.0));
        damageSource.addDamage(new Damage(DamageType.HEAT, 35.0, 0.0));

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.IMPACT, 92.75), actualModifiedDamageSource.getDamages(), 0.001);
        assertExpectedDamageExists(new Damage(DamageType.GAS, 259.7), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itCanHandleTwoElementalsThatCombineAndOneThatIsAlreadyCombined_1() {
        /* This test doesn't REALLY test anything, it's for  100% coverage... but it helps check that some logic that was added is in fact used in some cases. */
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.HEAT, 50.0), new Damage(DamageType.TOXIN, 50.0), new Damage(DamageType.RADIATION, 50.0)));
        fakeMod.setDamageIncrease(0.50);
        fakeModList.add(fakeMod);

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.RADIATION, 75.0), actualModifiedDamageSource.getDamages(), 0.001);
        assertExpectedDamageExists(new Damage(DamageType.GAS, 150.0), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itCanHandleTwoElementalsThatCombineAndOneThatIsAlreadyCombined_2() {
        /* This test doesn't REALLY test anything, it's for  100% coverage... but it helps check that some logic that was added is in fact used in some cases. */
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.RADIATION, 50.0), new Damage(DamageType.HEAT, 50.0), new Damage(DamageType.TOXIN, 50.0)));
        fakeMod.setDamageIncrease(0.50);
        fakeModList.add(fakeMod);

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.RADIATION, 75.0), actualModifiedDamageSource.getDamages(), 0.001);
        assertExpectedDamageExists(new Damage(DamageType.GAS, 150.0), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itDoesNotCombineAPrimalElementalAndCombinedElemental() {
        /* This test doesn't REALLY test anything, it's for  100% coverage... but it helps check that some logic that was added is in fact used in some cases. */
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.HEAT, 50.0), new Damage(DamageType.RADIATION, 50.0)));
        fakeMod.setDamageIncrease(0.50);
        fakeModList.add(fakeMod);

        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.RADIATION, 75.0), actualModifiedDamageSource.getDamages(), 0.001);
        assertExpectedDamageExists(new Damage(DamageType.HEAT, 75.0), actualModifiedDamageSource.getDamages(), 0.001);
    }

    @Test
    public void itSumsIdenticalCombinedTypes() {
        /* This test doesn't REALLY test anything, it's for  100% coverage... but it helps check that some logic that was added is in fact used in some cases. */
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.RADIATION, 50.0)));
        fakeMod.setDamage(new Damage(DamageType.ELECTRICITY, 0.0, 0.50));
        anotherFakeMod.setDamage(new Damage(DamageType.HEAT, 0.0, 0.50));
        fakeModList.add(fakeMod);
        fakeModList.add(anotherFakeMod);
        when(elementalDamageMapperMock.combineElements(DamageType.ELECTRICITY, DamageType.HEAT)).thenReturn(DamageType.RADIATION);
        when(elementalDamageMapperMock.combineElements(DamageType.HEAT, DamageType.ELECTRICITY)).thenReturn(DamageType.RADIATION);


        DamageSource actualModifiedDamageSource = subject.calculateDamageSources(damageSource, fakeModList);

        assertExpectedDamageExists(new Damage(DamageType.RADIATION, 100.0), actualModifiedDamageSource.getDamages(), 0.001);
    }

    private void assertExpectedDamageExists(Damage damageExpected, List<Damage> actualDamages, double accuracyThreshold) {
        assertTrue(actualDamages.stream().anyMatch(damage -> damage.getType().equals(damageExpected.getType()) && Math.abs(damage.getDamageValue() - damageExpected.getDamageValue()) < accuracyThreshold));
    }
}