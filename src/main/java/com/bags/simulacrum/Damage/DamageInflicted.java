package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.HealthClass;
import lombok.Data;

@Data
public class DamageInflicted {

    DamageType damageType;
    HealthClass healthClass;
    double value;
}
