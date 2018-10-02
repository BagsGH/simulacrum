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

import static com.bags.simulacrum.Armor.HealthClass.FLESH;
import static com.bags.simulacrum.Armor.HealthClass.SHIELD;
import static com.bags.simulacrum.Damage.DamageSourceType.PROJECTILE;
import static com.bags.simulacrum.Damage.DamageType.SLASH;
import static com.bags.simulacrum.Damage.DamageType.VIRAL;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class VirusTest {

    private double fakeDuration;
    private int fakeNumberOfTicks;
    private double fakeDeltaTime;

    @Mock
    private StatusFactory statusFactory;

    private Virus subject;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fakeDuration = 2.0;
        fakeNumberOfTicks = 7;
        fakeDeltaTime = 0.01;
        setupDefaultSubject();
    }

    @Test
    public void itAppliesInstantly() {
        assertTrue(subject.applyInstantly());
    }

    @Test
    public void itDoesNotReturnAnyDamages() {
        DamageSource actualDamageSource = subject.getDamageTickDamageSource();

        assertEquals(0, actualDamageSource.getDamages().size());
    }

    @Test
    public void itRemovesCurrentAndMaxHealth() {
        Target fakeTarget = new Target();
        Health fakeHealth = new Health(FLESH, 200);
        fakeHealth.setHealthValue(150);
        fakeTarget.setHealth(Arrays.asList(fakeHealth, new Health(SHIELD, 200)));

        subject.apply(fakeTarget);

        assertEquals(75.0, fakeTarget.getHealthHealth().getHealthValue(), 0.0);
        assertEquals(100, fakeTarget.getHealthHealth().getHealthValueMax(), 0.0);

    }

    @Test
    public void itAddsRemovedCurrentAndMaxHealthWhenRemoved() {
        Target fakeTarget = new Target();
        Target expectedTarget = new Target();
        fakeTarget.setHealth(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200)));
        expectedTarget.setHealth(Arrays.asList(new Health(FLESH, 200), new Health(SHIELD, 200)));

        subject.apply(fakeTarget);

        assertEquals(100, fakeTarget.getHealthHealth().getHealthValueMax(), 0.0);

        subject.removeStatusEffects();

        assertEquals(expectedTarget, fakeTarget);
    }

    @Test
    public void itCanBeFinished() {
        for (int i = 0; i < fakeDuration / fakeDeltaTime; i++) {
            subject.progressTime(fakeDeltaTime);
        }

        assertTrue(subject.finished());
    }


    private void setupDefaultSubject() {
        Virus virus = new Virus();
        virus.setDuration(fakeDuration);
        virus.setNumberOfDamageTicks(fakeNumberOfTicks);
        virus.setDamagePerTick(0);
        virus.setDamageType(SLASH);
        virus.setupTimers();
        when(statusFactory.getStatusProc(any(), eq(VIRAL))).thenReturn(virus);
        subject = (Virus) statusFactory.getStatusProc(new DamageSource(PROJECTILE, Collections.singletonList(new Damage(VIRAL, 50.0))), VIRAL);
    }

}