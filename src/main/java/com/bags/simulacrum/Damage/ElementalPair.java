package com.bags.simulacrum.Damage;

import lombok.Data;

@Data
public class ElementalPair {

    private Damage.DamageType damageTypeOne;
    private Damage.DamageType damageTypeTwo;

    public ElementalPair(Damage.DamageType d1, Damage.DamageType d2) {
        damageTypeOne = d1;
        damageTypeTwo = d2;
    }

}
