package com.bags.simulacrum.Entity;

import com.bags.simulacrum.Armor.Health;
import lombok.Data;

import java.util.List;

@Data
public class Enemy {

    private String name;
    private Faction faction;
    private EnemyType type;
    private List<String> weapons;
    private List<String> abilities;
    private List<Health> health;
    private double headshotMultiplier;
    private List<StatusProc> procImmunities;
    private int baseLevel;
    private int level;

    /*

    Enemy has:

    Multiple healths
        Health can be health, shield, or armor.
        Armor reduces damage taken to health classes that are not shield.

        Example: Ambula
        1100 robotic, 500 shields, 150 alloy armor

        150 alloy armor = 150/(150+300) = 33.33% reduction
        Effective health = 1100 / (1-.333333) + 500 = 2150

    Body multipliers


     */

    public enum Faction {
        GRINEER,
        CORPUS,
        INFESTED
    }

    public enum EnemyType {
        RANGED,
        MELEE
    }

    public enum StatusProc {
        KNOCKBACK,
        WEAKENED,
        BLEED,
        FREEZE,
        CHAIN_LIGHTNING,
        IGNITE,
        POISON,
        BULLET_ATTRACTOR,
        KNOCKDOWN,
        CORROSION,
        TOXIN_CLOUD,
        DISRUPT,
        CONFUSION,
        VIRUS
    }

}
