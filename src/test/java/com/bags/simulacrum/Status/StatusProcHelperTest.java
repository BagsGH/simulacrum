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

        assertTrue(returnedProc instanceof IgniteProc);
        assertEquals(DamageType.HEAT, ((IgniteProc) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesMultipleElementals() {
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);
        fakeDamageToHealth.put(DamageType.RADIATION, 25.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof IgniteProc || returnedProc instanceof ConfusionProc);
        if (returnedProc instanceof IgniteProc) {
            assertEquals(DamageType.HEAT, ((IgniteProc) returnedProc).getDamageType());
        }
        if (returnedProc instanceof ConfusionProc) {
            assertEquals(DamageType.RADIATION, ((ConfusionProc) returnedProc).getDamageType());
        }
    }

    @Test
    public void itHandlesSingleIPS() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof KnockbackProc);
        assertEquals(DamageType.IMPACT, ((KnockbackProc) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesMultipleIPS() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.SLASH, 25.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof KnockbackProc || returnedProc instanceof BleedProc);
        if (returnedProc instanceof KnockbackProc) {
            assertEquals(DamageType.IMPACT, ((KnockbackProc) returnedProc).getDamageType());
        }
        if (returnedProc instanceof BleedProc) {
            assertEquals(DamageType.SLASH, ((BleedProc) returnedProc).getDamageType());
        }
    }

    @Test
    public void itHandlesIPSAndElemental_1() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.74);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof KnockbackProc);
        assertEquals(DamageType.IMPACT, ((KnockbackProc) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesIPSAndElemental_2() {
        fakeDamageToHealth.put(DamageType.IMPACT, 50.0);
        fakeDamageToHealth.put(DamageType.CORROSIVE, 50.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.76);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof CorrosionProc);
        assertEquals(DamageType.CORROSIVE, ((CorrosionProc) returnedProc).getDamageType());
    }

    @Test
    public void itHandlesIPSAndMultipleElementals() {
        fakeDamageToHealth.put(DamageType.IMPACT, 10.0);
        fakeDamageToHealth.put(DamageType.CORROSIVE, 50.0);
        fakeDamageToShields.put(DamageType.RADIATION, 50.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.0);

        StatusProc returnedProc = subject.handleStatusProc(fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof KnockbackProc);
        assertEquals(DamageType.IMPACT, ((KnockbackProc) returnedProc).getDamageType());
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
        when(mockStatusPropertyMapper.getStatusProcClass(eq(DamageType.HEAT))).thenReturn(new IgniteProc().withDamageType(DamageType.HEAT));
        when(mockStatusPropertyMapper.getStatusProcClass(eq(DamageType.RADIATION))).thenReturn(new ConfusionProc().withDamageType(DamageType.RADIATION));
        when(mockStatusPropertyMapper.getStatusProcClass(eq(DamageType.CORROSIVE))).thenReturn(new CorrosionProc().withDamageType(DamageType.CORROSIVE));
        when(mockStatusPropertyMapper.getStatusProcClass(eq(DamageType.SLASH))).thenReturn(new BleedProc().withDamageType(DamageType.SLASH));
        when(mockStatusPropertyMapper.getStatusProcClass(eq(DamageType.IMPACT))).thenReturn(new KnockbackProc().withDamageType(DamageType.IMPACT));
        when(mockStatusPropertyMapper.getStatusProcClass(eq(null))).thenReturn(new UnimplementedProc());
    }
}