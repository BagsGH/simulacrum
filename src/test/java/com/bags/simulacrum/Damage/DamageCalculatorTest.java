package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DamageCalculatorTest {

    private DamageCalculator subject;
    private Damage fakeDamage;
    private Health fakeHealth;

    @Before
    public void setup() {
        subject = new DamageCalculator();
        fakeDamage = new Damage(DamageType.HEAT, 50.0);
        fakeHealth = new Health(HealthClass.MACHINERY, 200.0);
    }

    @Test
    public void itCanCalculateDamageWithNoModifiers() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, false, fakeDamage, 0.0, 0);

        assertEquals(50.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClass() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, false, fakeDamage, 0.0, 0);

        assertEquals(75.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClass() {
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, false, fakeDamage, 0.0, 0);

        assertEquals(25.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithAHeadshot() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 2.0, false, fakeDamage, 0.0, 0);

        assertEquals(100.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithAHeadshot() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 3.0, false, fakeDamage, 0.0, 0);

        assertEquals(225.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithAHeadshot_OnCorpus() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 3.0, true, fakeDamage, 0.0, 0);

        assertEquals(75.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithACriticalHeadshot() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 3.0, false, fakeDamage, 3.0, 1);

        assertEquals(1350.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithAHeadshot() {
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 3.0, false, fakeDamage, 0.0, 0);

        assertEquals(75.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithAHeadshot_OnCorpus() {
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 3.0, true, fakeDamage, 0.0, 0);

        assertEquals(25.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithANegativeBonusAgainstHealthClassWithACriticalHeadshot() {
        fakeDamage.setType(DamageType.VOID);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 3.0, false, fakeDamage, 3.0, 1);

        assertEquals(450.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithAHeadshot_OnCorpus() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 2.0, true, fakeDamage, 0.0, 0);

        assertEquals(50.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithACritical() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, false, fakeDamage, 2.0, 1);

        assertEquals(100.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithACriticalHeadshot() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 2.0, false, fakeDamage, 2.0, 1);

        assertEquals(400.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithACriticalHeadshot_OnCorpus() {
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 2.0, true, fakeDamage, 2.0, 1);

        assertEquals(100.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCalculateDamageWithAPositiveBonusAgainstHealthClassWithACritical() {
        fakeHealth.setHealthClass(HealthClass.INFESTED_FLESH);
        double actualDamage = subject.calculateDamage(fakeHealth, null, null, 0.0, false, fakeDamage, 2.0, 1);

        assertEquals(150.0, actualDamage, 0.001);
    }

}