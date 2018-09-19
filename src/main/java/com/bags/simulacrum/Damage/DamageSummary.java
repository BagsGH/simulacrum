package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.Map;

@Data
public class DamageSummary {

    private Target target;
    private Map<DamageType, Double> damageToShields;
    private Map<DamageType, Double> damageToHealth;

    public DamageSummary(Target target, Map<DamageType, Double> damageToShields, Map<DamageType, Double> damageToHealth) {
        this.target = target;
        this.damageToShields = damageToShields;
        this.damageToHealth = damageToHealth;
    }
}
