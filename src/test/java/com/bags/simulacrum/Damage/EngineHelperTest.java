package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Entity.BodyModifier;
import com.bags.simulacrum.Entity.BodyPart;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Weapon.Weapon;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
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
    private BodyModifier fakeHeadshotBodyModifier;

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

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_Headshot() {
        when(mockRandom.getRandom()).thenReturn(0.14);
        fakeWeapon.setCriticalChance(0.10);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.15);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(1.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(0.0, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(0, actualHitProperties.getCritLevel());
        assertEquals(0.0, actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_NoHeadshot() {
        when(mockRandom.getRandom()).thenReturn(0.51);
        fakeWeapon.setCriticalChance(0.10);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.15);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(0.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(0.0, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(0, actualHitProperties.getCritLevel());
        assertEquals(0.0, actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_Bodyshot() {
        when(mockRandom.getRandom()).thenReturn(0.15);
        fakeWeapon.setCriticalChance(0.10);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.10);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(0.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(0.50, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(0, actualHitProperties.getCritLevel());
        assertEquals(0.0, actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_Critical() {
        when(mockRandom.getRandom()).thenReturn(0.55);
        fakeWeapon.setCriticalChance(0.56);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(0.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(0.0, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(1, actualHitProperties.getCritLevel());
        assertEquals(fakeWeapon.getCriticalDamage(), actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_Critical_Headshot() {
        when(mockRandom.getRandom()).thenReturn(0.55);
        fakeWeapon.setCriticalChance(0.56);
        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.60);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(1.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(0.0, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(1, actualHitProperties.getCritLevel());
        assertEquals(fakeWeapon.getCriticalDamage(), actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itCallsTargetDamageHelperWithCorrectValues_Critical_Bodyshot() {
        when(mockRandom.getRandom()).thenReturn(0.45);
        fakeWeapon.setCriticalChance(0.46);
        subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.30);

        ArgumentCaptor<HitProperties> hitPropertiesCaptor = ArgumentCaptor.forClass(HitProperties.class);

        verify(mockTargetDamageHelper).applyDamageSourceDamageToTarget(any(), hitPropertiesCaptor.capture(), any());
        HitProperties actualHitProperties = hitPropertiesCaptor.getValue();
        assertEquals(0.0, actualHitProperties.getHeadshotModifier(), 0.0);
        assertEquals(0.50, actualHitProperties.getBodyPartModifier(), 0.0);
        assertEquals(1, actualHitProperties.getCritLevel());
        assertEquals(fakeWeapon.getCriticalDamage(), actualHitProperties.getCriticalDamageMultiplier(), 0.0);
    }

    @Test
    public void itReturnsADelayedDamageSource() {
        fakeDamageSource.setDamageSourceType(DamageSourceType.DELAYED_AOE);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        assertEquals(1, firedWeaponSummary.getDelayedDamageSources().size());
        assertEquals(fakeDamageSource, firedWeaponSummary.getDelayedDamageSources().get(0).getDamageSource());
    }

    @Test
    public void itReturnsDelayedDamageSources() {
        fakeDamageSource.setDamageSourceType(DamageSourceType.DELAYED_AOE);
        DamageSource anotherFakeDamageSource = new DamageSource(DamageSourceType.DELAYED, Collections.singletonList(new Damage(DamageType.IMPACT, 50.0)));
        fakeWeapon.setDamageSources(Arrays.asList(fakeDamageSource, anotherFakeDamageSource));

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        assertEquals(2, firedWeaponSummary.getDelayedDamageSources().size());
        assertEquals(fakeDamageSource, firedWeaponSummary.getDelayedDamageSources().get(0).getDamageSource());
        assertEquals(anotherFakeDamageSource, firedWeaponSummary.getDelayedDamageSources().get(1).getDamageSource()); //TODO: don't assume order?
    }

    @Test
    public void itReturnsADamageSummary() {
        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        assertEquals(fakeDamageToHealth, firedWeaponSummary.getDamageSummary().getDamageToHealth());
        assertEquals(fakeDamageToShields, firedWeaponSummary.getDamageSummary().getDamageToShields());
    }

    @Test
    public void itReturnsADamageSummaryWithValuesReturnedByHelpers_Health() {
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        assertEquals(fakeDamageToHealth, firedWeaponSummary.getDamageSummary().getDamageToHealth());
        assertEquals(fakeDamageToShields, firedWeaponSummary.getDamageSummary().getDamageToShields());

        assertEquals(50.0, firedWeaponSummary.getDamageSummary().getDamageToHealth().get(DamageType.HEAT), 0.0);
    }

    @Test
    public void itSumsDamageSummariesFromMultipleDamageSources_Health() {
        fakeDamageToHealth.put(DamageType.HEAT, 50.0);
        Map<DamageType, Double> anotherFakeDamageToHealth = DamageSummary.initialDamageMap();
        Map<DamageType, Double> anotherFakeDamageToShields = DamageSummary.initialDamageMap();
        anotherFakeDamageToHealth.put(DamageType.HEAT, 75.0);
        DamageSummary anotherFakeDamageSummary = new DamageSummary(fakeTarget, anotherFakeDamageToHealth, anotherFakeDamageToShields);
        when(mockTargetDamageHelper.applyDamageSourceDamageToTarget(any(), any(), any())).thenReturn(fakeDamageSummary).thenReturn(anotherFakeDamageSummary);
        DamageSource anotherFakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.HEAT, 1234.0)));
        fakeWeapon.setDamageSources(Arrays.asList(fakeDamageSource, anotherFakeDamageSource));

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        assertEquals(125.0, firedWeaponSummary.getDamageSummary().getDamageToHealth().get(DamageType.HEAT), 0.0);
    }

    @Test
    public void itReturnsADamageSummaryWithValuesReturnedByHelpers_Shields() {
        fakeDamageToShields.put(DamageType.HEAT, 50.0);

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        assertEquals(fakeDamageToHealth, firedWeaponSummary.getDamageSummary().getDamageToHealth());
        assertEquals(fakeDamageToShields, firedWeaponSummary.getDamageSummary().getDamageToShields());

        assertEquals(50.0, firedWeaponSummary.getDamageSummary().getDamageToShields().get(DamageType.HEAT), 0.0);
    }

    @Test
    public void itSumsDamageSummariesFromMultipleDamageSources_Shields() {
        fakeDamageToShields.put(DamageType.HEAT, 50.0);
        Map<DamageType, Double> anotherFakeDamageToHealth = DamageSummary.initialDamageMap();
        Map<DamageType, Double> anotherFakeDamageToShields = DamageSummary.initialDamageMap();
        anotherFakeDamageToShields.put(DamageType.HEAT, 75.0);
        DamageSummary anotherFakeDamageSummary = new DamageSummary(fakeTarget, anotherFakeDamageToHealth, anotherFakeDamageToShields);
        when(mockTargetDamageHelper.applyDamageSourceDamageToTarget(any(), any(), any())).thenReturn(fakeDamageSummary).thenReturn(anotherFakeDamageSummary);
        DamageSource anotherFakeDamageSource = new DamageSource(DamageSourceType.PROJECTILE, Collections.singletonList(new Damage(DamageType.HEAT, 1234.0)));
        fakeWeapon.setDamageSources(Arrays.asList(fakeDamageSource, anotherFakeDamageSource));

        FiredWeaponSummary firedWeaponSummary = subject.handleFireWeapon(fakeWeapon, fakeTarget, 0.0);

        assertEquals(125.0, firedWeaponSummary.getDamageSummary().getDamageToShields().get(DamageType.HEAT), 0.0);
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
        fakeHeadshotBodyModifier = new BodyModifier(BodyPart.HEAD, 1.0);
        fakeTarget.setBodyModifiers(Collections.singletonList(fakeBodyModifier));
        fakeTarget.setHeadshotModifier(fakeHeadshotBodyModifier);
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