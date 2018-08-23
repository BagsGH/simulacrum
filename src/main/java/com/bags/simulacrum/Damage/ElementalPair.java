package com.bags.simulacrum.Damage;

import lombok.Data;

@Data
public class ElementalPair {

    private Damage.DamageType damageOne;
    private Damage.DamageType damageTwo;

    public ElementalPair(Damage.DamageType d1, Damage.DamageType d2) {
        damageOne = d1;
        damageTwo = d2;
    }

}
