package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Entity.Enemy;

import java.util.ArrayList;
import java.util.List;

public class DamageCalculator {

    public List<DamageInflicted> calculateDamageDone(DamageSource damageSource, Enemy enemy) {
        List<DamageInflicted> damageInflicted = new ArrayList<>();
        Health enemyHealth = enemy.getHealth();
        List<Damage> damages = damageSource.getDamages();
        for (Damage d : damages) {
            for (Health h : enemyHealth) {
                //TODO: look up in the wiki for a better plan of attack of all this... plan it out.
            }
        }

        return damageInflicted;
    }
}
