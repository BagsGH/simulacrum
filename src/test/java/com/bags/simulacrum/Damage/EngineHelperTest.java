package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Entity.BodyModifier;
import com.bags.simulacrum.Entity.BodyPart;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Weapon.Weapon;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EngineHelperTest {

    @InjectMocks
    private EngineHelper subject;

    @Mock
    private Random mockRandom;

    @Mock
    private TargetDamageHelper mockTargetDamageHelper;

    private Map<DamageType, Double> fakeDamageToHealth;
    private Map<DamageType, Double> fakeDamageToShields;

    private Weapon fakeWeapon;

    private DamageSource fakeDamageSource;
    private Damage fakeDamage;

    private Target fakeTarget;
    private Health fakeHealth;
    private Health fakeShields;
    private Health fakeArmor;

    private BodyModifier fakeBodyModifier;

    private DamageSummary fakeDamageSummary;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);


        when(mockRandom.getRandom()).thenReturn(0.50);

        setupDefaultFakeWeapon();
        setupDefaultFakeTarget();
        setupDefaultFakeDamageSummary();

        when(mockTargetDamageHelper.applyDamageSourceDamageToTarget(any(), any(), any())).thenReturn(fakeDamageSummary);
    }

    @Test
    public void itCallsRandomFiveTimesWithoutMultishot() {
        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        verify(mockRandom, times(5)).getRandom();
    }

    @Test
    public void itCallsRandomMoreThanFiveTimesWithMultishot_1() {
        fakeWeapon.setMultishot(2.0);
        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        verify(mockRandom, times(7)).getRandom();
    }

    @Test
    public void itCallsRandomMoreThanFiveTimesWithMultishot_2() {
        fakeWeapon.setMultishot(2.51);
        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        verify(mockRandom, times(9)).getRandom();
    }

    @Test
    public void itCallsRandomMoreThanFiveTimesWithMultishot_3() {
        fakeWeapon.setMultishot(3.0);
        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        verify(mockRandom, times(9)).getRandom();
    }

    @Test
    public void itCallsTargetDamageHelperOnceWithoutMultishot() {
        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        verify(mockTargetDamageHelper, times(1)).applyDamageSourceDamageToTarget(any(), any(), any());
    }

    @Test
    public void itCallsTargetDamageHelperTwiceWithMultishot_1() {
        fakeWeapon.setMultishot(2.0);
        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        verify(mockTargetDamageHelper, times(2)).applyDamageSourceDamageToTarget(any(), any(), any());
    }

    @Test
    public void itCallsTargetDamageHelperTwiceWithMultishot_2() {
        fakeWeapon.setMultishot(2.5);
        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        verify(mockTargetDamageHelper, times(2)).applyDamageSourceDamageToTarget(any(), any(), any());
    }

    @Test
    public void itCallsTargetDamageHelperThreeTimesWithMultishot_3() {
        fakeWeapon.setMultishot(2.51);
        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        verify(mockTargetDamageHelper, times(3)).applyDamageSourceDamageToTarget(any(), any(), any());
    }

    private void setupDefaultFakeDamageSummary() {
        fakeDamageToHealth = DamageSummary.initialDamageMap();
        fakeDamageToShields = DamageSummary.initialDamageMap();
        fakeDamageSummary = new DamageSummary(fakeTarget, fakeDamageToHealth, fakeDamageToShields);
    }

    private void setupDefaultFakeTarget() {
        fakeHealth = new Health(HealthClass.INFESTED_FLESH, 250.0);
        fakeShields = new Health(HealthClass.SHIELD, 250.0);
        fakeArmor = new Health(HealthClass.ALLOY, 300.0);
        fakeTarget = new Target();
        fakeTarget.setHealth(Arrays.asList(fakeHealth, fakeShields, fakeArmor));
        fakeBodyModifier = new BodyModifier(BodyPart.GUN, 0.50, 0.50);
        fakeTarget.setBodyModifiers(Collections.singletonList(fakeBodyModifier));
    }

    private void setupDefaultFakeWeapon() {
        fakeWeapon = new Weapon();
        fakeWeapon.setMultishot(1.0);
        fakeWeapon.setCriticalChance(0.25);
        fakeWeapon.setCriticalDamage(2.0);
        fakeWeapon.setStatusChance(0.15);
        fakeDamage = new Damage(DamageType.HEAT, 25.0);
        fakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(fakeDamage));
        fakeWeapon.setDamageSources(Collections.singletonList(fakeDamageSource));
    }

}