package com.bags.simulacrum.Entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BodyModifierTest {

    private BodyModifier subject;

    @Before
    public void setup() {
        subject = new BodyModifier(BodyPart.HEAD, 0.50, 0.12);
    }

    @Test
    public void itCanHaveTestCoverage_1() {
        assertEquals(BodyPart.HEAD, subject.getBodyPart());
        assertEquals(0.50, subject.getModifierValue(), 0.0);
        assertEquals(0.12, subject.getChanceToHit(), 0.0);
    }

    @Test
    public void itCanHaveTestCoverage_2() {
        subject = new BodyModifier(BodyPart.HEAD, 0.50);

        assertEquals(BodyPart.HEAD, subject.getBodyPart());
        assertEquals(0.50, subject.getModifierValue(), 0.0);
        assertEquals(0.0, subject.getChanceToHit(), 0.0);
    }

    @Test
    public void itCanHaveTestCoverage_3() {
        subject.setBodyPart(BodyPart.GUN);
        subject.setChanceToHit(0.75);
        subject.setModifierValue(-0.50);
        assertEquals(BodyPart.GUN, subject.getBodyPart());
        assertEquals(-0.50, subject.getModifierValue(), 0.0);
        assertEquals(0.75, subject.getChanceToHit(), 0.0);
    }


}