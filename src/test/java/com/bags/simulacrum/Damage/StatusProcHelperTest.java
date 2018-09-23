package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Status.CorrosionProc;
import com.bags.simulacrum.Status.IgniteProc;
import com.bags.simulacrum.Status.KnockbackProc;
import com.bags.simulacrum.Status.StatusProc;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class StatusProcHelperTest {

    @InjectMocks
    private StatusProcHelper subject;

    @Mock
    private Random mockRandom;

    private Map<DamageType, Double> fakeDamageToHealth;
    private Map<DamageType, Double> fakeDamageToShields;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        when(mockRandom.getRandom()).thenReturn(0.50);

        fakeDamageToHealth = new HashMap<>();
        fakeDamageToShields = new HashMap<>();
    }

    @Test
    public void itHandlesSingleElemental() {
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof IgniteProc);
        assertEquals(DamageType.HEAT, ((IgniteProc) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesMultipleElementals() { //TODO: might need to fix test, order not gaurunteed
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);
        fakeDamageToHealth.put(DamageType.RADIATION, 25.0);

        when(mockRandom.getRandom()).thenReturn(0.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof IgniteProc);
        assertEquals(DamageType.HEAT, ((IgniteProc) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesSingleIPS() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof KnockbackProc);
        assertEquals(DamageType.IMPACT, ((KnockbackProc) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesMultipleIPS() { //TODO: might need to fix test, order not gaurunteed
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.PUNCTURE, 25.0);

        when(mockRandom.getRandom()).thenReturn(0.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof KnockbackProc);
        assertEquals(DamageType.IMPACT, ((KnockbackProc) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesIPSAndElemental_1() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);

        when(mockRandom.getRandom()).thenReturn(0.74);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof KnockbackProc);
        assertEquals(DamageType.IMPACT, ((KnockbackProc) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesIPSAndElemental_2() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.CORROSIVE, 50.0);

        when(mockRandom.getRandom()).thenReturn(0.76);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof CorrosionProc);
        assertEquals(DamageType.CORROSIVE, ((CorrosionProc) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesIPSAndMultipleElementals() {
        fakeDamageToHealth.put(DamageType.IMPACT, 10.0);
        fakeDamageToHealth.put(DamageType.CORROSIVE, 50.0);
        fakeDamageToShields.put(DamageType.RADIATION, 50.0);

        when(mockRandom.getRandom()).thenReturn(0.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof KnockbackProc);
        assertEquals(DamageType.IMPACT, ((KnockbackProc) returnedProc).getDamageType());
    }
}