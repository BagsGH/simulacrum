package com.bags.simulacrum.Damage;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Data
public class DamageSource {

    private DamageSourceType damageSourceType;
    private List<Damage> damages;
    private List<Damage> modifiedInnateDamages;
    private List<Damage> addedElementalDamages;
    private double delay;
    private double aoe;

    public DamageSource(DamageSourceType damageSourcetype, List<Damage> damages) {
        this.damages = damages;
        this.damageSourceType = damageSourcetype;
    }

    public DamageSource(DamageSourceType damageSourcetype, List<Damage> damages, double delay, double aoe) {
        this.damages = damages;
        this.damageSourceType = damageSourcetype;
        this.delay = delay;
        this.aoe = aoe;
    }

    public DamageSource copyWithoutDamages() {
        DamageSource copiedDamageSource = new DamageSource();
        copiedDamageSource.setDamageSourceType(this.damageSourceType);
        copiedDamageSource.setAoe(this.aoe);
        copiedDamageSource.setDelay(this.delay);
        return copiedDamageSource;
    }

    public DamageSource copy() {
        DamageSource copiedDamageSource = this.copyWithoutDamages();
        List<Damage> damagesList = this.damages.stream().map(Damage::copy).collect(Collectors.toList());
        copiedDamageSource.setDamages(damagesList);

        List<Damage> modifiedInnateDamageList = emptyIfNull(this.modifiedInnateDamages).stream().map(Damage::copy).collect(Collectors.toList()); //TODO: empty if null will cause an issie if something is removed from it!!
        copiedDamageSource.setModifiedInnateDamages((modifiedInnateDamageList.isEmpty() ? null : modifiedInnateDamageList)); //TODO: shouldnt need this weird thing, but it made a test fail.

        List<Damage> addedElementalDamageList = emptyIfNull(this.addedElementalDamages).stream().map(Damage::copy).collect(Collectors.toList());
        copiedDamageSource.setAddedElementalDamages((addedElementalDamageList.isEmpty() ? null : addedElementalDamageList));


        return copiedDamageSource;
    }

    public DamageSource() {

    }

    public void addDamage(Damage damage) {
        if (damages == null) {
            damages = new ArrayList<>();
        }
        damages.add(damage);
    }

}
