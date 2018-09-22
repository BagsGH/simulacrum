package com.bags.simulacrum.Damage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;

public class RandomTest {

    @InjectMocks
    private Random subject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void itAlwaysGeneratesValidNumbers() {
        for (int i = 0; i < 1000000; i++) {
            double generatedRandom = subject.getRandom();
            assertTrue(generatedRandom >= 0.0 && generatedRandom < 1.0);
        }
    }
}