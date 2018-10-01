package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Simulation.RandomNumberGenerator;
import com.bags.simulacrum.StatusProc.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class StatusProcHelperTest {

    @InjectMocks
    private StatusProcHelper subject;

    @Mock
    private RandomNumberGenerator mockRandomNumberGenerator;

    @Mock
    private StatusPropertyMapper mockStatusPropertyMapper;

    private Map<DamageType, Double> fakeDamageToHealth;
    private Map<DamageType, Double> fakeDamageToShields;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.50);

        fakeDamageToHealth = new HashMap<>();
        fakeDamageToShields = new HashMap<>();

        setupMockStatusProcPropertyMapper();
    }

    @Test
    public void itHandlesSingleElemental() {
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Ignite);
        assertEquals(DamageType.HEAT, ((Ignite) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesMultipleElementals() {
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);
        fakeDamageToHealth.put(DamageType.RADIATION, 25.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Ignite || returnedProc instanceof Confusion);
        if (returnedProc instanceof Ignite) {
            assertEquals(DamageType.HEAT, ((Ignite) returnedProc).getDamageType());
        }
        if (returnedProc instanceof Confusion) {
            assertEquals(DamageType.RADIATION, ((Confusion) returnedProc).getDamageType());
        }
    }

    @Test
    public void itHandlesSingleIPS() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Knockback);
        assertEquals(DamageType.IMPACT, ((Knockback) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesMultipleIPS() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.SLASH, 25.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Knockback || returnedProc instanceof Bleed);
        if (returnedProc instanceof Knockback) {
            assertEquals(DamageType.IMPACT, ((Knockback) returnedProc).getDamageType());
        }
        if (returnedProc instanceof Bleed) {
            assertEquals(DamageType.SLASH, ((Bleed) returnedProc).getDamageType());
        }
    }

    @Test
    public void itHandlesIPSAndElemental_1() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.74);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Knockback);
        assertEquals(DamageType.IMPACT, ((Knockback) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesIPSAndElemental_2() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.CORROSIVE, 50.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.76);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Corrosion);
        assertEquals(DamageType.CORROSIVE, ((Corrosion) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesIPSAndMultipleElementals() {
        fakeDamageToHealth.put(DamageType.IMPACT, 10.0);
        fakeDamageToHealth.put(DamageType.CORROSIVE, 50.0);
        fakeDamageToShields.put(DamageType.RADIATION, 50.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Knockback);
        assertEquals(DamageType.IMPACT, ((Knockback) returnedProc).getDamageType());
    }

    @Test
    public void nonsenseTestForCoverage() {
        fakeDamageToShields.put(DamageType.RADIATION, 50.0);
        /*
        TODO: This cannot happen. Find a way to remove this test and keep 100% coverage.
         */
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(1.01);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof UnimplementedProc);
    }

    private void setupMockStatusProcPropertyMapper() {
        when(mockStatusPropertyMapper.getStatusProcClass(eq(DamageType.HEAT))).thenReturn(new Ignite().withDamageType(DamageType.HEAT));
        when(mockStatusPropertyMapper.getStatusProcClass(eq(DamageType.RADIATION))).thenReturn(new Confusion().withDamageType(DamageType.RADIATION));
        when(mockStatusPropertyMapper.getStatusProcClass(eq(DamageType.CORROSIVE))).thenReturn(new Corrosion().withDamageType(DamageType.CORROSIVE));
        when(mockStatusPropertyMapper.getStatusProcClass(eq(DamageType.SLASH))).thenReturn(new Bleed().withDamageType(DamageType.SLASH));
        when(mockStatusPropertyMapper.getStatusProcClass(eq(DamageType.IMPACT))).thenReturn(new Knockback().withDamageType(DamageType.IMPACT));
        when(mockStatusPropertyMapper.getStatusProcClass(eq(null))).thenReturn(new UnimplementedProc());
    }
}