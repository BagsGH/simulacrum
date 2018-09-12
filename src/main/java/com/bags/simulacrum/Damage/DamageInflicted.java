package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.HealthType;
import lombok.Data;

@Data
public class DamageInflicted {

    DamageType damageType;
    HealthType healthType;
    double value;
}
