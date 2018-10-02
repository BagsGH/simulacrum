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
    public void itAppliesInstantly() {
        assertTrue(subject.applyInstantly());
    }

    @Test
    public void itStoresItsDamagePerTickAndCanReturnTheDamageSource() {
        assertEquals(fakeDamagePerTick, subject.getDamageTickDamageSource().getDamages().get(0).getDamageValue(), 0.0);
        assertEquals(fakeDamageType, subject.getDamageTickDamageSource().getDamages().get(0).getType());
        assertEquals(1, subject.getDamageTickDamageSource().getDamages().size());
        assertNull(subject.getDamageTickDamageSource().getModifiedInnateDamages());
        assertNull(subject.getDamageTickDamageSource().getAddedElementalDamages());
    }

    @Test
    public void itIsReadyToApplyAfterExpectedTime() {
        for (int i = 0; i < (int) ((fakeDuration / (fakeNumberOfTicks - (subject.applyInstantly() ? 1 : 0))) / fakeDeltaTime); i++) {
            subject.progressTime(fakeDeltaTime);
        }
        assertTrue(subject.checkProgress());
    }

    @Test
    public void immediatelyAfterItAppliesItIsNoLongerReady() {
        for (int i = 0; i < (int) ((fakeDuration / (fakeNumberOfTicks - (subject.applyInstantly() ? 1 : 0))) / fakeDeltaTime); i++) {
            subject.progressTime(fakeDeltaTime);
        }
        subject.apply(null);
        assertFalse(subject.checkProgress());
        assertEquals(1, subject.getTickProgress());
    }

    @Test
    public void itAppliesTheRightNumberOfTotalTicks() {
        int numberOfTicks = 0;
        if (subject.applyInstantly()) {
            subject.apply(null);
            numberOfTicks++;
        }
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
        if (subject.applyInstantly()) {
            subject.apply(null);
        }
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
        if (subject.applyInstantly()) {
            subject.apply(null);
            totalDamage += subject.getDamageTickDamageSource().getDamages().get(0).getDamageValue();
        }
        for (int i = 0; i < fakeDuration / fakeDeltaTime + 1; i++) {
            subject.progressTime(fakeDeltaTime);
            if (subject.checkProgress()) {
                subject.apply(null);
                totalDamage += subject.getDamageTickDamageSource().getDamages().get(0).getDamageValue();
            }

        }
        assertEquals(fakeDamagePerTick * fakeNumberOfTicks, totalDamage, 0.0);
    }

    @Test
    public void itDoesNotAffectTargetWhenApplied() {
        Target fakeTarget = new Target();
        Target expectedTarget = new Target();
        fakeTarget.setHealth(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200), new Health(ALLOY, 200)));
        expectedTarget.setHealth(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200), new Health(ALLOY, 200)));

        if (subject.applyInstantly()) {
            subject.apply(fakeTarget);
        }
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