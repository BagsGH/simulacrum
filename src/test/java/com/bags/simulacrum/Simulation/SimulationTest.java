package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Configuration.SimulationConfig;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Weapon.Weapon;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static com.bags.simulacrum.Armor.HealthClass.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimulationTest {

    @InjectMocks
    private Simulation subject;

    @Mock
    private SimulationConfig mockConfig;
    @Mock
    private RandomNumberGenerator mockRandomNumberGenerator;
    @Mock
    private SimulationHelper mockSimulationHelper;
    @Mock
    private TargetDamageHelper mockTargetDamageHelper;

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
    }

    private void setupDefaultConstantValues() {
        fakeDeltaTime = 0.01;
        fakeDuration = 10.0;
        fakeHeadshotChance = 0.15;
    }

    private void setupDefaultWeapon() {
        fakeWeapon = mock(Weapon.class);
    }

    private void setupDefaultTarget() {
        fakeTarget = new Target();
        fakeTarget.setHealth(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200), new Health(ALLOY, 200)));
    }

    private void setupDefaultSimulationParameters() {
        fakeSimulationParameters = new SimulationParameters();
        fakeSimulationParameters.setTargetList(Arrays.asList(fakeTarget));
        fakeSimulationParameters.setDuration(fakeDuration);
        fakeSimulationParameters.setIterations(1);
        fakeSimulationParameters.setLimitAmmo(false);
        fakeSimulationParameters.setHeadshotChance(fakeHeadshotChance);
    }

    private void setupDefaultConfigMocks() {
        when(mockConfig.getDeltaTime()).thenReturn(fakeDeltaTime);
    }


}