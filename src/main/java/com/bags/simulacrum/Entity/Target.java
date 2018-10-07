package com.bags.simulacrum.Entity;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Status.Status;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Target {

    private String name;
    private String targetName;
    private List<String> abilities;
    private List<Health> healths;
    private List<DamageType> procImmunities; //TODO: check if immune to proc b4 apoply
    private int baseLevel;
    private int level;
    private Faction faction;
    private TargetType targetType;
    private List<String> weapons;
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
        if (this.healths == null) {
            this.healths = new ArrayList<>();
        }
        this.healths.add(health);
    }

    public void addStatus(Status Status) {
        if (this.statuses == null) {
            this.statuses = new ArrayList<>();
        }
        this.statuses.add(Status);
    }

    public Target copy() {
        Target copy = new Target();
        copy.setName(this.name);
        copy.setTargetName(this.targetName);

        List<String> deepCopiedAbilities = this.abilities != null ? new ArrayList<>(this.abilities) : new ArrayList<>();
        copy.setAbilities(deepCopiedAbilities);

        List<Health> deepCopiedHealths = this.healths != null ? this.healths.stream().map(Health::copy).collect(Collectors.toList()) : new ArrayList<>();
        copy.setHealths(deepCopiedHealths);

        List<DamageType> deepCopiedProcImmunities = this.procImmunities != null ? new ArrayList<>(this.procImmunities) : new ArrayList<>();
        copy.setProcImmunities(deepCopiedProcImmunities);

        copy.setBaseLevel(this.baseLevel);
        copy.setLevel(this.level);
        copy.setFaction(this.faction);
        copy.setTargetType(this.targetType);

        List<String> deepCopiedWeapons = this.weapons != null ? new ArrayList<>(this.weapons) : new ArrayList<>();
        copy.setWeapons(deepCopiedWeapons);

        List<BodyModifier> deepCopiedBodyModifiers = this.bodyModifiers != null ? this.bodyModifiers.stream().map(BodyModifier::copy).collect(Collectors.toList()) : new ArrayList<>(); //TODO: test if the returned list can be removed from
        copy.setBodyModifiers(deepCopiedBodyModifiers);

        if (this.headBodyModifier != null) {
            copy.setHeadBodyModifier(this.headBodyModifier.copy());
        }

        List<Status> deepCopiedStatuses = this.statuses != null ? this.statuses.stream().map(Status::copy).collect(Collectors.toList()) : new ArrayList<>();
        copy.setStatuses(deepCopiedStatuses);

        return copy;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = new ArrayList<>(statuses);
    }

    public boolean isDead() {
        return this.getHealth().getHealthValue() <= 0.0;
    }

    public Health getArmor() {
        return this.healths.stream().filter(h -> HealthClass.isArmor(h.getHealthClass())).findFirst().orElse(null);
    }

    public Health getHealth() {
        return this.healths.stream().filter(h -> (!HealthClass.isArmor(h.getHealthClass()) && !HealthClass.isShield(h.getHealthClass()))).findFirst().orElse(null);
    }

}
