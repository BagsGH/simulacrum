package com.bags.simulacrum.Damage;

import com.bags.simulacrum.Entity.BodyModifier;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Weapon.Weapon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class EngineHelper {

    private final TargetDamageHelper targetDamageHelper;
    private final Random random;
    private final StatusProcHelper statusPROCHelper;

    @Autowired
    public EngineHelper(TargetDamageHelper targetDamageHelper, Random random, StatusProcHelper statusPROCHelper) {
        this.targetDamageHelper = targetDamageHelper;
        this.random = random;
        this.statusPROCHelper = statusPROCHelper;
    }

    public FiredWeaponMetrics handleFireWeapon(Weapon weapon, Target target, double headshotChance) { //tODO: secondary targets for aoe...?
        List<DelayedDamageSource> delayedDamageSources = new ArrayList<>();
        Map<DamageType, Double> summedDamageToHealth = DamageMetrics.initialDamageMap();
        Map<DamageType, Double> summedDamageToShields = DamageMetrics.initialDamageMap();
        DamageMetrics finalDamageMetrics = new DamageMetrics(target, summedDamageToHealth, summedDamageToShields);

        List<HitProperties> hitPropertiesList = new ArrayList<>();

        double multishotRNG = random.getRandom();
        double headshotRNG = random.getRandom(); //TODO: maybe if the accuracy is bad, calculate this independently for multishot?
        double bodyshotRNG = random.getRandom(); //TODO: maybe if the accuracy is bad, calculate this independently for multishot?

        int multishots = getMultishotLevel(weapon.getMultishot(), multishotRNG);
        double headshotModifier = calculateHeadshotModifier(target, headshotChance, headshotRNG);
        double bodyModifier = calculateBodyModifier(target, headshotChance, headshotRNG, bodyshotRNG);

        for (int i = 0; i < multishots; i++) {
            double criticalHitRNG = random.getRandom();
            double statusProcRNG = random.getRandom(); //TODO: handle procs
            int critLevel = getCritLevel(weapon.getCriticalChance(), criticalHitRNG);
            double weaponCriticalDamageMultiplier = critLevel > 0 ? weapon.getCriticalDamage() : 0.0;
            HitProperties hitProperties = new HitProperties(critLevel, weaponCriticalDamageMultiplier, headshotModifier, bodyModifier);


            for (DamageSource damageSource : weapon.getDamageSources()) {
                if (!isDelayedDamageSource(damageSource)) {
                    DamageMetrics damageMetrics = targetDamageHelper.applyDamageSourceDamageToTarget(damageSource, hitProperties, target);
                    updateRunningTotalDamageToHealth(finalDamageMetrics, damageMetrics.getDamageToHealth());
                    updateRunningTotalDamageToShields(finalDamageMetrics, damageMetrics.getDamageToShields());
                    if (statusProcRNG < weapon.getStatusChance()) {
                        statusPROCHelper.handleStatusProc(damageMetrics.getDamageToHealth(), damageMetrics.getDamageToShields());
                    }
                } else {
                    delayedDamageSources.add(new DelayedDamageSource(damageSource, damageSource.getDelay())); //TODO: calculate crits etc now or later? //TODO: new up a new damageSource?
                }
            }
            hitPropertiesList.add(hitProperties);
        }

        return new FiredWeaponMetrics(hitPropertiesList, finalDamageMetrics, delayedDamageSources);
    }

    private int getMultishotLevel(double weaponMultishotChance, double multishotRNG) {
        int multishot = (int) Math.floor(weaponMultishotChance);
        boolean calculateMultishotChance = weaponMultishotChance % 1.0 > 0;
        if (calculateMultishotChance && multishotRNG < weaponMultishotChance % 1.0) {
            multishot++;
        }
        return multishot;
    }

    private double calculateHeadshotModifier(Target target, double headshotChance, double headshotRNG) {
        return headshotRNG < headshotChance ? target.getHeadshotModifier() : 0.0;
    }

    private double calculateBodyModifier(Target target, double headshotChance, double headshotRNG, double bodyshotRNG) {
        return !(headshotRNG < headshotChance) ? getBodyModifier(bodyshotRNG, target.getBodyModifiers()) : 0.0;
    }

    private boolean isHeadshot(double headshotChance, double headshotRNG) {
        return headshotRNG < headshotChance;
    }

    private double getBodyModifier(double bodyshotRNG, List<BodyModifier> bodyModifiers) {
        double maxOfRangeForBodyPart = 0.0;
        double minOfRangeForBodyPart = 0.0;
        for (BodyModifier bodyModifier : bodyModifiers) {
            maxOfRangeForBodyPart = bodyModifier.getChanceToHit() + maxOfRangeForBodyPart;
            if (bodyshotRNG >= minOfRangeForBodyPart && bodyshotRNG < maxOfRangeForBodyPart) {
                return bodyModifier.getModifierValue();
            }
            minOfRangeForBodyPart = maxOfRangeForBodyPart;
        }
        return 0.0;
    }

    private int getCritLevel(double weaponCriticalChance, double criticalRNG) {
        int critLevel = (int) Math.floor(weaponCriticalChance);
        boolean calculateCriticalChance = weaponCriticalChance % 1.0 > 0;
        if (calculateCriticalChance && criticalRNG < weaponCriticalChance % 1.0) {
            critLevel++;
        }
        return critLevel;
    }

    private boolean isDelayedDamageSource(DamageSource damageSource) {
        return damageSource.getDamageSourceType().equals(DamageSourceType.DELAYED) || damageSource.getDamageSourceType().equals(DamageSourceType.DELAYED_AOE);
    }

    private void updateRunningTotalDamageToHealth(DamageMetrics finalDamageMetrics, Map<DamageType, Double> damageToHealth) {
        for (DamageType damageType : damageToHealth.keySet()) {
            finalDamageMetrics.addToHealth(damageType, damageToHealth.get(damageType));
        }
    }

    private void updateRunningTotalDamageToShields(DamageMetrics finalDamageMetrics, Map<DamageType, Double> damageToShields) {
        for (DamageType damageType : damageToShields.keySet()) {
            finalDamageMetrics.addToShields(damageType, damageToShields.get(damageType));
        }
    }
}
