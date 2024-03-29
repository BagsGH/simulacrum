package com.bags.simulacrum.Status;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static com.bags.simulacrum.Armor.HealthClass.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public abstract class StatusDotTest {

    protected StatusDot subject;

    @Mock
    protected StatusFactory statusFactory;

    protected double fakeDuration;
    protected int fakeNumberOfTicks;
    protected double fakeDeltaTime;
    protected double fakeDamagePerTick;
    protected DamageType fakeDamageType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupDefaultSubject();
    }

    @Test
    public void itStoresItsDamagePerTickAndCanReturnTheDamageSource() {
        assertEquals(fakeDamagePerTick, subject.apply(new Target()).getDamages().get(0).getDamageValue(), 0.0);
        assertEquals(fakeDamageType, subject.apply(new Target()).getDamages().get(0).getDamageType());
        assertEquals(1, subject.apply(new Target()).getDamages().size());
        assertNull(subject.apply(new Target()).getModifiedInnateDamages());
        assertNull(subject.apply(new Target()).getAddedElementalDamages());
    }

    @Test
    public void itIsReadyToApplyAfterExpectedTime() {
        for (int i = 0; i < (int) ((fakeDuration / (fakeNumberOfTicks - 1)) / fakeDeltaTime); i++) {
            subject.progressTime(fakeDeltaTime);
        }
        assertTrue(subject.checkProgress());
    }

    @Test
    public void immediatelyAfterItAppliesItIsNoLongerReady() {
        for (int i = 0; i < (int) ((fakeDuration / (fakeNumberOfTicks - 1)) / fakeDeltaTime); i++) {
            subject.progressTime(fakeDeltaTime);
        }
        subject.apply(null);
        assertFalse(subject.checkProgress());
        assertEquals(1, subject.getTickProgress());
    }

    @Test
    public void itAppliesTheRightNumberOfTotalTicks() {
        int numberOfTicks = 1;
        subject.apply(null);

        for (int i = 0; i < fakeDuration / fakeDeltaTime; i++) {
            subject.progressTime(fakeDeltaTime);
            if (subject.checkProgress()) {
                subject.apply(null);
                numberOfTicks++;
            }

        }
        assertEquals(fakeNumberOfTicks, numberOfTicks);
    }

    @Test
    public void itIsFinishedAfterTheDuration() {
        subject.apply(null);
        for (int i = 0; i < fakeDuration / fakeDeltaTime + 1; i++) {
            subject.progressTime(fakeDeltaTime);
            if (subject.checkProgress()) {
                subject.apply(null);
            }

        }
        assertTrue(subject.finished());
    }

    @Test
    public void itReturnsTheTotalExpectedDamage() {
        double totalDamage = 0.0;
        subject.apply(null);
        totalDamage += subject.apply(new Target()).getDamages().get(0).getDamageValue();
        for (int i = 0; i < fakeDuration / fakeDeltaTime + 1; i++) {
            subject.progressTime(fakeDeltaTime);
            if (subject.checkProgress()) {
                subject.apply(null);
                totalDamage += subject.apply(new Target()).getDamages().get(0).getDamageValue();
            }

        }
        assertEquals(fakeDamagePerTick * fakeNumberOfTicks, totalDamage, 0.0);
    }

    @Test
    public void itDoesNotAffectTargetWhenApplied() {
        Target fakeTarget = new Target();
        Target expectedTarget = new Target();
        fakeTarget.setHealths(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200), new Health(ALLOY, 200)));
        expectedTarget.setHealths(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200), new Health(ALLOY, 200)));

        subject.apply(fakeTarget);

        for (int i = 0; i < fakeDuration / fakeDeltaTime + 1; i++) {
            subject.progressTime(fakeDeltaTime);
            if (subject.checkProgress()) {
                subject.apply(fakeTarget);
            }
        }

        assertEquals(expectedTarget, fakeTarget);
    }

    protected abstract void setupDefaultSubject();
}