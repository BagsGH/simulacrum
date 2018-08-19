package com.bags.simulacrum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class WeaponModderTest {

    @InjectMocks
    private WeaponModder subject;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        subject = new WeaponModder();
    }

    @Test
    public void itCanCorrectlyCalculatePositiveAccuracy(){
        Mod fakeMod = new Mod();
        fakeMod.setAccuracyIncrease(.3);

        double actualAccuracy = subject.calculateAccuracy(1.00, Collections.singletonList(fakeMod));

        assertEquals(1.30, actualAccuracy, .001);
    }

    @Test
    public void itCanCorrectlyCalculateNegativeAccuracy(){
        Mod fakeMod = new Mod();
        fakeMod.setAccuracyIncrease(-0.3);

        double actualAccuracy = subject.calculateAccuracy(1.00, Collections.singletonList(fakeMod));

        assertEquals(0.70, actualAccuracy, .001);
    }

    @Test
    public void itCanCorrectlyCalculateComplexAccuracy(){
        Mod fakeMod = new Mod();
        fakeMod.setAccuracyIncrease(0.3);
        Mod fakeMod2 = new Mod();
        fakeMod2.setAccuracyIncrease(-0.55);

        double actualAccuracy = subject.calculateAccuracy(1.00, Arrays.asList(fakeMod, fakeMod2));

        assertEquals(0.75, actualAccuracy, .001);
    }

}