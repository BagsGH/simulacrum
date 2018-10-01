package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Weapon.State.Fired;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeaponStateMetricsTest {

    private WeaponStateMetrics subject;

    @Before
    public void setup() {
        subject = new WeaponStateMetrics();
    }

    @Test
    public void itCanAddValuesToTheMap_1() {
        subject.add(Fired.class, .01);

        assertEquals(1, subject.getValue(Fired.class));
    }

    @Test
    public void itCanAddValuesToTheMap_2() {
        subject.add(Fired.class, .1);

        assertEquals(10, subject.getValue(Fired.class));
    }

    @Test
    public void itCanAddValuesToTheMap_3() {
        subject.add(Fired.class, .03);

        assertEquals(3, subject.getValue(Fired.class));
    }

    @Test
    public void classesDefaultValueIsZero() {
        assertEquals(0, subject.getValue(Fired.class));
    }

    @Test
    public void itReturnsZeroForClassesNotInTheMap() {
        assertEquals(0, subject.getValue(WeaponStateMetrics.class));
    }

}