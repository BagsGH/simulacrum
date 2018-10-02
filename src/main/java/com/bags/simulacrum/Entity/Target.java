package com.bags.simulacrum.Entity;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Status.Status;
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
    //    private List<Status> procImmunities;
    private int baseLevel;
    private int level;
    /*
        How headshot vs body shot works... if there's no headshot, 100% chance is split across the other body modifiers.
        TODO: Decide if I want to work heads body part into the list. Could remove it at run time when needed. Dunno.
     */
    private List<BodyModifier> bodyModifiers;
    private BodyModifier headBodyModifier;
    private List<Status> statuses;

    public double getHeadModifierValue() {
        return headBodyModifier.getModifierValue();
    }

    public void addHealth(Health health) {
        if (this.health == null) {
            this.health = new ArrayList<>();
        }
        this.health.add(health);
    }

    public void addStatus(Status Status) {
        if (this.statuses == null) {
            this.statuses = new ArrayList<>();
        }
        this.statuses.add(Status);
    }

//    public void applyStatus() {
//        this.statuses.get(this.statuses.size() - 1).apply(this);
//    }

    public List<Status> statusProgressTime(double deltaTime) {
        List<Status> procsToApply = new ArrayList<>();

        statuses.forEach(status -> {
            status.progressTime(deltaTime);
            if (status.checkProgress()) {
                procsToApply.add(status);
            }
        });
        return procsToApply;
    }

    public Health getArmor() {
        return this.health.stream().filter(h -> HealthClass.isArmor(h.getHealthClass())).findFirst().orElse(null);
    }

    public Health getHealthHealth() {
        return this.health.stream().filter(h -> (!HealthClass.isArmor(h.getHealthClass()) && !HealthClass.isShield(h.getHealthClass()))).findFirst().orElse(null);
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

//    public enum Status {
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
