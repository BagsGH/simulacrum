package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DamageCalculatorTest {

    private DamageCalculator subject;
    private Damage fake50HeatDamage;

    @Before
    public void setup() {
        subject = new DamageCalculator();
        fake50HeatDamage = new Damage(DamageType.HEAT, 50.0);
    }

    @Test
    public void itCanCorrectlyCalculateDamageWithNoModifiers() {
        double actualDamage = subject.calculateDamage(new Health(HealthClass.MACHINERY, 200.0), null, null, 0.0, false, fake50HeatDamage, 0.0, 0);

        assertEquals(50.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateDamageWithAPositiveBonusAgainstHealthClass() {
        double actualDamage = subject.calculateDamage(new Health(HealthClass.INFESTED_FLESH, 200.0), null, null, 0.0, false, fake50HeatDamage, 0.0, 0);

        assertEquals(75.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateDamageWithANegativeBonusAgainstHealthClass() {
        fake50HeatDamage.setType(DamageType.COLD);
        double actualDamage = subject.calculateDamage(new Health(HealthClass.INFESTED_FLESH, 200.0), null, null, 0.0, false, fake50HeatDamage, 0.0, 0);

        assertEquals(25.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateDamageWithAHeadshot() {
        double actualDamage = subject.calculateDamage(new Health(HealthClass.MACHINERY, 200.0), null, null, 2.0, false, fake50HeatDamage, 0.0, 0);

        assertEquals(100.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateDamageWithAHeadshot_OnCorpus() {
        double actualDamage = subject.calculateDamage(new Health(HealthClass.MACHINERY, 200.0), null, null, 2.0, true, fake50HeatDamage, 0.0, 0);

        assertEquals(50.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateDamageWithACritical() {
        double actualDamage = subject.calculateDamage(new Health(HealthClass.MACHINERY, 200.0), null, null, 0.0, false, fake50HeatDamage, 2.0, 1);

        assertEquals(100.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateDamageWithACriticalHeadshot() {
        double actualDamage = subject.calculateDamage(new Health(HealthClass.MACHINERY, 200.0), null, null, 2.0, false, fake50HeatDamage, 2.0, 1);

        assertEquals(400.0, actualDamage, 0.001);
    }

    @Test
    public void itCanCorrectlyCalculateDamageWithACriticalHeadshot_OnCorpus() {
        double actualDamage = subject.calculateDamage(new Health(HealthClass.MACHINERY, 200.0), null, null, 2.0, true, fake50HeatDamage, 2.0, 1);

        assertEquals(100.0, actualDamage, 0.001);
    }

}