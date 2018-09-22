package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Entity.Target;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TargetDamageHelperTest {

    @InjectMocks
    private TargetDamageHelper subject;

    @Mock
    private DamageCalculator damageCalculatorMock;

    private DamageSource fakeDamageSource;
    private Damage fakeDamage;

    private Target fakeTarget;
    private Health fakeHealth;
    private Health fakeShield;
    private Health fakeArmor;
    private HitProperties fakeHitProperties;

    private double fakeWeaponCriticalDamageModifier;
    private double fakeHeadshotMultiplier;
    private int fakeCritLevel;
    private double fakeBodyPartModifier;

    private HealthMatcher deadShieldsMatcher;
    private HealthMatcher startingShieldMatcher;
    private HealthMatcher deadArmorMatcher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(damageCalculatorMock.calculateDamage(any(Health.class), any(Health.class), any(Health.class), any(Damage.class), any(HitProperties.class))).thenReturn(50.0);

        setupDefaultFakeDamageSource();
        setupDefaultFakeTarget();
        setupDefaultFakeHitProperties();

        deadShieldsMatcher = new HealthMatcher(new Health(HealthClass.SHIELD, 0.0));
        startingShieldMatcher = new HealthMatcher(new Health(HealthClass.SHIELD, 200.0));
        deadArmorMatcher = new HealthMatcher(new Health(HealthClass.ALLOY, 0));
    }

    @Test
    public void itCanApplyDamageToShields() {
        DamageSummary damageSummary = subject.applyDamageSourceDamageToTarget(fakeDamageSource, fakeHitProperties, fakeTarget);

        assertExpectedHealthExists(damageSummary.getTarget().getHealth(), new Health(HealthClass.SHIELD, 150.0), 0.0);
        verify(damageCalculatorMock).calculateDamage(eq(fakeHealth), eq(fakeShield), eq(fakeArmor), eq(fakeDamage), eq(fakeHitProperties));
        assertEquals(50.0, damageSummary.getDamageToShields().get(DamageType.HEAT), 0.0);
        assertEquals(0.0, damageSummary.getDamageToHealth().get(DamageType.HEAT), 0.0);
    }

    @Test
    public void itCanApplyDamageToHealth() {
        fakeTarget = new Target();
        fakeHealth = new Health(HealthClass.MACHINERY, 200.0);
        fakeTarget.setHealth(Arrays.asList(fakeHealth, new Health(HealthClass.SHIELD, 0.0), new Health(HealthClass.ALLOY, 0.0)));
        when(damageCalculatorMock.calculateDamage(any(Health.class), argThat(deadShieldsMatcher), argThat(deadArmorMatcher), any(Damage.class), any(HitProperties.class))).thenReturn(50.0);

        DamageSummary damageSummary = subject.applyDamageSourceDamageToTarget(fakeDamageSource, fakeHitProperties, fakeTarget);

        assertExpectedHealthExists(damageSummary.getTarget().getHealth(), new Health(HealthClass.MACHINERY, 150.0), 0.0);
        assertEquals(50.0, damageSummary.getDamageToHealth().get(DamageType.HEAT), 0.0);
        assertEquals(0.0, damageSummary.getDamageToShields().get(DamageType.HEAT), 0.0);
    }

    @Test
    public void itCanApplyDamageToShieldsAndHealth() {

        ArgumentCaptor<Damage> damageCaptor = ArgumentCaptor.forClass(Damage.class);
        when(damageCalculatorMock.calculateDamage(eq(fakeHealth), argThat(startingShieldMatcher), eq(fakeArmor), eq(fakeDamage), eq(fakeHitProperties))).thenReturn(250.0);
        when(damageCalculatorMock.calculateDamage(eq(fakeHealth), argThat(deadShieldsMatcher), eq(fakeArmor), damageCaptor.capture(), eq(fakeHitProperties))).thenReturn(35.0);

        DamageSummary damageSummary = subject.applyDamageSourceDamageToTarget(fakeDamageSource, fakeHitProperties, fakeTarget);

        assertExpectedDamageExists(new Damage(DamageType.HEAT, 5.0), damageCaptor.getAllValues(), 0.001);
        assertExpectedHealthExists(damageSummary.getTarget().getHealth(), new Health(HealthClass.SHIELD, 0.0), 0.0);
        assertExpectedHealthExists(damageSummary.getTarget().getHealth(), new Health(HealthClass.MACHINERY, 165.0), 0.0);
        assertEquals(35.0, damageSummary.getDamageToHealth().get(DamageType.HEAT), 0.0);
        assertEquals(200.0, damageSummary.getDamageToShields().get(DamageType.HEAT), 0.0);
    }

    private void setupDefaultFakeHitProperties() {
        fakeWeaponCriticalDamageModifier = 1.0;
        fakeHeadshotMultiplier = 0.0;
        fakeCritLevel = 1;
        fakeBodyPartModifier = 1.0;
        fakeHitProperties = new HitProperties(fakeCritLevel, fakeWeaponCriticalDamageModifier, fakeHeadshotMultiplier, fakeBodyPartModifier);
    }

    private void setupDefaultFakeDamageSource() {
        fakeDamageSource = new DamageSource();
        fakeDamage = new Damage(DamageType.HEAT, 25.0);
        fakeDamageSource.setDamages(Collections.singletonList(fakeDamage));
    }

    private void setupDefaultFakeTarget() {
        fakeTarget = new Target();
        fakeHealth = new Health(HealthClass.MACHINERY, 200.0);
        fakeShield = new Health(HealthClass.SHIELD, 200.0);
        fakeArmor = new Health(HealthClass.ALLOY, 300.0);
        fakeTarget.setHealth(Arrays.asList(fakeArmor, fakeShield, fakeHealth));
    }

    private void assertExpectedHealthExists(List<Health> targetHealth, Health health, double threshold) {
        boolean assertTrue = false;
        for (Health h : targetHealth) {
            if (h.getHealthClass().equals(health.getHealthClass()) && Math.abs(h.getHealthValue() - health.getHealthValue()) <= threshold) {
                assertTrue = true;
            }
        }
        assertTrue(assertTrue);
    }

    private void assertExpectedDamageExists(Damage damageExpected, List<Damage> actualDamages, double accuracyThreshold) {
        assertTrue(actualDamages.stream().anyMatch(damage -> damage.getType().equals(damageExpected.getType()) && Math.abs(damage.getDamageValue() - damageExpected.getDamageValue()) < accuracyThreshold));
    }

    private class HealthMatcher implements ArgumentMatcher<Health> {

        private Health left;

        public HealthMatcher(Health left) {
            this.left = left;
        }

        @Override
        public boolean matches(Health right) {
            return left.getHealthClass().equals(right.getHealthClass()) && Math.abs(left.getHealthValue() - right.getHealthValue()) <= 0.01;
        }
    }
}