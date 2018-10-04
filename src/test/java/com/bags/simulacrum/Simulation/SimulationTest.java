package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Configuration.SimulationConfig;
import com.bags.simulacrum.Entity.Target;
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

import static com.bags.simulacrum.Armor.HealthClass.*;
import static org.junit.Assert.assertEquals;
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

    private SimulationParameters fakeSimulationParameters;

    private Target fakeTarget;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        setupDefaultConstantValues();
        setupDefaultConfigMocks();
        setupDefaultTarget();
        setupDefaultWeapon();
        setupDefaultSimulationParameters();
        setupDefaultSimulationHelperMocks();
    }

    @Test
    public void itCallsSimulationHelperToHandleDelayedDamagesEveryDeltaTime() {
        subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper, times((int) (fakeDuration / fakeDeltaTime))).handleDelayedDamageSources(any(), any(), anyDouble());
    }

    @Test
    public void itCallsSimulationHelperToHandleStatusesEveryDeltaTime() {
        subject.runSimulation(fakeSimulationParameters);

        verify(mockSimulationHelper, times((int) (fakeDuration / fakeDeltaTime))).handleApplyingStatuses(any(), any(), anyDouble());
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

    private void setupDefaultSimulationHelperMocks() {
        when(mockSimulationHelper.handleDelayedDamageSources(any(), any(), anyDouble())).thenReturn(new ArrayList<>());
        when(mockSimulationHelper.handleFireWeapon(any(), any(), anyDouble())).thenReturn(new FiredWeaponSummary(new ArrayList<>(), getFakeDamageMetrics(), new ArrayList<>(), new ArrayList<>()));
        when(mockSimulationHelper.handleApplyingStatuses(any(), any(), anyDouble())).thenReturn(new ArrayList<>());
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
    }

    private void setupDefaultTarget() {
        fakeTarget = new Target();
        fakeTarget.setHealth(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200), new Health(ALLOY, 200)));
        fakeTarget.setStatuses(new ArrayList<>());
    }

    private void setupDefaultSimulationParameters() {
        fakeSimulationParameters = new SimulationParameters();
        fakeSimulationParameters.setTargetList(Arrays.asList(fakeTarget));
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
        DamageMetrics finalDamageMetrics = new DamageMetrics.DamageMetricsBuilder()
                .withDamageToHealth()
                .withDamageToShields()
                .withStatusDamageToHealth()
                .withStatusDamageToShields()
                .build();

        return finalDamageMetrics;
    }

}