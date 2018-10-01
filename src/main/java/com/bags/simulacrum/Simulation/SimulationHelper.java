package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageSourceType;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Damage.DelayedDamageSource;
import com.bags.simulacrum.Entity.BodyModifier;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Status.Status;
import com.bags.simulacrum.Status.StatusProcHelper;
import com.bags.simulacrum.Weapon.Weapon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SimulationHelper {

    private final TargetDamageHelper targetDamageHelper;
    private final RandomNumberGenerator randomNumberGenerator;
    private final StatusProcHelper statusProcHelper;

    @Autowired
    public SimulationHelper(TargetDamageHelper targetDamageHelper, RandomNumberGenerator randomNumberGenerator, StatusProcHelper statusProcHelper) {
        this.targetDamageHelper = targetDamageHelper;
        this.randomNumberGenerator = randomNumberGenerator;
        this.statusProcHelper = statusProcHelper;
    }

    public FiredWeaponMetrics handleFireWeapon(Weapon weapon, Target target, double headshotChance) { //tODO: secondary targets for aoe...?
        List<DelayedDamageSource> delayedDamageSources = new ArrayList<>();
        List<Status> statusProcsApplied = new ArrayList<>();
        Map<DamageType, Double> summedDamageToHealth = DamageMetrics.initialDamageMap();
        Map<DamageType, Double> summedDamageToShields = DamageMetrics.initialDamageMap();
        DamageMetrics finalDamageMetrics = new DamageMetrics(target, summedDamageToHealth, summedDamageToShields);

        List<HitProperties> hitPropertiesList = new ArrayList<>();

        double multishotRNG = randomNumberGenerator.getRandomPercentage();
        double headshotRNG = randomNumberGenerator.getRandomPercentage(); //TODO: maybe if the accuracy is bad, calculate this independently for multishot?
        double bodyshotRNG = randomNumberGenerator.getRandomPercentage(); //TODO: maybe if the accuracy is bad, calculate this independently for multishot?

        int multishots = getMultishotLevel(weapon.getMultishot(), multishotRNG);
        double headshotModifier = calculateHeadshotModifier(target, headshotChance, headshotRNG);
        double bodyModifier = calculateBodyModifier(target, headshotChance, headshotRNG, bodyshotRNG);

        for (int i = 0; i < multishots; i++) {
            double criticalHitRNG = randomNumberGenerator.getRandomPercentage();
            double statusProcRNG = randomNumberGenerator.getRandomPercentage();
            int critLevel = getCritLevel(weapon.getCriticalChance(), criticalHitRNG);
            double weaponCriticalDamageMultiplier = critLevel > 0 ? weapon.getCriticalDamage() : 0.0;
            HitProperties hitProperties = new HitProperties(critLevel, weaponCriticalDamageMultiplier, headshotModifier, bodyModifier);

            for (DamageSource damageSource : weapon.getDamageSources()) {
                //TODO: for now, each damage source will get a status proc. Not sure how to handle Opticor, but for now this is how it works.
                if (!isDelayedDamageSource(damageSource)) {
                    DamageMetrics damageMetrics = targetDamageHelper.applyDamageSourceDamageToTarget(damageSource, hitProperties, target);
                    updateRunningTotalDamageToHealth(finalDamageMetrics, damageMetrics.getDamageToHealth());
                    updateRunningTotalDamageToShields(finalDamageMetrics, damageMetrics.getDamageToShields());
                    if (statusProcRNG < weapon.getStatusChance()) {
                        statusProcsApplied.add(handleStatusChance(target, damageMetrics));
                    }
                } else {
                    delayedDamageSources.add(new DelayedDamageSource(damageSource.deepCopy(), damageSource.getDelay())); //TODO: calculate crits etc now or later?
                }
            }
            hitPropertiesList.add(hitProperties);
        }

        return new FiredWeaponMetrics(hitPropertiesList, finalDamageMetrics, statusProcsApplied, delayedDamageSources);
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
        return headshotRNG < headshotChance ? target.getHeadModifierValue() : 0.0;
    }

    private double calculateBodyModifier(Target target, double headshotChance, double headshotRNG, double bodyshotRNG) {
        return !(headshotRNG < headshotChance) ? getBodyModifier(bodyshotRNG, target.getBodyModifiers()) : 0.0;
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

    private Status handleStatusChance(Target target, DamageMetrics damageMetrics) {
        Status status = statusProcHelper.handleStatusProc(damageMetrics.getDamageToHealth(), damageMetrics.getDamageToShields());
        target.addStatus(status);
        if (status.applyInstantly()) {
            target.applyStatus();
        }
        return status;
    }
}