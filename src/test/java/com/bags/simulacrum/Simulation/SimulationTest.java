package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Configuration.SimulationConfig;
import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageSourceType;
import com.bags.simulacrum.Damage.DelayedDamageSource;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Status.Ignite;
import com.bags.simulacrum.Weapon.State.Charging;
import com.bags.simulacrum.Weapon.State.Fired;
import com.bags.simulacrum.Weapon.State.Ready;
import com.bags.simulacrum.Weapon.Weapon;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.bags.simulacrum.Armor.HealthClass.*;
import static com.bags.simulacrum.Damage.DamageType.CORROSIVE;
import static com.bags.simulacrum.Damage.DamageType.HEAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SimulationTest {

    @InjectMocks
    private Simulation subject;

    @Mock
    private SimulationConfig mockConfig;

    @Mock
    private SimulationHelper mockSimulationHelper;

    private double fakeDeltaTime;
    private double fakeDuration;
    private double fakeHeadshotChance;
    private Weapon fakeWeapon;
    private SimulationTargets fakeSimulationTargets;

    private SimulationParameters fakeSimulationParameters;

    private Target fakeTarget;

    private List<Target> fakeTargetAsList;
    private String fakeTargetName;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        setupDefaultConstantValues();
        setupDefaultConfigMocks();
        setupDefaultTarget();
        setupDefaultWeapon();
        setupDefaultSimulationParameters();
        setupDefaultSimulationHelperMocks();
        fakeTargetAsList = new ArrayList<>(Collections.singletonList(fakeTarget));
    }

    @Test
    public void itNeverCAllsSimulationHelperForDelayedDamageSourceHandlingWithoutDelayedDamageSources() {
        subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper, never()).handleDelayedDamageSources(any(), anyDouble());
    }

    @Test
    public void itCallsSimulationHelperToHandleStatusesEveryDeltaTime() {
        subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper, times((int) (fakeDuration / fakeDeltaTime))).handleApplyingStatuses(any());
    }

    @Test
    public void itCallsSimulationHelperToHandleFireWeaponExpectedNumberOfTimes_0() {
        Charging fakeCharging = new Charging(null);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging);

        subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper, never()).handleFireWeapon(any(), any(), anyDouble());
    }

    @Test
    public void itCallsSimulationHelperToHandleFireWeaponExpectedNumberOfTimes_1() {
        Fired fakeFired = new Fired(null, null);
        Charging fakeCharging = new Charging(null);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging);

        subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper, times(1)).handleFireWeapon(any(), any(), anyDouble());
    }

    @Test
    public void itCallsSimulationHelperToHandleFireWeaponExpectedNumberOfTimes_2() {
        Fired fakeFired = new Fired(null, null);
        Charging fakeCharging = new Charging(null);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeCharging)
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging);

        subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper, times(3)).handleFireWeapon(any(), any(), anyDouble());
    }

