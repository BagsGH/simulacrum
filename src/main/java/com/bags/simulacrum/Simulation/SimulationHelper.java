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

    public FiredWeaponSummary handleFireWeapon(Weapon weapon, Target target, double headshotChance) { //tODO: secondary targets for aoe...?
        List<DelayedDamageSource> delayedDamageSources = new ArrayList<>();
        List<Status> statusProcsApplied = new ArrayList<>();
        DamageMetrics finalDamageMetrics = new DamageMetrics.DamageMetricsBuilder()
                .withDamageToHealth()
                .withDamageToShields()
                .withStatusDamageToHealth()
                .withStatusDamageToShields()
                .build();

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
                        statusProcsApplied.add(getStatusProcAndApply(target, damageSource, damageMetrics, finalDamageMetrics));
                    }
                } else {
                    delayedDamageSources.add(new DelayedDamageSource(damageSource.copy(), hitProperties, damageSource.getDelay()));
                }
            }
            hitPropertiesList.add(hitProperties);
        }

        return new FiredWeaponSummary(hitPropertiesList, finalDamageMetrics, statusProcsApplied, delayedDamageSources);
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
            finalDamageMetrics.addDamageToHealth(damageType, damageToHealth.get(damageType));
        }
    }

    private void updateRunningTotalDamageToShields(DamageMetrics finalDamageMetrics, Map<DamageType, Double> damageToShields) {
        for (DamageType damageType : damageToShields.keySet()) {
            finalDamageMetrics.addDamageToShields(damageType, damageToShields.get(damageType));
        }
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


    //TODO: Test
    public List<DamageMetrics> handleDelayedDamageSources(List<DelayedDamageSource> delayedDamageSources, Target target, double deltaTime) {
        List<DamageMetrics> listOfDamageMetrics = new ArrayList<>();
        for (DelayedDamageSource delayedDamageSource : delayedDamageSources) {
            delayedDamageSource.progressTime(deltaTime);
            if (delayedDamageSource.delayOver()) {
                DamageMetrics damageMetricsFromDelayedDamage = targetDamageHelper.applyDamageSourceDamageToTarget(delayedDamageSource.getDamageSource(), delayedDamageSource.getHitProperties(), target); //TODO: delayed damage source keeps track of which target to apply to?
                listOfDamageMetrics.add(damageMetricsFromDelayedDamage);
            }
        }
        return listOfDamageMetrics;
    }

    public List<DamageMetrics> handleApplyingStatuses(Target target, HitProperties statusTickHitProperties, double deltaTime) {
        List<DamageMetrics> listOfDamageMetrics = new ArrayList<>();
        List<Status> procsApplying = target.statusProgressTime(deltaTime);
        for (Status status : procsApplying) {
            DamageMetrics damageMetricsFromStatusTick = targetDamageHelper.applyDamageSourceDamageToTarget(status.apply(target), statusTickHitProperties, target);
            listOfDamageMetrics.add(damageMetricsFromStatusTick);
        }
        return listOfDamageMetrics;
    }
}
