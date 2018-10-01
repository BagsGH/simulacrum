package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.StatusProc.Ignite;
import com.bags.simulacrum.StatusProc.StatusProc;
import com.bags.simulacrum.StatusProc.StatusPropertyMapper;
import com.bags.simulacrum.StatusProc.UnimplementedProc;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class StatusPropertyMapperTest {

    @InjectMocks
    private StatusPropertyMapper subject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void itCanReturnStatusProcDuration() {
        double duration = subject.getStatusProcDuration(DamageType.HEAT);
        assertEquals(6.0, duration, 0.0);
    }

    @Test
    public void itCanReturnStatusProcDuration_Null() {
        double duration = subject.getStatusProcDuration(null);
        assertEquals(0.0, duration, 0.0);
    }

    @Test
    public void itCanReturnStatusProcModifier() {
        double modifier = subject.getStatusProcModifier(DamageType.HEAT);
        assertEquals(2.50, modifier, 0.0);
    }

    @Test
    public void itCanReturnStatusProcModifier_Null() {
        double modifier = subject.getStatusProcModifier(null);
        assertEquals(0.0, modifier, 0.0);
    }

    @Test
    public void itCanReturnAStatusProcClass() {
        StatusProc proc = subject.getStatusProcClass(DamageType.HEAT);
        assertTrue(proc instanceof Ignite);
    }

    @Test
    public void itCanReturnAStatusProcClass_Null() {
        StatusProc proc = subject.getStatusProcClass(null);
        assertTrue(proc instanceof UnimplementedProc);
    }

    @Test
    public void itCanReturnDamageTicks() {
        int ticks = subject.getStatusProcTicks(DamageType.HEAT);
        assertEquals(7, ticks);
    }

    @Test
    public void itCanReturnDamageTicks_Null() {
        int ticks = subject.getStatusProcTicks(null);
        assertEquals(0, ticks);
    }

}