package com.bags.simulacrum.Entity;

import com.bags.simulacrum.Armor.Health;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Enemy {

    private String name;
    private Faction faction;
    private EnemyType type;
    private List<String> weapons;
    private List<String> abilities;
    private Health health;
    private Map<String, Double> bodyMultipliers;
    private List<StatusProc> procImmunities;
    private int baseLevel;
    private int level;

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
