package com.bags.simulacrum.Entity;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.StatusProc.StatusProc;
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
    //    private List<StatusProc> procImmunities;
    private int baseLevel;
    private int level;
    /*
        How headshot vs body shot works... if there's no headshot, 100% chance is split across the other body modifiers.
        TODO: Decide if I want to work heads body part into the list. Could remove it at run time when needed. Dunno.
     */
    private List<BodyModifier> bodyModifiers;
    private BodyModifier headBodyModifier;
    private List<StatusProc> statuses;

    public double getHeadModifierValue() {
        return headBodyModifier.getModifierValue();
    }

    public void addHealth(Health health) {
        if (this.health == null) {
            this.health = new ArrayList<>();
        }
        this.health.add(health);
    }

    public void addStatus(StatusProc StatusProc) {
        if (this.statuses == null) {
            this.statuses = new ArrayList<>();
        }
        this.statuses.add(StatusProc);
    }

    public void applyStatus() {
        this.statuses.get(this.statuses.size() - 1).apply(this);
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
