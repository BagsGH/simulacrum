package com.bags.simulacrum.Damage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

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
        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.HEAT, 25.0)));

        subject.handleStatusPROC(fakeDamageSource);
    }

    @Test
    public void itHandlesMultipleElementals() {
        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.HEAT, 25.0), new Damage(DamageType.RADIATION, 25.0)));

        subject.handleStatusPROC(fakeDamageSource);
    }

    @Test
    public void itHandlesSingleIPS() {
        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.IMPACT, 25.0)));

        subject.handleStatusPROC(fakeDamageSource);
    }

    @Test
    public void itHandlesMultipleIPS() {
        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.IMPACT, 25.0), new Damage(DamageType.PUNCTURE, 25.0)));

        subject.handleStatusPROC(fakeDamageSource);
    }

    @Test
    public void itHandlesIPSAndElemental() {
        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.HEAT, 25.0), new Damage(DamageType.PUNCTURE, 25.0)));

        subject.handleStatusPROC(fakeDamageSource);
    }

    @Test
    public void itHandlesIPSAndElemental_1() {
        DamageSource fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.HEAT, 25.0), new Damage(DamageType.RADIATION, 25.0), new Damage(DamageType.BLAST, 25.0), new Damage(DamageType.PUNCTURE, 25.0)));

        subject.handleStatusPROC(fakeDamageSource);
    }

}