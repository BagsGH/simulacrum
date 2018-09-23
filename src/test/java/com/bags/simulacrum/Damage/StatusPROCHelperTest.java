package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Status.StatusPROC;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class StatusPROCHelperTest {

    @InjectMocks
    private StatusPROCHelper subject;

    @Mock
    private Random mockRandom;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockRandom.getRandom()).thenReturn(0.50);
    }

    @Test
    public void itHandlesSingleElemental() {
        Map<DamageType, Double> fakeDamageToHealth = new HashMap<>();
        Map<DamageType, Double> fakeDamageToShields = new HashMap<>();
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);

        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.HEAT, 25.0)));

        subject.handleStatusPROC(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);
    }

    @Test
    public void itHandlesMultipleElementals() {
        Map<DamageType, Double> fakeDamageToHealth = new HashMap<>();
        Map<DamageType, Double> fakeDamageToShields = new HashMap<>();
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);
        fakeDamageToHealth.put(DamageType.RADIATION, 25.0);

        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.HEAT, 25.0), new Damage(DamageType.RADIATION, 25.0)));

        subject.handleStatusPROC(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);
    }

    @Test
    public void itHandlesSingleIPS() {
        Map<DamageType, Double> fakeDamageToHealth = new HashMap<>();
        Map<DamageType, Double> fakeDamageToShields = new HashMap<>();
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);

        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 25.0)));

        subject.handleStatusPROC(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);
    }

    @Test
    public void itHandlesMultipleIPS() {
        Map<DamageType, Double> fakeDamageToHealth = new HashMap<>();
        Map<DamageType, Double> fakeDamageToShields = new HashMap<>();
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.PUNCTURE, 25.0);

        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.IMPACT, 25.0), new Damage(DamageType.PUNCTURE, 25.0)));

        subject.handleStatusPROC(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);
    }

    @Test
    public void itHandlesIPSAndElemental() {
        Map<DamageType, Double> fakeDamageToHealth = new HashMap<>();
        Map<DamageType, Double> fakeDamageToShields = new HashMap<>();
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);

        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.IMPACT, 25.0), new Damage(DamageType.HEAT, 25.0)));

        subject.handleStatusPROC(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);
    }

    @Test
    public void itHandlesIPSAndMultipleElementals() {
        Map<DamageType, Double> fakeDamageToHealth = new HashMap<>();
        Map<DamageType, Double> fakeDamageToShields = new HashMap<>();
        fakeDamageToHealth.put(DamageType.IMPACT, 10.0);
        fakeDamageToHealth.put(DamageType.CORROSIVE, 50.0);
        fakeDamageToShields.put(DamageType.RADIATION, 50.0);

        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.IMPACT, 25.0), new Damage(DamageType.RADIATION, 25.0), new Damage(DamageType.CORROSIVE, 25.0)));

        StatusPROC p = subject.handleStatusPROC(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);
        System.out.println("");
    }
}