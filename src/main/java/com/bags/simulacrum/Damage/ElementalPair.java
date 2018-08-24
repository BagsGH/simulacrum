package com.bags.simulacrum.Damage;

import lombok.Data;

@Data
public class ElementalPair {

    private DamageType damageTypeOne;
    private DamageType damageTypeTwo;

    public ElementalPair(DamageType d1, DamageType d2) {
        damageTypeOne = d1;
        damageTypeTwo = d2;
    }

}
