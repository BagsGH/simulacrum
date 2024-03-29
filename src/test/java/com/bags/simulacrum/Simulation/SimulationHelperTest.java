package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageSourceType;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.BodyModifier;
import com.bags.simulacrum.Entity.BodyPart;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Status.Corrosion;
import com.bags.simulacrum.Status.Ignite;
import com.bags.simulacrum.Status.StatusProcHelper;
import com.bags.simulacrum.Weapon.Weapon;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static com.bags.simulacrum.Armor.HealthClass.*;
import static com.bags.simulacrum.Damage.DamageSourceType.DELAYED_AOE;
import static com.bags.simulacrum.Damage.DamageType.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("FieldCanBeLocal")
public class SimulationHelperTest {

    //TODO: testing multiple targets

    @InjectMocks
    private SimulationHelper subject;

    @Mock
    private RandomNumberGenerator mockRandomNumberGenerator;

    @Mock
    private TargetDamageHelper mockTargetDamageHelper;

    @Mock
    private StatusProcHelper mockStatusProcHelper;

    private Map<DamageType, Double> fakeDamageToHealth;
    private Map<DamageType, Double> fakeDamageToShields;
    private Map<DamageType, Double> anotherFakeDamageToShields;
    private Map<DamageType, Double> anotherFakeDamageToHealth;
    private Map<DamageType, Double> expectedInitialMap;

    private Weapon fakeWeapon;

    private DamageSource fakeDamageSource;

    private Target fakeTarget;
    private Health fakeHealth;
    private Health fakeShields;
    private Health fakeArmor;

    private BodyModifier fakeBodyModifier;
    private BodyModifier fakeHeadshotBodyModifier;

    private DamageMetrics fakeDamageMetrics;
    private DamageMetrics anotherFakeDamageMetrics;

    private SimulationTargets fakeSimulationTargets;

