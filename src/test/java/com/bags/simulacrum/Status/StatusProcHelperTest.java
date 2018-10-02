package com.bags.simulacrum.Status;

import com.bags.simulacrum.Configuration.StatusProcConfig;
import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Simulation.RandomNumberGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.bags.simulacrum.Damage.DamageType.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class StatusProcHelperTest {

    @InjectMocks
    private StatusProcHelper subject;

    @Mock
    private RandomNumberGenerator mockRandomNumberGenerator;

    @Mock
    private StatusFactory mockStatusFactory;

    @Mock
    private StatusProcConfig mockStatusProcConfig;

    private Map<DamageType, Double> fakeDamageToHealth;
    private Map<DamageType, Double> fakeDamageToShields;

    private DamageSource fakeDamageSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.50);

        fakeDamageToHealth = new HashMap<>();
        fakeDamageToShields = new HashMap<>();

        when(mockStatusProcConfig.getIpsStatusWeight()).thenReturn(3.0);

        setupMockStatusProcPropertyMapper();

        fakeDamageSource = new DamageSource();
        fakeDamageSource.setModifiedInnateDamages(Collections.singletonList(new Damage(HEAT, 25.0)));
    }

    @Test
    public void itHandlesSingleElemental() {
        fakeDamageToHealth.put(HEAT, 50.0);

        Status returnedProc = subject.constructStatusProc(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Ignite);
    }

    @Test
    public void itHandlesMultipleElementals() {
        fakeDamageToHealth.put(HEAT, 50.0);
        fakeDamageToHealth.put(RADIATION, 25.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.0);

        Status returnedProc = subject.constructStatusProc(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Ignite || returnedProc instanceof Confusion);
        if (returnedProc instanceof Ignite) {
            assertEquals(HEAT, returnedProc.getDamageType());
        }
        if (returnedProc instanceof Confusion) {
            assertEquals(RADIATION, returnedProc.getDamageType());
        }
    }

    @Test
    public void itHandlesSingleIPS() {
        fakeDamageToHealth.put(IMPACT, 50.0);

        Status returnedProc = subject.constructStatusProc(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Knockback);
        assertEquals(IMPACT, returnedProc.getDamageType());
    }

    @Test
    public void itHandlesMultipleIPS() {
        fakeDamageToHealth.put(IMPACT, 50.0);
        fakeDamageToHealth.put(SLASH, 25.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.0);

        Status returnedProc = subject.constructStatusProc(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Knockback || returnedProc instanceof Bleed);
        if (returnedProc instanceof Knockback) {
            assertEquals(IMPACT, returnedProc.getDamageType());
        }
        if (returnedProc instanceof Bleed) {
            assertEquals(SLASH, returnedProc.getDamageType());
        }
    }

    @Test
    public void itHandlesIPSAndElemental_1() {
        fakeDamageToHealth.put(IMPACT, 50.0);
        fakeDamageToHealth.put(HEAT, 50.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.74);

        Status returnedProc = subject.constructStatusProc(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Knockback);
        assertEquals(IMPACT, returnedProc.getDamageType());
    }

    @Test
    public void itHandlesIPSAndElemental_2() {
        fakeDamageToHealth.put(IMPACT, 50.0);
        fakeDamageToHealth.put(CORROSIVE, 50.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.76);

        Status returnedProc = subject.constructStatusProc(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Corrosion);
        assertEquals(CORROSIVE, returnedProc.getDamageType());
    }

    @Test
    public void itHandlesIPSAndMultipleElementals() {
        fakeDamageToHealth.put(IMPACT, 10.0);
        fakeDamageToHealth.put(CORROSIVE, 50.0);
        fakeDamageToShields.put(RADIATION, 50.0);

        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(0.0);

        Status returnedProc = subject.constructStatusProc(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof Knockback);
        assertEquals(IMPACT, returnedProc.getDamageType());
    }

    @Test
    public void nonsenseTestForCoverage() {
        fakeDamageToShields.put(RADIATION, 50.0);
        /*
        TODO: This cannot happen. Find a way to remove this test and keep 100% coverage.
         */
        when(mockRandomNumberGenerator.getRandomPercentage()).thenReturn(1.01);

        Status returnedProc = subject.constructStatusProc(fakeDamageSource, fakeDamageToHealth, fakeDamageToShields);

        assertTrue(returnedProc instanceof UnimplementedStatus);
    }

    private void setupMockStatusProcPropertyMapper() {
        when(mockStatusFactory.getStatusProc(any(), eq(HEAT))).thenReturn(setupExpectedProc(new Ignite(), HEAT)); //TODO: replace any with damageSource
        when(mockStatusFactory.getStatusProc(any(), eq(RADIATION))).thenReturn(setupExpectedProc(new Confusion(), RADIATION));
        when(mockStatusFactory.getStatusProc(any(), eq(CORROSIVE))).thenReturn(setupExpectedProc(new Corrosion(), CORROSIVE));
        when(mockStatusFactory.getStatusProc(any(), eq(SLASH))).thenReturn(setupExpectedProc(new Bleed(), SLASH));
        when(mockStatusFactory.getStatusProc(any(), eq(IMPACT))).thenReturn(setupExpectedProc(new Knockback(), IMPACT));
        when(mockStatusFactory.getStatusProc(any(), eq(null))).thenReturn(setupExpectedProc(new UnimplementedStatus(), VOID));
    }

    private Status setupExpectedProc(Status status, DamageType damageType) {
        status.setDamageType(damageType);
        return status;
    }
}