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

    private Fired fakeFired;
    private Charging fakeCharging;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        fakeFired = new Fired(null, null);
        fakeCharging = new Charging(null);

        setupDefaultConstantValues();
        setupDefaultConfigMocks();
        setupDefaultTarget();
        setupDefaultWeapon();
        setupDefaultSimulationParameters();
        setupDefaultSimulationHelperMocks();
        fakeTargetAsList = new ArrayList<>(Collections.singletonList(fakeTarget));
    }

    @Test
    public void itNeverCallsSimulationHelperForDelayedDamageSourceHandlingWithoutDelayedDamageSources() {
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
//    public void itHandlesDelayedDamageSourcesWithShortDelayOnTheNextDeltaTime() { //TODO: cannot figure out why this didnt work
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

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper).handleApplyingStatuses(eq(fakeTargetAsList));
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

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper).handleApplyingStatuses(eq(fakeTargetAsList));
        assertEquals(0, fakeTarget.getStatuses().size());
    }

    @Test
    public void itReturnsSimulationSummaryWithInformationAboutTimeSpentInEachWeaponState() {
        fakeSimulationParameters.setDuration(0.10);
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
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeFired);

        FiredWeaponSummary fakeFiredWeaponSummary = new FiredWeaponSummary();
        doAnswer(invocation -> {
            fakeTarget.getHealth().setHealthValue(0.0);
            return fakeFiredWeaponSummary;
        }).when(mockSimulationHelper).handleFireWeapon(eq(fakeWeapon), eq(fakeSimulationTargets), eq(fakeHeadshotChance));

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        assertEquals(1, summary.getKilledTargets().size());
        assertEquals(fakeTarget, summary.getKilledTargets().get(0));
    }

    @Test
    public void itAddsDamageAppliedByADelayedDamageSourceToTheRunningTotal() {
        fakeSimulationParameters.setDuration(fakeDeltaTime + fakeDeltaTime);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging);

        FiredWeaponSummary fakeFiredWeaponSummary = new FiredWeaponSummary();
        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.DELAYED, Collections.singletonList(new Damage(HEAT, 25)), 0.01, 0.0);
        HitProperties fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        DelayedDamageSource fakeDelayedDamageSource = new DelayedDamageSource(fakeTarget, fakeDamageSource, fakeHitProperties, fakeDamageSource.getDelay());
        fakeFiredWeaponSummary.setDelayedDamageSources(Collections.singletonList(fakeDelayedDamageSource));

        when(mockSimulationHelper.handleFireWeapon(eq(fakeWeapon), eq(fakeSimulationTargets), eq(fakeHeadshotChance))).thenReturn(fakeFiredWeaponSummary);

        DamageMetrics fakeDamageMetrics = new DamageMetrics();
        fakeDamageMetrics.addDamageToHealth(HEAT, 25.0);

        FiredWeaponSummary fakeDelayedDamageFiredSummary = new FiredWeaponSummary();
        fakeDelayedDamageFiredSummary.addDamageToHealth(fakeTargetName, fakeDamageMetrics.getDamageToHealth());
        fakeDelayedDamageFiredSummary.addHitProperties(fakeTargetName, fakeHitProperties);

        when(mockSimulationHelper.handleDelayedDamageSources(eq(Collections.singletonList(fakeDelayedDamageSource)), anyDouble())).thenReturn(fakeDelayedDamageFiredSummary);

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        assertEquals(25.0, summary.getFiredWeaponSummary().getDamageMetricsMap().get(fakeTargetName).getDamageToHealth().get(HEAT), 0.0);
        assertEquals(1, summary.getFiredWeaponSummary().getHitPropertiesListMap().get(fakeTargetName).size());
    }

    @Test
    public void itAddsDamageAppliedByStatusesToRunningTotal() {
        fakeSimulationParameters.setDuration(fakeDeltaTime + fakeDeltaTime);
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
        fakeFiredWeaponSummary.addStatusApplied(fakeTargetName, fakeIgnite);
        doAnswer(invocation -> {
            fakeTarget.setStatuses(Collections.singletonList(fakeIgnite));
            fakeIgnite.setTickProgress(fakeIgnite.getTickProgress() + 1);
            return fakeFiredWeaponSummary;
        }).when(mockSimulationHelper).handleFireWeapon(fakeWeapon, fakeSimulationTargets, fakeHeadshotChance);

        DamageMetrics fakeDamageMetrics = new DamageMetrics();
        fakeDamageMetrics.addDamageToHealth(HEAT, 25.0);
        FiredWeaponSummary fakeStatusSummary = new FiredWeaponSummary();
        fakeStatusSummary.addStatusDamageToHealth(fakeTargetName, fakeDamageMetrics.getDamageToHealth());
        when(mockSimulationHelper.handleApplyingStatuses(fakeTargetAsList)).thenReturn(fakeStatusSummary).thenReturn(new FiredWeaponSummary());

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        assertEquals(25.0, summary.getFiredWeaponSummary().getDamageMetricsMap().get(fakeTargetName).getStatusDamageToHealth().get(HEAT), 0.0);
        assertEquals(fakeIgnite, summary.getFiredWeaponSummary().getStatusesAppliedMap().get(fakeTargetName).get(0));
        assertNull(summary.getFiredWeaponSummary().getHitPropertiesListMap().get(fakeTargetName));
    }

    @Test
    public void itAddsDamagedAppliedByWeaponFiredToRunningTotal() {
        fakeSimulationParameters.setDuration(fakeDeltaTime);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeFired);

        FiredWeaponSummary fakeFiredWeaponSummary = new FiredWeaponSummary();
        HitProperties fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        fakeFiredWeaponSummary.setDelayedDamageSources(new ArrayList<>());
        DamageMetrics fakeDamageMetrics = new DamageMetrics();
        fakeDamageMetrics.addDamageToHealth(HEAT, 25.0);
        fakeFiredWeaponSummary.addDamageToHealth(fakeTargetName, fakeDamageMetrics.getDamageToHealth());
        fakeFiredWeaponSummary.addHitProperties(fakeTargetName, fakeHitProperties);

        when(mockSimulationHelper.handleFireWeapon(fakeWeapon, fakeSimulationTargets, fakeHeadshotChance)).thenReturn(fakeFiredWeaponSummary);

        SimulationSummary summary = subject.runSimulation(fakeSimulationParameters);

        assertEquals(25.0, summary.getFiredWeaponSummary().getDamageMetricsMap().get(fakeTargetName).getDamageToHealth().get(HEAT), 0.0);
        assertEquals(1, summary.getFiredWeaponSummary().getHitPropertiesListMap().get(fakeTargetName).size());
    }

    @Test
    public void itAddsAllDamageReturnedByHelperCalls() {
        fakeSimulationParameters.setDuration(fakeDeltaTime + fakeDeltaTime);
        when(fakeWeapon.firingStateProgressTime(anyDouble()))
                .thenReturn(fakeFired)
                .thenReturn(fakeCharging);
        /*
         * Setup the mock return for the first handleFireWeapon call.
         */
        //Setup the status effect.
        FiredWeaponSummary fakeFiredWeaponSummary = new FiredWeaponSummary();

        Ignite fakeIgnite = new Ignite();
        fakeIgnite.setDamageType(CORROSIVE); //Realistically this value would be HEAT, but it doesn't matter.
        fakeIgnite.setDamagePerTick(1234.0); //Realistically this value would be 25.0, but it doesn't matter.
        fakeIgnite.setDuration(0.01);
        fakeIgnite.setNumberOfDamageTicks(2);
        fakeIgnite.setupTimers();
        fakeFiredWeaponSummary.addStatusApplied(fakeTargetName, fakeIgnite);
        //Hit properties to be used for the hit and the delay.
        HitProperties fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        fakeFiredWeaponSummary.addHitProperties(fakeTargetName, fakeHitProperties);

        //DelayedDamageSource and its DamageSource to be returned by the call to handleFireWeapon.
        DamageSource fakeDamageSourceForDelayedDamage = new DamageSource(DamageSourceType.DELAYED, Collections.singletonList(new Damage(HEAT, 25)), 0.01, 0.0);
        DelayedDamageSource fakeDelayedDamageSource = new DelayedDamageSource(fakeTarget, fakeDamageSourceForDelayedDamage, fakeHitProperties, fakeDamageSourceForDelayedDamage.getDelay());
        fakeFiredWeaponSummary.setDelayedDamageSources(Collections.singletonList(fakeDelayedDamageSource));

        //The DamageMetrics to be returned for the original call to handleFireWeapon. This says the call did 25 HEAT damage to healths.
        DamageMetrics fakeDamageMetrics = new DamageMetrics();
        fakeDamageMetrics.addDamageToHealth(HEAT, 25.0);
        fakeFiredWeaponSummary.addDamageToHealth(fakeTargetName, fakeDamageMetrics.getDamageToHealth());
        doAnswer(invocation -> {
            fakeTarget.setStatuses(Collections.singletonList(fakeIgnite));
            fakeIgnite.setTickProgress(fakeIgnite.getTickProgress() + 1);
            return fakeFiredWeaponSummary;
        }).when(mockSimulationHelper).handleFireWeapon(fakeWeapon, fakeSimulationTargets, fakeHeadshotChance);

        /*
         * Setup the mock return for the call for handling the delayed damage sources.
         */
        FiredWeaponSummary fakeDelayedDamageFiredSummary = new FiredWeaponSummary();
        fakeDelayedDamageFiredSummary.addHitProperties(fakeTargetName, fakeHitProperties);

        //The DamageMetrics to be returned for the call to handleDelayedDamageSources. This says the call did 35 HEAT damage to healths.
        DamageMetrics fakeDelayedDamageMetrics = new DamageMetrics();
        fakeDelayedDamageMetrics.addDamageToHealth(HEAT, 35.0);
        fakeDelayedDamageFiredSummary.addDamageToHealth(fakeTargetName, fakeDelayedDamageMetrics.getDamageToHealth());

        when(mockSimulationHelper.handleDelayedDamageSources(eq(Collections.singletonList(fakeDelayedDamageSource)), anyDouble())).thenReturn(fakeDelayedDamageFiredSummary);

        /*
         * Setup the mock return for the call to handle applying the status procs. This says the call did 25 HEAT damage to healths, but since it was a status the Simulation will add it to statusDamageToHealth.
         */
        DamageMetrics fakeStatusDamageMetrics = new DamageMetrics();
        fakeStatusDamageMetrics.addDamageToHealth(HEAT, 25.0);
        FiredWeaponSummary fakeStatusSummary = new FiredWeaponSummary();
        fakeStatusSummary.addStatusDamageToHealth(fakeTargetName, fakeStatusDamageMetrics.getDamageToHealth());
        when(mockSimulationHelper.handleApplyingStatuses(eq(fakeTargetAsList))).thenReturn(new FiredWeaponSummary()).thenReturn(fakeStatusSummary);

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
}