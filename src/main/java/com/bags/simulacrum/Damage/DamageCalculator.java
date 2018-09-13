package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Entity.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DamageCalculator {

    private static final double ARMOR_CONSTANT = 300;

    public List<DamageInflicted> calculateDamageDone(DamageSource damageSource, Enemy enemy) {
        List<DamageInflicted> damageInflicted = new ArrayList<>();
        List<Health> enemyHealth = enemy.getHealth(); //TODO: This should probably be the responsibility of the caller.
        List<Health> enemyShields = findShields(enemyHealth);//TODO: This should probably be the responsibility of the caller.
        Health enemyArmor = findArmor(enemyHealth);//TODO: This should probably be the responsibility of the caller.
        double damageReduction = getDamageReduction(enemyArmor);


        return damageInflicted;
    }

    private double getDamageReduction(Health enemyArmor) {
        return enemyArmor != null ? (enemyArmor.getValue() / enemyArmor.getValue() + ARMOR_CONSTANT) : 0.0;
    }

    private Health findArmor(List<Health> health) {
        return health.stream().filter(h -> HealthClass.isArmor(h.getHealthClass())).findFirst().orElse(null);
    }

    private List<Health> findShields(List<Health> health) {
        return health.stream().filter(hc -> HealthClass.isShield(hc.getHealthClass())).collect(Collectors.toList());
    }
}
