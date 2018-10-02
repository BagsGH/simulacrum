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
        Map<DamageType, Double> summedStatusDamageToShields = DamageMetrics.initialDamageMap();
        Map<DamageType, Double> summedStatusDamageToHealth = DamageMetrics.initialDamageMap();
        DamageMetrics finalDamageMetrics = new DamageMetrics(target, summedDamageToHealth, summedDamageToShields);
        finalDamageMetrics.setStatusDamageToHealth(summedStatusDamageToHealth);
        finalDamageMetrics.setStatusDamageToShields(summedStatusDamageToShields); //TODO: roll into constructor

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
                        handleStatusProc(target, statusProcsApplied, finalDamageMetrics, damageSource, damageMetrics);
                    }
                } else {
                    delayedDamageSources.add(new DelayedDamageSource(damageSource.copy(), damageSource.getDelay())); //TODO: calculate crits etc now or later?
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

    private void handleStatusProc(Target target, List<Status> statusProcsApplied, DamageMetrics finalDamageMetrics, DamageSource damageSource, DamageMetrics damageMetrics) {
        Status status = statusProcHelper.constructStatusProc(damageSource, damageMetrics.getDamageToHealth(), damageMetrics.getDamageToShields());
        target.addStatus(status);
        status.apply(target);
        statusProcsApplied.add(status);

        HitProperties statusTickHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        DamageSource damageSourceForDamageTick = status.getDamageTickDamageSource();
        DamageMetrics damageMetricsFromDamageTick = targetDamageHelper.applyDamageSourceDamageToTarget(damageSourceForDamageTick, statusTickHitProperties, target);
        updateRunningTotalStatusDamageToHealth(finalDamageMetrics, damageMetricsFromDamageTick.getDamageToHealth()); //TODO: if i make it only do one status proc per hit, doesnt need this method any more
        updateRunningTotalStatusDamageToShields(finalDamageMetrics, damageMetricsFromDamageTick.getDamageToShields());
    }

    private void updateRunningTotalStatusDamageToHealth(DamageMetrics finalDamageMetrics, Map<DamageType, Double> damageToHealth) {
        for (DamageType damageType : damageToHealth.keySet()) {
            finalDamageMetrics.addToStatusHealth(damageType, damageToHealth.get(damageType));
        }
    }

    private void updateRunningTotalStatusDamageToShields(DamageMetrics finalDamageMetrics, Map<DamageType, Double> damageToShields) {
        for (DamageType damageType : damageToShields.keySet()) {
            finalDamageMetrics.addToStatusShields(damageType, damageToShields.get(damageType));
        }
    }
}
