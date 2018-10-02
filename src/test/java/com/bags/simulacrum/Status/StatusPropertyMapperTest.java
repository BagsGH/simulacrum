package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static com.bags.simulacrum.Damage.DamageType.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class StatusPropertyMapperTest {

    @InjectMocks
    private StatusPropertyMapper subject;

    private DamageSource fakeDamageSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        fakeDamageSource = new DamageSource();
    }

    @Test
    public void itCanReturnAStatusProcWithAllExpectedFields() {
        fakeDamageSource.setModifiedInnateDamages(Collections.singletonList(new Damage(HEAT, 25.0)));

        Status proc = subject.getStatusProc(fakeDamageSource, HEAT);

        assertTrue(proc instanceof Ignite);
        assertEquals(12.5, proc.getDamagePerTick(), 0.0);
        assertEquals(6.0, proc.getDuration(), 0.0);
        assertEquals(7, proc.getDamageTicks());
    }

    @Test
    public void itCanReturnAStatusProcWithMultipleInnateDamages() {
        fakeDamageSource.setModifiedInnateDamages(Arrays.asList(new Damage(HEAT, 25.0), new Damage(IMPACT, 25.0)));

        Status proc = subject.getStatusProc(fakeDamageSource, HEAT);

        assertTrue(proc instanceof Ignite);
        assertEquals(25.0, proc.getDamagePerTick(), 0.0);
    }

    @Test
    public void itCanReturnAStatusProcWithMultipleInnateDamagesAndAnAddedElementalDamage() {
        fakeDamageSource.setModifiedInnateDamages(Arrays.asList(new Damage(HEAT, 25.0), new Damage(IMPACT, 25.0)));
        fakeDamageSource.setAddedElementalDamages(Collections.singletonList(new Damage(HEAT, 25.0)));

        Status proc = subject.getStatusProc(fakeDamageSource, HEAT);

        assertTrue(proc instanceof Ignite);
        assertEquals(37.5, proc.getDamagePerTick(), 0.0);
    }

    @Test
    public void itCanReturnAStatusProcWithMultipleInnateDamagesAndAnAddedElementalDamageButTheProcTypeHasNoDamageAssociatedWithIt() {
        fakeDamageSource.setModifiedInnateDamages(Arrays.asList(new Damage(HEAT, 25.0), new Damage(IMPACT, 25.0)));
        fakeDamageSource.setAddedElementalDamages(Collections.singletonList(new Damage(HEAT, 25.0)));

        Status proc = subject.getStatusProc(fakeDamageSource, BLAST);

        assertTrue(proc instanceof Knockdown);
        assertEquals(0.0, proc.getDamagePerTick(), 0.0);
    }

    @Test
    public void itCanReturnAStatusProc_Null() {
        Status proc = subject.getStatusProc(fakeDamageSource, null);

        assertTrue(proc instanceof UnimplementedStatus);
        assertEquals(0.0, proc.getDamagePerTick(), 0.0);
    }
}