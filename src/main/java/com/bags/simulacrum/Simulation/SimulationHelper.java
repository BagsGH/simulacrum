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
import java.util.stream.Collectors;

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

    //TODO: test
    public FiredWeaponSummary handleApplyingStatuses(List<Target> simulationTargets) { //TODO: bleed apply directly ohealth
        HitProperties statusTickHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        FiredWeaponSummary appliedStatusSummary = new FiredWeaponSummary();
        for (Target individualTarget : simulationTargets) {
            String targetName = individualTarget.getTargetName();
            List<Status> procsApplying = individualTarget.getStatuses().stream().filter(Status::checkProgress).collect(Collectors.toList());
            for (Status individualStatus : procsApplying) {
                DamageMetrics damageMetricsFromStatusTick = targetDamageHelper.applyDamageSourceDamageToTarget(individualStatus.apply(individualTarget), statusTickHitProperties, individualTarget);
                appliedStatusSummary.addStatusDamageToHealth(damageMetricsFromStatusTick.getDamageToHealth());
                appliedStatusSummary.addStatusDamageToShields(damageMetricsFromStatusTick.getDamageToShields());
            }
        }

        return appliedStatusSummary;
    }

    //TODO: TESt
    public FiredWeaponSummary handleDelayedDamageSources(List<DelayedDamageSource> delayedDamageSources, double statusChance) {
        DamageMetrics finalDamageMetricsFromDelayedDamageSources = new DamageMetrics.DamageMetricsBuilder().withDamageToHealth().withDamageToShields().withStatusDamageToHealth().withStatusDamageToShields().build();

        FiredWeaponSummary delayedDamageSourceSummary = new FiredWeaponSummary();

        for (DelayedDamageSource delayedDamageSource : delayedDamageSources) {
            if (delayedDamageSource.delayOver()) {
                Target target = delayedDamageSource.getTarget();
                String targetName = target.getTargetName();
                HitProperties delayedDamageSourceHitProperties = delayedDamageSource.getHitProperties();
                DamageMetrics damageMetricsFromDelayedDamage = targetDamageHelper.applyDamageSourceDamageToTarget(delayedDamageSource.getDamageSource(), delayedDamageSourceHitProperties, target); //TODO: delayed damage source keeps track of which target to apply to?
                delayedDamageSourceSummary.addDamageToHealth(targetName, damageMetricsFromDelayedDamage.getDamageToHealth());
                delayedDamageSourceSummary.addDamageToShields(targetName, damageMetricsFromDelayedDamage.getDamageToShields());
                if (randomNumberGenerator.getRandomPercentage() < statusChance) {
                    delayedDamageSourceSummary.addStatusApplied(targetName, getStatusProcAndApply(target, delayedDamageSource.getDamageSource(), damageMetricsFromDelayedDamage, finalDamageMetricsFromDelayedDamageSources));
                }
                delayedDamageSourceSummary.addHitProperties(targetName, delayedDamageSourceHitProperties);
            }
        }
        return delayedDamageSourceSummary;
    }

    public FiredWeaponSummary handleFireWeapon(Weapon weapon, SimulationTargets simulationTargets, double headshotChance) {
        List<DelayedDamageSource> delayedDamageSources = new ArrayList<>();
        DamageMetrics finalDamageMetrics = new DamageMetrics.DamageMetricsBuilder()
                .withDamageToHealth()
                .withDamageToShields()
                .withStatusDamageToHealth()
                .withStatusDamageToShields()
                .build();

        FiredWeaponSummary firedWeaponSummary = new FiredWeaponSummary(delayedDamageSources);

        double multishotRNG = randomNumberGenerator.getRandomPercentage();
        int shots = getMultishotLevel(weapon.getMultishot(), multishotRNG);

        for (int i = 0; i < shots; i++) {
            double criticalHitRNG = randomNumberGenerator.getRandomPercentage();
            double statusProcRNG = randomNumberGenerator.getRandomPercentage();

            int critLevel = getCritLevel(weapon.getCriticalChance(), criticalHitRNG);
            double weaponCriticalDamageMultiplier = critLevel > 0 ? weapon.getCriticalDamage() : 0.0;

            for (DamageSource damageSource : weapon.getDamageSources()) {
                List<Target> effectiveTargetList = getTargetList(simulationTargets, damageSource);
                for (Target individualTarget : effectiveTargetList) {
                    String targetName = individualTarget.getTargetName();

                    double headshotRNG = randomNumberGenerator.getRandomPercentage();
                    double bodyshotRNG = randomNumberGenerator.getRandomPercentage();
                    double headshotModifier = calculateHeadshotModifier(individualTarget, headshotChance, headshotRNG);
                    double bodyModifier = calculateBodyModifier(individualTarget, headshotChance, headshotRNG, bodyshotRNG);

                    HitProperties hitProperties = new HitProperties(critLevel, weaponCriticalDamageMultiplier, headshotModifier, bodyModifier); //TODO: roll sattus into this...

                    if (!isDelayedDamageSource(damageSource)) {
                        DamageMetrics damageMetrics = targetDamageHelper.applyDamageSourceDamageToTarget(damageSource, hitProperties, individualTarget);
                        firedWeaponSummary.addDamageToHealth(targetName, damageMetrics.getDamageToHealth());
                        firedWeaponSummary.addDamageToShields(targetName, damageMetrics.getDamageToShields());
                        if (statusProcRNG < weapon.getStatusChance()) {
                            firedWeaponSummary.addStatusApplied(targetName, getStatusProcAndApply(individualTarget, damageSource, damageMetrics, finalDamageMetrics));
                        }
                    } else {
                        delayedDamageSources.add(new DelayedDamageSource(individualTarget, damageSource.copy(), hitProperties, damageSource.getDelay()));
                    }
                    firedWeaponSummary.addHitProperties(targetName, hitProperties);
                }
            }
        }

        return firedWeaponSummary;
    }

    private List<Target> getTargetList(SimulationTargets simulationTargets, DamageSource damageSource) {
        List<Target> effectiveTargetList = new ArrayList<>();
        if (damageSource.getDamageSourceType().equals(DamageSourceType.HIT_AOE) || damageSource.getDamageSourceType().equals(DamageSourceType.DELAYED_AOE)) {
            effectiveTargetList.addAll(simulationTargets.getAllTargets());
        } else {
            effectiveTargetList.add(simulationTargets.getPrimaryTarget());
        }
        return effectiveTargetList;
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

    private Status getStatusProcAndApply(Target target, DamageSource damageSource, DamageMetrics damageMetrics, DamageMetrics finalDamageMetrics) {
        Status status = statusProcHelper.constructStatusProc(damageSource, damageMetrics.getDamageToHealth(), damageMetrics.getDamageToShields());
        target.addStatus(status);

        HitProperties statusTickHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);
        DamageSource damageSourceForDamageTick = status.apply(target);
        DamageMetrics damageMetricsFromDamageTick = targetDamageHelper.applyDamageSourceDamageToTarget(damageSourceForDamageTick, statusTickHitProperties, target);
        updateRunningTotalStatusDamageToHealth(finalDamageMetrics, damageMetricsFromDamageTick.getDamageToHealth()); //TODO: if i make it only do one status proc per hit, doesnt need this method any more
        updateRunningTotalStatusDamageToShields(finalDamageMetrics, damageMetricsFromDamageTick.getDamageToShields());

        return status;
    }

    private void updateRunningTotalStatusDamageToHealth(DamageMetrics finalDamageMetrics, Map<DamageType, Double> damageToHealth) {
        for (DamageType damageType : damageToHealth.keySet()) {
            finalDamageMetrics.addStatusDamageToHealth(damageType, damageToHealth.get(damageType));
        }
    }

    private void updateRunningTotalStatusDamageToShields(DamageMetrics finalDamageMetrics, Map<DamageType, Double> damageToShields) {
        for (DamageType damageType : damageToShields.keySet()) {
            finalDamageMetrics.addStatusDamageToShields(damageType, damageToShields.get(damageType));
        }
    }
}
