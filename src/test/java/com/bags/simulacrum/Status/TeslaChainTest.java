package com.bags.simulacrum.Status;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Entity.Target;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static com.bags.simulacrum.Armor.HealthClass.*;
import static com.bags.simulacrum.Damage.DamageSourceType.PROJECTILE;
import static com.bags.simulacrum.Damage.DamageType.ELECTRICITY;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class TeslaChainTest {

    private TeslaChain subject;

    @Mock
    private StatusFactory statusFactory;

    private double fakeDuration;
    private int fakeNumberOfTicks;
    private double fakeDeltaTime;
    private double fakeDamagePerTick;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fakeDuration = 0.0;
        fakeNumberOfTicks = 1;
        fakeDeltaTime = 0.01;
        fakeDamagePerTick = 55;
        setupDefaultSubject();
    }

    @Test
    public void itAppliesInstantly() {
        assertTrue(subject.applyInstantly());
    }

    @Test
    public void itStoresItsDamagePerTickAndCanReturnTheDamageSource() {
        assertEquals(fakeDamagePerTick, subject.getDamageSource().getDamages().get(0).getDamageValue(), 0.0);
        assertEquals(ELECTRICITY, subject.getDamageSource().getDamages().get(0).getType());
        assertEquals(1, subject.getDamageSource().getDamages().size());
        assertNull(subject.getDamageSource().getModifiedInnateDamages());
        assertNull(subject.getDamageSource().getAddedElementalDamages());
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
            totalDamage += subject.getDamageSource().getDamages().get(0).getDamageValue();
        }
        for (int i = 0; i < fakeDuration / fakeDeltaTime + 1; i++) {
            subject.progressTime(fakeDeltaTime);
            if (subject.checkProgress()) {
                subject.apply(null);
                totalDamage += subject.getDamageSource().getDamages().get(0).getDamageValue();
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

    @Test
    public void itIsNeverReadyToApply() {
        for (int i = 0; i < (int) ((fakeDuration / (fakeNumberOfTicks - (subject.applyInstantly() ? 1 : 0))) / fakeDeltaTime); i++) {
            subject.progressTime(fakeDeltaTime);
        }
        assertFalse(subject.checkProgress());
    }

    private void setupDefaultSubject() {
        TeslaChain teslaChain = new TeslaChain();
        teslaChain.setDuration(fakeDuration);
        teslaChain.setNumberOfDamageTicks(fakeNumberOfTicks);
        teslaChain.setDamagePerTick(fakeDamagePerTick);
        teslaChain.setDamageType(ELECTRICITY);
        teslaChain.setupTimers();
        when(statusFactory.getStatusProc(any(), eq(ELECTRICITY))).thenReturn(teslaChain);
        subject = (TeslaChain) statusFactory.getStatusProc(new DamageSource(PROJECTILE, Collections.singletonList(new Damage(ELECTRICITY, 50.0))), ELECTRICITY);
    }
}