//    @Test
//    public void itHandlesDelayedDamageSourcesWithShortDelayOnTheNextDeltaTime() {
//        fakeSimulationParameters.setDuration(0.02);
//        Fired fakeFired = new Fired(null, null);
//        Charging fakeCharging = new Charging(null);
//        when(fakeWeapon.firingStateProgressTime(anyDouble()))
//                .thenReturn(fakeFired)
//                .thenReturn(fakeCharging);
//
//        FiredWeaponSummary fakeFiredWeaponSummary = new FiredWeaponSummary().getEmptySummary();
//        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.DELAYED, Arrays.asList(new Damage(HEAT, 25)), 0.01, 0.0);
//        HitProperties fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
//        DelayedDamageSource fakeDelayedDamageSource = new DelayedDamageSource(fakeDamageSource, fakeHitProperties, fakeDamageSource.getDelay());
//        fakeFiredWeaponSummary.setDelayedDamageSources(Arrays.asList(fakeDelayedDamageSource));
//
//        when(mockSimulationHelper.handleFireWeapon(fakeWeapon, fakeTarget, fakeHeadshotChance)).thenReturn(fakeFiredWeaponSummary);
//
//        SimulationSummary simulationSummary = subject.runSimulation(fakeSimulationParameters);
//
//        verify(mockSimulationHelper).handleDelayedDamageSources(eq(new ArrayList<>()), eq(fakeTarget), anyDouble());
//        verify(mockSimulationHelper).handleDelayedDamageSources(eq(Arrays.asList(fakeDelayedDamageSource)), eq(fakeTarget), anyDouble());
//    }

    @Test
    public void itCallsTheHelperToHandleStatusProcs_1() {
        Ignite ignite = new Ignite();
        ignite.setDuration(0.01);
        ignite.setNumberOfDamageTicks(2);
        ignite.setTickProgress(2);
        fakeSimulationParameters.setDuration(0.01);
        fakeTarget.setStatuses(Collections.singletonList(ignite));
        HitProperties expectedHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper).handleApplyingStatuses(eq(fakeTargetAsList)); //simulationHelper.handleApplyingStatuses(simulationParameters.getSimulationTargets().getAllTargets());
        assertEquals(0, fakeTarget.getStatuses().size());
    }

    @Test
    public void itCallsTheHelperToHandleStatusProcs_2() {
        Ignite ignite = new Ignite();
        ignite.setDuration(0.01);
        ignite.setTickProgress(2);
        ignite.setNumberOfDamageTicks(2);
        Ignite ignite2 = new Ignite();
        ignite2.setDuration(0.01);
        ignite2.setTickProgress(2);
        ignite2.setNumberOfDamageTicks(2);
        fakeSimulationParameters.setDuration(0.01);
        fakeTarget.setStatuses(Arrays.asList(ignite, ignite2));
        HitProperties expectedHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper).handleApplyingStatuses(eq(fakeTargetAsList)); //simulationHelper.handleApplyingStatuses(simulationParameters.getSimulationTargets().getAllTargets());
        assertEquals(0, fakeTarget.getStatuses().size());
    }

    @Test
    public void itReturnsSimulationSummaryWithInformationAboutTimeSpentInEachWeaponState() {
        fakeSimulationParameters.setDuration(0.10);
        Fired fakeFired = new Fired(null, null);
        Charging fakeCharging = new Charging(null);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeCharging)
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging)
                .thenReturn(fakeCharging)
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging);

        SimulationSummary simulationSummary = subject.runSimulation(fakeSimulationParameters);

        assertEquals(3, simulationSummary.getWeaponStateMetrics().getValue(Fired.class));
        assertEquals(7, simulationSummary.getWeaponStateMetrics().getValue(Charging.class));
    }

    @Test
    public void itReturnsSimulationSummaryWithListOfDeadTargets() {
        fakeSimulationParameters.setDuration(fakeDeltaTime);
        Fired fakeFired = new Fired(null, null);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeFired);

        FiredWeaponSummary fakeFiredWeaponSummary = new FiredWeaponSummary();
        doAnswer(invocation -> {
            fakeTarget.getHealth().setHealthValue(0.0);
            return fakeFiredWeaponSummary;
        }).when(mockSimulationHelper).handleFireWeapon(eq(fakeWeapon), eq(fakeSimulationTargets), eq(fakeHeadshotChance)); //simulationHelper.handleFireWeapon(weapon, simulationParameters.getSimulationTargets(), simulationParameters.getHeadshotChance());

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        assertEquals(1, summary.getKilledTargets().size());
        assertEquals(fakeTarget, summary.getKilledTargets().get(0));
    }

    @Test
    public void itAddsDamageAppliedByADelayedDamageSourceToTheRunningTotal() {
        fakeSimulationParameters.setDuration(fakeDeltaTime + fakeDeltaTime);
        Fired fakeFired = new Fired(null, null);
        Charging fakeCharging = new Charging(null);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging);

        FiredWeaponSummary fakeFiredWeaponSummary = new FiredWeaponSummary();
        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.DELAYED, Collections.singletonList(new Damage(HEAT, 25)), 0.01, 0.0);
        HitProperties fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        DelayedDamageSource fakeDelayedDamageSource = new DelayedDamageSource(fakeDamageSource, fakeHitProperties, fakeDamageSource.getDelay());
        fakeFiredWeaponSummary.setDelayedDamageSources(Collections.singletonList(fakeDelayedDamageSource));
        fakeDelayedDamageSource.setTarget(fakeTarget);

        when(mockSimulationHelper.handleFireWeapon(eq(fakeWeapon), eq(fakeSimulationTargets), eq(fakeHeadshotChance))).thenReturn(fakeFiredWeaponSummary);  //simulationHelper.handleFireWeapon(weapon, simulationParameters.getSimulationTargets(), simulationParameters.getHeadshotChance());

        DamageMetrics fakeDamageMetrics = new DamageMetrics.DamageMetricsBuilder().withDamageToHealth().withDamageToShields().withStatusDamageToHealth().withStatusDamageToShields().build();
        fakeDamageMetrics.addDamageToHealth(HEAT, 25.0);

        FiredWeaponSummary fakeDelayedDamageFiredSummary = new FiredWeaponSummary();
        fakeDelayedDamageFiredSummary.addDamageToHealth(fakeTargetName, fakeDamageMetrics.getDamageToHealth());
        fakeDelayedDamageFiredSummary.addHitProperties(fakeTargetName, fakeHitProperties);

        when(mockSimulationHelper.handleDelayedDamageSources(eq(Collections.singletonList(fakeDelayedDamageSource)), anyDouble())).thenReturn(fakeDelayedDamageFiredSummary); //handleDelayedDamageSources(List<DelayedDamageSource> delayedDamageSources, double statusChance) {

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        assertEquals(25.0, summary.getFiredWeaponSummary().getDamageMetricsMap().get(fakeTargetName).getDamageToHealth().get(HEAT), 0.0);
        assertEquals(1, summary.getFiredWeaponSummary().getHitPropertiesListMap().get(fakeTargetName).size());
    }

    @Test
    public void itAddsDamageAppliedByStatusesToRunningTotal() {
        fakeSimulationParameters.setDuration(fakeDeltaTime + fakeDeltaTime);
        Fired fakeFired = new Fired(null, null);
        Charging fakeCharging = new Charging(null);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging);

        FiredWeaponSummary fakeFiredWeaponSummary = new FiredWeaponSummary();
        Ignite fakeIgnite = new Ignite(); //TODO: not a huge fan of how hard to make one of these from scartch. Lots of methods.
        fakeIgnite.setDamageType(CORROSIVE); //Realistically this value would be HEAT, but it doesn't matter.
        fakeIgnite.setDamagePerTick(1234.0); //Realistically this value would be 25.0, but it doesn't matter.
        fakeIgnite.setDuration(0.01);
        fakeIgnite.setNumberOfDamageTicks(2);
        fakeIgnite.setupTimers();
        fakeFiredWeaponSummary.addStatusesApplied(fakeTargetName, new ArrayList<>(Collections.singletonList(fakeIgnite)));
        doAnswer(invocation -> {
            fakeTarget.setStatuses(Collections.singletonList(fakeIgnite));
            fakeIgnite.setTickProgress(fakeIgnite.getTickProgress() + 1);
            return fakeFiredWeaponSummary;
        }).when(mockSimulationHelper).handleFireWeapon(fakeWeapon, fakeSimulationTargets, fakeHeadshotChance);

        DamageMetrics fakeDamageMetrics = new DamageMetrics.DamageMetricsBuilder().withDamageToHealth().withDamageToShields().withStatusDamageToHealth().withStatusDamageToShields().build();
        fakeDamageMetrics.addDamageToHealth(HEAT, 25.0);
        FiredWeaponSummary fakeDelayedDamageFiredSummary = new FiredWeaponSummary();
        fakeDelayedDamageFiredSummary.addStatusDamageToHealth(fakeTargetName, fakeDamageMetrics.getDamageToHealth());
        when(mockSimulationHelper.handleApplyingStatuses(fakeTargetAsList)).thenReturn(fakeDelayedDamageFiredSummary).thenReturn(new FiredWeaponSummary()); //simulationHelper.handleApplyingStatuses(simulationParameters.getSimulationTargets().getAllTargets());

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        assertEquals(25.0, summary.getFiredWeaponSummary().getDamageMetricsMap().get(fakeTargetName).getStatusDamageToHealth().get(HEAT), 0.0);
        assertEquals(fakeIgnite, summary.getFiredWeaponSummary().getStatusesAppliedMap().get(fakeTargetName).get(0));
        assertNull(summary.getFiredWeaponSummary().getHitPropertiesListMap().get(fakeTargetName));
    }

    @Test
    public void itAddsDamagedAppliedByWeaponFiredToRunningTotal() {
        fakeSimulationParameters.setDuration(fakeDeltaTime);
        Fired fakeFired = new Fired(null, null);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeFired);

        FiredWeaponSummary fakeFiredWeaponSummary = new FiredWeaponSummary();
        HitProperties fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        fakeFiredWeaponSummary.setDelayedDamageSources(new ArrayList<>());
        DamageMetrics fakeDamageMetrics = new DamageMetrics.DamageMetricsBuilder().withDamageToHealth().withDamageToShields().withStatusDamageToHealth().withStatusDamageToShields().build();
        fakeDamageMetrics.addDamageToHealth(HEAT, 25.0);
        fakeFiredWeaponSummary.addDamageToHealth(fakeTargetName, fakeDamageMetrics.getDamageToHealth());
        fakeFiredWeaponSummary.addHitProperties(fakeTargetName, fakeHitProperties);

        when(mockSimulationHelper.handleFireWeapon(fakeWeapon, fakeSimulationTargets, fakeHeadshotChance)).thenReturn(fakeFiredWeaponSummary); //simulationHelper.handleFireWeapon(weapon, simulationParameters.getSimulationTargets(), simulationParameters.getHeadshotChance());

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        assertEquals(25.0, summary.getFiredWeaponSummary().getDamageMetricsMap().get(fakeTargetName).getDamageToHealth().get(HEAT), 0.0);
        assertEquals(1, summary.getFiredWeaponSummary().getHitPropertiesListMap().get(fakeTargetName).size());
    }

    @Test
    public void itAddsAllDamageReturnedByHelperCalls() {
        fakeSimulationParameters.setDuration(fakeDeltaTime + fakeDeltaTime);
        Fired fakeFired = new Fired(null, null);
        Charging fakeCharging = new Charging(null);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging);

        /*
         * Setup the mock return for the first handleFireWeapon call.
         */
        //Setup the status effect.
        FiredWeaponSummary fakeFiredWeaponSummary = new FiredWeaponSummary().getEmptySummary();

        Ignite fakeIgnite = new Ignite();
        fakeIgnite.setDamageType(CORROSIVE); //Realistically this value would be HEAT, but it doesn't matter.
        fakeIgnite.setDamagePerTick(1234.0); //Realistically this value would be 25.0, but it doesn't matter.
        fakeIgnite.setDuration(0.01);
        fakeIgnite.setNumberOfDamageTicks(2);
        fakeIgnite.setupTimers();
        fakeFiredWeaponSummary.setStatusesApplied(Collections.singletonList(fakeIgnite));
        //Hit properties to be used for the hit and the delay.
        HitProperties fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        fakeFiredWeaponSummary.setHitPropertiesList(Collections.singletonList(fakeHitProperties));

        //DelayedDamageSource and its DamageSource to be returned by the call to handleFireWeapon.
        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.DELAYED, Collections.singletonList(new Damage(HEAT, 25)), 0.01, 0.0);
        DelayedDamageSource fakeDelayedDamageSource = new DelayedDamageSource(fakeDamageSource, fakeHitProperties, fakeDamageSource.getDelay());
        fakeFiredWeaponSummary.setDelayedDamageSources(Collections.singletonList(fakeDelayedDamageSource));

        //The DamageMetrics to be returned for the original call to handleFireWeapon. This says the call did 25 HEAT damage to healths.
        DamageMetrics fakeDamageMetrics = new DamageMetrics.DamageMetricsBuilder().withDamageToHealth().withDamageToShields().withStatusDamageToHealth().withStatusDamageToShields().build();
        fakeDamageMetrics.addDamageToHealth(HEAT, 25.0);
        fakeFiredWeaponSummary.setDamageMetrics(fakeDamageMetrics);
        doAnswer(invocation -> {
            fakeTarget.setStatuses(Collections.singletonList(fakeIgnite));
            fakeIgnite.setTickProgress(fakeIgnite.getTickProgress() + 1);
            return fakeFiredWeaponSummary;
        }).when(mockSimulationHelper).handleFireWeapon(fakeWeapon, fakeSimulationTargets, fakeHeadshotChance); //simulationHelper.handleFireWeapon(weapon, simulationParameters.getSimulationTargets(), simulationParameters.getHeadshotChance());

        /*
         * Setup the mock return for the call for handling the delayed damage sources.
         */
        FiredWeaponSummary fakeDelayedDamageFiredSummary = new FiredWeaponSummary().getEmptySummary();
        fakeDelayedDamageFiredSummary.setHitPropertiesList(Collections.singletonList(fakeHitProperties));

        //The DamageMetrics to be returned for the call to handleDelayedDamageSources. This says the call did 35 HEAT damage to healths.
        DamageMetrics fakeDelayedDamageMetrics = new DamageMetrics.DamageMetricsBuilder().withDamageToHealth().withDamageToShields().withStatusDamageToHealth().withStatusDamageToShields().build();
        fakeDelayedDamageMetrics.addDamageToHealth(HEAT, 35.0);
        fakeDelayedDamageFiredSummary.setDamageMetrics(fakeDelayedDamageMetrics);
        when(mockSimulationHelper.handleDelayedDamageSources(eq(Collections.singletonList(fakeDelayedDamageSource)), anyDouble())).thenReturn(fakeDelayedDamageFiredSummary);

        /*
         * Setup the mock return for the call to handle applying the status procs. This says the call did 25 HEAT damage to healths, but since it was a status the Simulation will add it to statusDamageToHealth.
         */
        DamageMetrics fakeStatusDamageMetrics = new DamageMetrics.DamageMetricsBuilder().withDamageToHealth().withDamageToShields().withStatusDamageToHealth().withStatusDamageToShields().build();
        fakeStatusDamageMetrics.addDamageToHealth(HEAT, 25.0);
        FiredWeaponSummary fakeStatusSummary = new FiredWeaponSummary();
        when(mockSimulationHelper.handleApplyingStatuses(eq(fakeTargetAsList))).thenReturn(fakeStatusSummary); //simulationHelper.handleApplyingStatuses(simulationParameters.getSimulationTargets().getAllTargets());

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        assertEquals(60.0, summary.getFiredWeaponSummary().getDamageMetricsMap().get(fakeTargetName).getDamageToHealth().get(HEAT), 0.0);
        assertEquals(25.0, summary.getFiredWeaponSummary().getDamageMetricsMap().get(fakeTargetName).getStatusDamageToHealth().get(HEAT), 0.0);
        assertEquals(2, summary.getFiredWeaponSummary().getHitPropertiesListMap().get(fakeTargetName).size());
    }

    private void setupDefaultSimulationHelperMocks() {
        when(mockSimulationHelper.handleFireWeapon(any(), any(), anyDouble())).thenReturn(new FiredWeaponSummary());
        when(mockSimulationHelper.handleApplyingStatuses(any())).thenReturn(new FiredWeaponSummary());
    }

    private void setupDefaultConstantValues() {
        fakeDeltaTime = 0.01;
        fakeDuration = 1.0;
        fakeHeadshotChance = 0.15;
    }

    private void setupDefaultWeapon() {
        fakeWeapon = mock(Weapon.class);
        doNothing().when(fakeWeapon).initializeFiringState();
        when(fakeWeapon.firingStateProgressTime(anyDouble())).thenReturn(new Ready(null));
        when(fakeWeapon.getStatusChance()).thenReturn(0.15);
    }

    private void setupDefaultTarget() {
        fakeTarget = new Target();
        fakeTargetName = "bagooder";
        fakeTarget.setTargetName(fakeTargetName);
        fakeTarget.setHealths(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200), new Health(ALLOY, 200)));
        fakeTarget.setStatuses(new ArrayList<>());
    }

    private void setupDefaultSimulationParameters() {
        fakeSimulationParameters = new SimulationParameters();
        fakeSimulationTargets = new SimulationTargets(fakeTarget, null);
        fakeSimulationParameters.setSimulationTargets(fakeSimulationTargets);
        fakeSimulationParameters.setDuration(fakeDuration);
        fakeSimulationParameters.setIterations(1);
        fakeSimulationParameters.setLimitAmmo(false);
        fakeSimulationParameters.setHeadshotChance(fakeHeadshotChance);
        fakeSimulationParameters.setModdedWeapon(fakeWeapon);
    }

    private void setupDefaultConfigMocks() {
        when(mockConfig.getDeltaTime()).thenReturn(fakeDeltaTime);
    }

    private DamageMetrics getFakeDamageMetrics() {
        return new DamageMetrics.DamageMetricsBuilder()
                .withDamageToHealth()
                .withDamageToShields()
                .withStatusDamageToHealth()
                .withStatusDamageToShields()
                .build();
    }

}