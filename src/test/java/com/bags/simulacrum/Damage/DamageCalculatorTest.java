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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fakeDamage = new Damage(DamageType.HEAT, 50.0);
        fakeHealth = new Health(HealthClass.MACHINERY, 200.0);
        fakeArmor = new Health(HealthClass.FERRITE, 300.0);
        fakeShield = new Health(HealthClass.PROTO_SHIELD, 200.0);
        setupDamageBonusMapperMocks();
    }

    @Test
    public void itCanCalculateDamageWithNoModifiers() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(50.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithBodyPartModifier() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, fakeDamage, 0.0, 0, 3.0);

        assertEquals(150.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithBodyPartModifier_Shield() {
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, null, 0.0, fakeDamage, 0.0, 0, 2.0);

        assertEquals(50.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamage_Shield() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, null, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(25.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateGasDamageIgnoringShields() {
        when(damageBonusMapperMock.getBonus(HealthClass.INFESTED_FLESH, DamageType.GAS)).thenReturn(0.50);
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        fakeDamage.setType(DamageType.GAS);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, null, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClass() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClass() {
        when(damageBonusMapperMock.getBonus(HealthClass.MACHINERY, DamageType.VOID)).thenReturn(-0.50);
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(25.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAHeadshot() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 2.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(100.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithAHeadshot() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 3.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(225.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithACriticalHeadshot() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 3.0, fakeDamage, 3.0, 1, 1.0);

        assertEquals(1350.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithAHeadshot() {
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 3.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithACriticalHeadshot() {
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 3.0, fakeDamage, 3.0, 1, 1.0);

        assertEquals(450.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACritical() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, fakeDamage, 2.0, 1, 1.0);

        assertEquals(100.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel2Critical() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, fakeDamage, 2.0, 2, 1.0);

        assertEquals(150.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel5Critical() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, fakeDamage, 2.0, 5, 1.0);

        assertEquals(300.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACriticalAndBodyPartModifier() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, fakeDamage, 2.0, 1, 3.0);

        assertEquals(300.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACriticalHeadshot() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 2.0, fakeDamage, 2.0, 1, 1.0);

        assertEquals(400.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel2CriticalHeadshot() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 2.0, fakeDamage, 2.0, 2, 1.0);

        assertEquals(600.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithACritical() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, fakeDamage, 2.0, 1, 1.0);

        assertEquals(150.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithNoModifiers_Armor() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(25.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithBodyPartModifier_Armor() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 0.0, fakeDamage, 0.0, 0, 3.0);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithBodyPartModifier_Shield_Armor() {
        /* Because of the shields, armor is not taken into account. So we have a 2x modifier from the body part, but 50% reduction from the shield type, so it's still 50.0. */
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeArmor, 0.0, fakeDamage, 0.0, 0, 2.0);

        assertEquals(50.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamage_Shield_Armor() {
        /* Because there are shields, there is no 50% bonus against Infested_Flesh, but there is no 50% reduction from the armor either. Simply a 50% reduction from Heat vs. Proto Shields. */
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeArmor, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(25.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateGasDamageIgnoringShields_Armor() {
        /* Gas has a 50% bonus against Infested Flesh, ignores shields, but there is a 50% reduction from the armor. */
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        fakeDamage.setType(DamageType.GAS);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeArmor, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(38.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClass_Armor() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(38.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClass_Armor() {
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(13, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAHeadshot_Armor() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 2.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(50.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithAHeadshot_Armor() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 3.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(113, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithACriticalHeadshot_Armor() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 3.0, fakeDamage, 3.0, 1, 1.0);

        assertEquals(675.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithAHeadshot_Armor() {
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 3.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(38, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithACriticalHeadshot_Armor() {
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 3.0, fakeDamage, 3.0, 1, 1.0);

        assertEquals(225.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACritical_Armor() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 0.0, fakeDamage, 2.0, 1, 1.0);

        assertEquals(50.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel2Critical_Armor() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 0.0, fakeDamage, 2.0, 2, 1.0);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel5Critical_Armor() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 0.0, fakeDamage, 2.0, 5, 1.0);

        assertEquals(150.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACriticalAndBodyPartModifier_Armor() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 0.0, fakeDamage, 2.0, 1, 3.0);

        assertEquals(150.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithACriticalHeadshot_Armor() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 2.0, fakeDamage, 2.0, 1, 1.0);

        assertEquals(200.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithALevel2CriticalHeadshot_Armor() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 2.0, fakeDamage, 2.0, 2, 1.0);

        assertEquals(300.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithACritical_Armor() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 0.0, fakeDamage, 2.0, 1, 1.0);

        assertEquals(75.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthAndArmorClass() {
        fakeHealth.setHealthClass(HealthClass.SINEW);
        fakeDamage.setType(DamageType.PUNCTURE);
        double actualDamage = subject.calculateDamage(fakeHealth, null, fakeArmor, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(42.0, actualDamage, 0.0);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthAndArmorClassHittingShields() {
        /*  Puncture has a 25% bonus against Sinew and a 50% bonus against Ferrite Armor.
            However, it does 50% less against Proto Shields. Since we have shields, it will do 50% less damage because of the shields and ignore the other bonuses. */
        fakeHealth.setHealthClass(HealthClass.SINEW);
        fakeDamage.setType(DamageType.PUNCTURE);
        double actualDamage = subject.calculateDamage(fakeHealth, fakeShield, fakeArmor, 0.0, fakeDamage, 0.0, 0, 1.0);

        assertEquals(25.0, actualDamage, 0.0);
    }

    private void setupDamageBonusMapperMocks() {
        when(damageBonusMapperMock.getBonus(HealthClass.MACHINERY, DamageType.HEAT)).thenReturn(0.0);
        when(damageBonusMapperMock.getBonus(HealthClass.MACHINERY, DamageType.VOID)).thenReturn(-0.50);
        when(damageBonusMapperMock.getBonus(HealthClass.PROTO_SHIELD, DamageType.HEAT)).thenReturn(-0.50);
        when(damageBonusMapperMock.getBonus(HealthClass.PROTO_SHIELD, DamageType.PUNCTURE)).thenReturn(-0.50);
        when(damageBonusMapperMock.getBonus(HealthClass.INFESTED_FLESH, DamageType.HEAT)).thenReturn(0.50);
        when(damageBonusMapperMock.getBonus(HealthClass.INFESTED_FLESH, DamageType.GAS)).thenReturn(0.50);
        when(damageBonusMapperMock.getBonus(HealthClass.SINEW, DamageType.PUNCTURE)).thenReturn(0.25);
        when(damageBonusMapperMock.getBonus(HealthClass.FERRITE, DamageType.PUNCTURE)).thenReturn(0.50);
    }
}