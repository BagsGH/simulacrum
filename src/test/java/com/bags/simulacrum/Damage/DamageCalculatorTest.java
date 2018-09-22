package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.DamageBonusMapper;
import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class DamageCalculatorTest {

    @InjectMocks
    private DamageCalculator subject;

    @Mock
    private DamageBonusMapper damageBonusMapperMock;

    private Damage fakeDamage;
    private Health fakeHealth;
    private Health fakeArmor;
    private Health fakeShield;
    private Health fakeNoShield;
    private Health fakeNoArmor;
    private HitProperties fakeHitProperties;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupDefaultFakeHealth();
        setupDamageBonusMapperMocks();
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithNoModifiers() {
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(50.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithBodyPartModifier() {
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 2.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(150.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithBodyPartModifier_Shield() {
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 1.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(50.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamage_Shield() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(25.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateGasDamageIgnoringShields() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        fakeDamage.setType(DamageType.GAS);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClass() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClass() {
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(25.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAHeadshot() {
        fakeHitProperties = new HitProperties(0, 0.0, 1.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(100.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithAHeadshot() {
        fakeHitProperties = new HitProperties(0, 0.0, 2.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(225.0, actualDamage, 0.0);
    }


    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithACriticalHeadshot() {
        fakeHitProperties = new HitProperties(1, 3.0, 2.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(1350.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithAHeadshot() {
        fakeHitProperties = new HitProperties(0, 0.0, 2.0, 0.0);
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithACriticalHeadshot() {
        fakeHitProperties = new HitProperties(1, 3.0, 2.0, 0.0);
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(450.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACritical() {
        fakeHitProperties = new HitProperties(1, 2.0, 0.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(100.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel2Critical() {
        fakeHitProperties = new HitProperties(2, 2.0, 0.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(150.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel5Critical() {
        fakeHitProperties = new HitProperties(5, 2.0, 0.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(300.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACriticalAndBodyPartModifier() {
        fakeHitProperties = new HitProperties(1, 2.0, 0.0, 2.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(300.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACriticalHeadshot() {
        fakeHitProperties = new HitProperties(1, 2.0, 1.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(400.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel2CriticalHeadshot() {
        fakeHitProperties = new HitProperties(2, 2.0, 1.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(600.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithACritical() {
        fakeHitProperties = new HitProperties(1, 2.0, 0.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeNoArmor, fakeDamage, fakeHitProperties);

        assertEquals(150.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithNoModifiers_Armor() {
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(25.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithBodyPartModifier_Armor() {
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 2.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithBodyPartModifier_Shield_Armor() {
        /* Because of the shields, armor is not taken into account. So we have a 2x modifier from the body part, but 50% reduction from the shield type, so it's still 50.0. */
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 1.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(50.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamage_Shield_Armor() {
        /* Because there are shields, there is no 50% bonus against Infested_Flesh, but there is no 50% reduction from the armor either. Simply a 50% reduction from Heat vs. Proto Shields. */
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(25.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateGasDamageIgnoringShields_Armor() {
        /* Gas has a 50% bonus against Infested Flesh, ignores shields, but there is a 50% reduction from the armor. */
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        fakeDamage.setType(DamageType.GAS);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(38.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClass_Armor() {
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(38.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClass_Armor() {
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(13, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAHeadshot_Armor() {
        fakeHitProperties = new HitProperties(0, 0.0, 1.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(50.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithAHeadshot_Armor() {
        fakeHitProperties = new HitProperties(0, 0.0, 2.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(113, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithACriticalHeadshot_Armor() {
        fakeHitProperties = new HitProperties(1, 3.0, 2.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(675.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithAHeadshot_Armor() {
        fakeHitProperties = new HitProperties(0, 0.0, 2.0, 0.0);
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(38, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithACriticalHeadshot_Armor() {
        fakeHitProperties = new HitProperties(1, 3.0, 2.0, 0.0);
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(225.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACritical_Armor() {
        fakeHitProperties = new HitProperties(1, 2.0, 0.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(50.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel2Critical_Armor() {
        fakeHitProperties = new HitProperties(2, 2.0, 0.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel5Critical_Armor() {
        fakeHitProperties = new HitProperties(5, 2.0, 0.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(150.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACriticalAndBodyPartModifier_Armor() {
        fakeHitProperties = new HitProperties(1, 2.0, 0.0, 2.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(150.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACriticalHeadshot_Armor() {
        fakeHitProperties = new HitProperties(1, 2.0, 1.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(200.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel2CriticalHeadshot_Armor() {
        fakeHitProperties = new HitProperties(2, 2.0, 1.0, 0.0);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(300.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithACritical_Armor() {
        fakeHitProperties = new HitProperties(1, 2.0, 0.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthAndArmorClass() {
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.SINEW);
        fakeDamage.setType(DamageType.PUNCTURE);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeNoShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(42.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthAndArmorClassHittingShields() {
        /*  Puncture has a 25% bonus against Sinew and a 50% bonus against Ferrite Armor.
            However, it does 50% less against Proto Shields. Since we have shields, it will do 50% less damage because of the shields and ignore the other bonuses. */
        fakeHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        fakeHealth.setHealthClass(HealthClass.SINEW);
        fakeDamage.setType(DamageType.PUNCTURE);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeArmor, fakeDamage, fakeHitProperties);

        assertEquals(25.0, actualDamage, 0.0);
    }

    private void setupDamageBonusMapperMocks() {
        when(damageBonusMapperMock.getBonus(DamageType.HEAT, HealthClass.MACHINERY)).thenReturn(0.0);
        when(damageBonusMapperMock.getBonus(DamageType.VOID, HealthClass.MACHINERY)).thenReturn(-0.50);
        when(damageBonusMapperMock.getBonus(DamageType.HEAT, HealthClass.PROTO_SHIELD)).thenReturn(-0.50);
        when(damageBonusMapperMock.getBonus(DamageType.PUNCTURE, HealthClass.PROTO_SHIELD)).thenReturn(-0.50);
        when(damageBonusMapperMock.getBonus(DamageType.HEAT, HealthClass.INFESTED_FLESH)).thenReturn(0.50);
        when(damageBonusMapperMock.getBonus(DamageType.GAS, HealthClass.INFESTED_FLESH)).thenReturn(0.50);
        when(damageBonusMapperMock.getBonus(DamageType.PUNCTURE, HealthClass.SINEW)).thenReturn(0.25);
        when(damageBonusMapperMock.getBonus(DamageType.PUNCTURE, HealthClass.FERRITE)).thenReturn(0.50);
    }

    private void setupDefaultFakeHealth() {
        fakeDamage = new Damage(DamageType.HEAT, 50.0);
        fakeHealth = new Health(HealthClass.MACHINERY, 200.0);
        fakeArmor = new Health(HealthClass.FERRITE, 300.0);
        fakeNoArmor = new Health(HealthClass.FERRITE, 0.0);
        fakeShield = new Health(HealthClass.PROTO_SHIELD, 200.0);
        fakeNoShield = new Health(HealthClass.PROTO_SHIELD, 0.0);
    }
}