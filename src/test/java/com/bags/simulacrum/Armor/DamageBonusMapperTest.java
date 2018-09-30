package com.bags.simulacrum.Armor;

import com.bags.simulacrum.Damage.DamageType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DamageBonusMapperTest {

    private DamageBonusMapper damageBonusMapper;

    @Before
    public void setup() {
        damageBonusMapper = new DamageBonusMapper();
    }

    @Test
    public void itMapsDamageBonusesCorrectly_1() {
        HealthClass healthClass = HealthClass.MACHINERY;
        DamageType damageType = DamageType.BLAST;

        double bonus = damageBonusMapper.getBonusModifier(damageType, healthClass);

        assertEquals(0.75, bonus, .001);
    }

    @Test
    public void itMapsDamageBonusesCorrectly_2() {
        HealthClass healthClass = HealthClass.SHIELD;
        DamageType damageType = DamageType.RADIATION;

        double bonus = damageBonusMapper.getBonusModifier(damageType, healthClass);

        assertEquals(-0.25, bonus, .001);
    }

    @Test
    public void itMapsDamageBonusesCorrectly_3() {
        HealthClass healthClass = HealthClass.ALLOY;
        DamageType damageType = DamageType.MAGNETIC;

        double bonus = damageBonusMapper.getBonusModifier(damageType, healthClass);

        assertEquals(-0.50, bonus, .001);
    }

}