    private String fakeTargetName;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);


        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.50);

        setupDefaultFakeWeapon();
        setupDefaultFakeTarget();
        setupFakeDamageMetricsMocks();


        fakeSimulationTargets = new SimulationTargets(fakeTarget, null);
    }

    @Test
    public void itCallsRandomFiveTimesWithoutMultishotWithOneDamageSource() {
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockRandomNumberGenerator, times(5)).getRandomPercentage();
    }

    @Test
    public void itCallsRandomMoreThanFiveTimesWithMultishotWithOneDamageSource_1() {
        fakeWeapon.setMultishot(2.0);
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockRandomNumberGenerator, times(9)).getRandomPercentage();
    }

    @Test
    public void itCallsRandomMoreThanFiveTimesWithMultishotWithOneDamageSource_2() {
        fakeWeapon.setMultishot(2.51);
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockRandomNumberGenerator, times(13)).getRandomPercentage();
    }

    @Test
    public void itCallsRandomMoreThanFiveTimesWithMultishotWithOneDamageSource_3() {
        fakeWeapon.setMultishot(3.0);
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockRandomNumberGenerator, times(13)).getRandomPercentage();
    }

    @Test
    public void itCallsRandomMoreThanFiveTimesWithMultishotWithTwoDamageSources() {
        DamageSource anotherFakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, new ArrayList<>(Collections.singletonList(new Damage(HEAT, 25))));
        fakeWeapon.setDamageSources(Arrays.asList(fakeDamageSource, anotherFakeDamageSource));
        fakeWeapon.setMultishot(3.0);
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockRandomNumberGenerator, times(19)).getRandomPercentage();
    }

    @Test
    public void itCallsTargetDamageHelperOnceWithoutMultishotWithOneDamageSource() {
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockTargetDamageHelper, times(1)).applyDamageSourceDamageToTarget(any(), any(), any());
    }

    @Test
    public void itCallsTargetDamageHelperTwiceWithMultishotWithOneDamageSource_1() {
        fakeWeapon.setMultishot(2.0);
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockTargetDamageHelper, times(2)).applyDamageSourceDamageToTarget(any(), any(), any());
    }

    @Test
    public void itCallsTargetDamageHelperTwiceWithMultishotWithOneDamageSource_2() {
        fakeWeapon.setMultishot(2.5);
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockTargetDamageHelper, times(2)).applyDamageSourceDamageToTarget(any(), any(), any());
    }

    @Test
    public void itCallsTargetDamageHelperThreeTimesWithMultishotWithOneDamageSource_3() {
        fakeWeapon.setMultishot(2.51);
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockTargetDamageHelper, times(3)).applyDamageSourceDamageToTarget(any(), any(), any());
    }

    @Test
    public void itCallsTargetDamageHelperTwiceWithoutMultishotWithTwoDamageSources() {
        DamageSource anotherFakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, new ArrayList<>(Collections.singletonList(new Damage(HEAT, 25))));
        fakeWeapon.setDamageSources(Arrays.asList(fakeDamageSource, anotherFakeDamageSource));
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockTargetDamageHelper, times(2)).applyDamageSourceDamageToTarget(any(), any(), any());
    }

    @Test
    public void itCallsTargetDamageHelperSixTimesWithMultishotWithTwoDamageSources() {
        DamageSource anotherFakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, new ArrayList<>(Collections.singletonList(new Damage(HEAT, 25))));
        fakeWeapon.setDamageSources(Arrays.asList(fakeDamageSource, anotherFakeDamageSource));
        fakeWeapon.setMultishot(2.51);
        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockTargetDamageHelper, times(6)).applyDamageSourceDamageToTarget(any(), any(), any());
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_Headshot() {
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.14);
        fakeWeapon.setCriticalChance(0.10);
        fakeWeapon.setStatusChance(0.0);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.15);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(1.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(0.0, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(0, actualHitProperties.getCritLevel());
        assertEquals(0.0, actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_NoHeadshot() {
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.51);
        fakeWeapon.setCriticalChance(0.10);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.15);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(0.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(0.0, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(0, actualHitProperties.getCritLevel());
        assertEquals(0.0, actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_Bodyshot() {
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.15);
        fakeWeapon.setCriticalChance(0.10);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.10);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(0.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(-0.50, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(0, actualHitProperties.getCritLevel());
        assertEquals(0.0, actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_Critical() {
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.55);
        fakeWeapon.setCriticalChance(0.56);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(0.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(0.0, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(1, actualHitProperties.getCritLevel());
        assertEquals(fakeWeapon.getCriticalDamage(), actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_Critical_Headshot() {
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.55);
        fakeWeapon.setCriticalChance(0.56);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.60);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(1.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(0.0, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(1, actualHitProperties.getCritLevel());
        assertEquals(fakeWeapon.getCriticalDamage(), actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_Critical_Bodyshot() {
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.45);
        fakeWeapon.setCriticalChance(0.46);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.30);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(0.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(-0.50, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(1, actualHitProperties.getCritLevel());
        assertEquals(fakeWeapon.getCriticalDamage(), actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itReturnsADelayedDamageSource() {
        fakeDamageSource.setDamageSourceType(DELAYED_AOE);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(1, firedWeaponSummary.getDelayedDamageSources().size());
        assertEquals(fakeDamageSource, firedWeaponSummary.getDelayedDamageSources().get(0).getDamageSource());
        assertEquals(DELAYED_AOE, firedWeaponSummary.getDelayedDamageSources().get(0).getDamageSource().getDamageSourceType());
    }

    @Test
    public void itReturnsDelayedDamageSources() {
        fakeDamageSource.setDamageSourceType(DELAYED_AOE);
        DamageSource anotherFakeDamageSource = new DamageSource(DamageSourceType.DELAYED, Collections.singletonList(new Damage(IMPACT, 50.0)));
        fakeWeapon.setDamageSources(Arrays.asList(fakeDamageSource, anotherFakeDamageSource));

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(2, firedWeaponSummary.getDelayedDamageSources().size());
        assertEquals(fakeDamageSource, firedWeaponSummary.getDelayedDamageSources().get(0).getDamageSource());
        assertEquals(anotherFakeDamageSource, firedWeaponSummary.getDelayedDamageSources().get(1).getDamageSource());
    }

    @Test
    public void itReturnsADamageSummary() {
        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(expectedInitialMap, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getDamageToHealth());
        assertEquals(expectedInitialMap, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getDamageToShields());
    }

    @Test
    public void itReturnsADamageSummaryWithValuesReturnedByHelpers_Health() {
        fakeDamageToHealth.put(HEAT, 50.0);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(fakeDamageToHealth, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getDamageToHealth());
        assertEquals(expectedInitialMap, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getDamageToShields());

        assertEquals(50.0, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getDamageToHealth().get(HEAT), 0.0);
    }

    @Test
    public void itSumsDamageSummariesFromMultipleDamageSources_Health() {
        fakeDamageToHealth.put(HEAT, 50.0);
        anotherFakeDamageToHealth.put(HEAT, 75.0);

        when(mockTargetDamageHelper.applyDamageSourceDamageToTarget(any(), any(), any())).thenReturn(fakeDamageMetrics).thenReturn(anotherFakeDamageMetrics);

        DamageSource anotherFakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(HEAT, 1234.0)));
        fakeWeapon.setDamageSources(Arrays.asList(fakeDamageSource, anotherFakeDamageSource));

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(125.0, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getDamageToHealth().get(HEAT), 0.0);
    }

    @Test
    public void itReturnsADamageSummaryWithValuesReturnedByHelpers_Shields() {
        fakeDamageToShields.put(HEAT, 50.0);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(expectedInitialMap, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getDamageToHealth());
        assertEquals(fakeDamageMetrics.getDamageToShields(), firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getDamageToShields());
        assertEquals(50.0, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getDamageToShields().get(HEAT), 0.0);
    }

    @Test
    public void itSumsDamageSummariesFromMultipleDamageSources_Shields() {
        fakeDamageToShields.put(HEAT, 50.0);
        anotherFakeDamageToShields.put(HEAT, 75.0);

        when(mockTargetDamageHelper.applyDamageSourceDamageToTarget(any(), any(), any())).thenReturn(fakeDamageMetrics).thenReturn(anotherFakeDamageMetrics);

        DamageSource anotherFakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(HEAT, 1234.0)));
        fakeWeapon.setDamageSources(Arrays.asList(fakeDamageSource, anotherFakeDamageSource));

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(125.0, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getDamageToShields().get(HEAT), 0.0);
    }

    @Test
    public void itReturnsMetrics() {
        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(1, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).size());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getHeadshotModifier(), 0.0);
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getBodyPartModifier(), 0.0);
        assertEquals(0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCritLevel());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itReturnsMetrics_Headshot() {
        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.60);

        assertEquals(1, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).size());
        assertEquals(1.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getHeadshotModifier(), 0.0);
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getBodyPartModifier(), 0.0);
        assertEquals(0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCritLevel());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itReturnsMetrics_Bodyshot() {
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.40);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(1, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).size());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getHeadshotModifier(), 0.0);
        assertEquals(-0.50, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getBodyPartModifier(), 0.0);
        assertEquals(0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCritLevel());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itReturnsMetrics_Critical() {
        fakeWeapon.setCriticalChance(0.70);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.40);

        assertEquals(1, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).size());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getHeadshotModifier(), 0.0);
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getBodyPartModifier(), 0.0);
        assertEquals(1, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCritLevel());
        assertEquals(fakeWeapon.getCriticalDamage(), firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itReturnsMetrics_Critical_Headshot() {
        fakeWeapon.setCriticalChance(0.70);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.60);

        assertEquals(1, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).size());
        assertEquals(1.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getHeadshotModifier(), 0.0);
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getBodyPartModifier(), 0.0);
        assertEquals(1, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCritLevel());
        assertEquals(fakeWeapon.getCriticalDamage(), firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itReturnsMetrics_Critical_Bodyshot() {
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.40);
        fakeWeapon.setCriticalChance(0.70);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(1, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).size());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getHeadshotModifier(), 0.0);
        assertEquals(-0.50, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getBodyPartModifier(), 0.0);
        assertEquals(1, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCritLevel());
        assertEquals(fakeWeapon.getCriticalDamage(), firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itReturnsMetrics_Multishot() {
        fakeWeapon.setMultishot(2.0);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(2, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).size());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getHeadshotModifier(), 0.0);
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getBodyPartModifier(), 0.0);
        assertEquals(0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCritLevel());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCriticalDamageMultiplier(), 0.0);

        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(1).getHeadshotModifier(), 0.0);
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(1).getBodyPartModifier(), 0.0);
        assertEquals(0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(1).getCritLevel());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(1).getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void eachShotOfMultishotCalculatesMetricsIndependently() {
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.50).thenReturn(0.50).thenReturn(0.50).thenReturn(0.50).thenReturn(0.50).thenReturn(0.10).thenReturn(0.50);
        fakeWeapon.setMultishot(2.0);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(2, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).size());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getHeadshotModifier(), 0.0);
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getBodyPartModifier(), 0.0);
        assertEquals(0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCritLevel());
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(0).getCriticalDamageMultiplier(), 0.0);

        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(1).getHeadshotModifier(), 0.0);
        assertEquals(0.0, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(1).getBodyPartModifier(), 0.0);
        assertEquals(1, firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(1).getCritLevel());
        assertEquals(fakeWeapon.getCriticalDamage(), firedWeaponSummary.getHitPropertiesListMap().get(fakeTargetName).get(1).getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itReturnsStatusProcMetrics() {
        when(mockStatusProcHelper.constructStatusProc(any(), any(), any())).thenReturn(new Ignite());
        fakeWeapon.setStatusChance(0.75);
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.74);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(1, firedWeaponSummary.getStatusesAppliedMap().get(fakeTargetName).size());
        assertTrue(firedWeaponSummary.getStatusesAppliedMap().get(fakeTargetName).get(0) instanceof Ignite);
    }

    @Test
    public void itAppliesTheStatusProcDamageToTheTarget() {
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);
        Ignite fakeIgnite = mock(Ignite.class);
        when(mockStatusProcHelper.constructStatusProc(any(), any(), any())).thenReturn(fakeIgnite);
        DamageSource fakeIgniteDamageTickDamageSource = mock(DamageSource.class);
        when(fakeIgnite.apply(fakeTarget)).thenReturn(fakeIgniteDamageTickDamageSource);

        DamageMetrics fakeDamageMetricsReturnedFromApplyingDamageTickDamageSourceToTarget = mock(DamageMetrics.class);
        anotherFakeDamageToShields.put(HEAT, 22.0);
        when(fakeDamageMetricsReturnedFromApplyingDamageTickDamageSourceToTarget.getDamageToShields()).thenReturn(anotherFakeDamageToShields);
        when(mockTargetDamageHelper.applyDamageSourceDamageToTarget(eq(fakeIgniteDamageTickDamageSource), hitPropertiesCaptor.capture(), eq(fakeTarget))).thenReturn(fakeDamageMetricsReturnedFromApplyingDamageTickDamageSourceToTarget);

        fakeWeapon.setStatusChance(0.75);
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.74);
        HitProperties expectedHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(eq(fakeIgniteDamageTickDamageSource), hitPropertiesCaptor.capture(), eq(fakeTarget));
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(1, firedWeaponSummary.getStatusesAppliedMap().get(fakeTargetName).size());
        assertEquals(22.0, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getStatusDamageToShields().get(HEAT), 0.0);
        assertTrue(firedWeaponSummary.getStatusesAppliedMap().get(fakeTargetName).get(0) instanceof Ignite);
        assertEquals(expectedHitProperties, actualHitProperties);
    }

    @Test
    public void ifTheReturnedStatusProcHasNoDamageItDoesNotDealDamageToTheTarget() {
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);
        Corrosion fakeCorrosion = mock(Corrosion.class);
        when(mockStatusProcHelper.constructStatusProc(any(), any(), any())).thenReturn(fakeCorrosion);
        DamageSource fakeCorrosionTickDamageSource = mock(DamageSource.class);
        when(fakeCorrosion.apply(fakeTarget)).thenReturn(fakeCorrosionTickDamageSource);

        DamageMetrics fakeDamageMetricsReturnedFromApplyingDamageTickDamageSourceToTarget = mock(DamageMetrics.class);
        anotherFakeDamageToShields.put(CORROSIVE, 0.0);
        when(fakeDamageMetricsReturnedFromApplyingDamageTickDamageSourceToTarget.getDamageToShields()).thenReturn(anotherFakeDamageToShields);
        when(mockTargetDamageHelper.applyDamageSourceDamageToTarget(eq(fakeCorrosionTickDamageSource), hitPropertiesCaptor.capture(), eq(fakeTarget))).thenReturn(fakeDamageMetricsReturnedFromApplyingDamageTickDamageSourceToTarget);

        fakeWeapon.setStatusChance(0.75);
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.74);
        HitProperties expectedHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(eq(fakeCorrosionTickDamageSource), hitPropertiesCaptor.capture(), eq(fakeTarget));
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(1, firedWeaponSummary.getStatusesAppliedMap().get(fakeTargetName).size());
        assertEquals(0.0, firedWeaponSummary.getDamageMetricsMap().get(fakeTargetName).getStatusDamageToShields().get(CORROSIVE), 0.0);
        assertTrue(firedWeaponSummary.getStatusesAppliedMap().get(fakeTargetName).get(0) instanceof Corrosion);
        assertEquals(expectedHitProperties, actualHitProperties);
    }

    @Test
    public void eachShotOfMultishotCalculatesStatusChanceIndependently() {
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.50).thenReturn(0.50).thenReturn(0.50).thenReturn(0.50).thenReturn(0.50).thenReturn(0.50).thenReturn(0.04);
        when(mockStatusProcHelper.constructStatusProc(any(), any(), any())).thenReturn(new Corrosion());
        fakeWeapon.setStatusChance(0.05);
        fakeWeapon.setMultishot(2.0);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeSimulationTargets, 0.0);

        assertEquals(1, firedWeaponSummary.getStatusesAppliedMap().get(fakeTargetName).size());
        assertTrue(firedWeaponSummary.getStatusesAppliedMap().get(fakeTargetName).get(0) instanceof Corrosion);
    }

    private void setupFakeDamageMetricsMocks() {
        fakeDamageMetrics = mock(DamageMetrics.class);
        anotherFakeDamageMetrics = mock(DamageMetrics.class);

        when(mockTargetDamageHelper.applyDamageSourceDamageToTarget(any(), any(), any())).thenReturn(fakeDamageMetrics);
        fakeDamageToHealth = DamageMetrics.initialDamageMap();
        anotherFakeDamageToHealth = DamageMetrics.initialDamageMap();
        fakeDamageToShields = DamageMetrics.initialDamageMap();
        anotherFakeDamageToShields = DamageMetrics.initialDamageMap();
        expectedInitialMap = DamageMetrics.initialDamageMap();

        when(fakeDamageMetrics.getDamageToHealth()).thenReturn(fakeDamageToHealth);
        when(fakeDamageMetrics.getDamageToShields()).thenReturn(fakeDamageToShields);
        when(anotherFakeDamageMetrics.getDamageToHealth()).thenReturn(anotherFakeDamageToHealth);
        when(anotherFakeDamageMetrics.getDamageToShields()).thenReturn(anotherFakeDamageToShields);
    }

    private void setupDefaultFakeTarget() {
        fakeTargetName = "banana";
        fakeHealth = new Health(INFESTED_FLESH, 250.0);
        fakeShields = new Health(SHIELD, 250.0);
        fakeArmor = new Health(ALLOY, 300.0);
        fakeTarget = new Target();
        fakeTarget.setTargetId(fakeTargetName);
        fakeTarget.setHealths(Arrays.asList(fakeHealth, fakeShields, fakeArmor));
        fakeBodyModifier = new BodyModifier(BodyPart.GUN, -0.50, 0.50);
        fakeHeadshotBodyModifier = new BodyModifier(BodyPart.HEAD, 1.0);
        fakeTarget.setBodyModifiers(Collections.singletonList(fakeBodyModifier));
        fakeTarget.setHeadBodyModifier(fakeHeadshotBodyModifier);
    }

    private void setupDefaultFakeWeapon() {
        fakeWeapon = new Weapon();
        fakeWeapon.setMultishot(1.0);
        fakeWeapon.setCriticalChance(0.25);
        fakeWeapon.setCriticalDamage(2.0);
        fakeWeapon.setStatusChance(0.05);
        fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(HEAT, 25.0)));
        fakeWeapon.setDamageSources(Collections.singletonList(fakeDamageSource));
    }

}