package com.bags.simulacrum.Entity;

import com.bags.simulacrum.Armor.Health;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Target {

    private String name;
    //    private Faction faction;
//    private TargetType type;
    private List<String> weapons;
    private List<String> abilities;
    private List<Health> health;
    private double headshotMultiplier;
    //    private List<StatusProc> procImmunities;
    private int baseLevel;
    private int level;

    public void addHealth(Health health) {
        if (this.health == null) {
            this.health = new ArrayList<>();
        }
        this.health.add(health);
    }

    public enum Faction {
        GRINEER,
        CORPUS,
        INFESTED
    }
//
//    public enum TargetType {
//        RANGED,
//        MELEE
//    }

//    public enum StatusProc {
//        KNOCKBACK,
//        WEAKENED,
//        BLEED,
//        FREEZE,
//        CHAIN_LIGHTNING,
//        IGNITE,
//        POISON,
//        BULLET_ATTRACTOR,
//        KNOCKDOWN,
//        CORROSION,
//        TOXIN_CLOUD,
//        DISRUPT,
//        CONFUSION,
//        VIRUS
//    }

}
