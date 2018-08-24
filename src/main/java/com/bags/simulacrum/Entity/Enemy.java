package com.bags.simulacrum.Entity;

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
    private Map<HealthClass, Double> healthClasses;
    private Map<ArmorType, Double> maxArmors;
    private Map<ArmorType, Double> currentArmors;
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

    public enum ArmorType {
        FERRITE_ARMOR,
        ALLOY_ARMOR
    }

    public enum HealthClass {
        CLONED_FLESH,
        MACHINERY,
        FLESH,
        ROBOTIC,
        INFESTED,
        INFESTED_FLESH,
        FOSSILIZED,
        INFESTED_SINEW,
        OBJECT,
        SHIELD,
        PROTO_SHIELD,
        FERRITE_ARMOR,
        ALLOY_ARMOR
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
