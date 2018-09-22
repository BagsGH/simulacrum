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

    @Autowired
    public EngineHelper(TargetDamageHelper targetDamageHelper, Random random) {
        this.targetDamageHelper = targetDamageHelper;
        this.random = random;
    }

    public FiredWeaponSummary handleFireWeapon(Weapon weapon, Target target, double headshotChance) {
        Map<DamageType, Double> summedDamageToHealth = DamageSummary.initialDamageMap();
        Map<DamageType, Double> summedDamageToShields = DamageSummary.initialDamageMap();

        double headshotRNG = random.getRandom();
        double multishotRNG = random.getRandom();
        double statusProcRNG = random.getRandom();
        double criticalHitRNG = random.getRandom();
        double bodyshotRNG = random.getRandom();

        int critLevel = getCritLevel(weapon.getCriticalChance(), criticalHitRNG);
        int multishots = getMultishotLevel(weapon.getMultishot(), multishotRNG);
        double headshotModifier = headshotRNG < headshotChance ? target.getHeadshotModifier() : 0.0;
        double bodyModifier = headshotChance == 0.0 ? getBodyModifier(bodyshotRNG, target.getBodyModifiers()) : 0.0;

        HitProperties hitProperties = new HitProperties(critLevel, weapon.getCriticalDamage(), headshotModifier, bodyModifier);
        List<DelayedDamageSource> delayedDamageSources = new ArrayList<>();

        for (int i = 0; i < multishots; i++) {
            for (DamageSource damageSource : weapon.getDamageSources()) {
                if (isDelayedDamageSource(damageSource)) {
                    delayedDamageSources.add(new DelayedDamageSource(damageSource, 2.0)); //TODO: where do delays come from?
                } else {
                    DamageSummary damageSummary = targetDamageHelper.applyDamageSourceDamageToTarget(damageSource, hitProperties, target);//TODO: handle procs
                    updateRunningTotalDamageToHealth(summedDamageToHealth, damageSummary.getDamageToHealth());
                    updateRunningTotalDamageToShields(summedDamageToShields, damageSummary.getDamageToShields());
                }
            }
        }

        return new FiredWeaponSummary(hitProperties, new DamageSummary(target, summedDamageToShields, summedDamageToHealth), delayedDamageSources);
    }

    private int getCritLevel(double weaponCriticalChance, double criticalRNG) {
        int baseCritLevel = (int) Math.floor(weaponCriticalChance);
        boolean calculateCriticalChance = weaponCriticalChance % 1.0 > 0;
        if (calculateCriticalChance && criticalRNG < weaponCriticalChance % 1.0) {
            baseCritLevel++;
        }
        return baseCritLevel;
    }

    private int getMultishotLevel(double weaponMultishotChance, double multishotRNG) {
        int baseMultishot = (int) Math.floor(weaponMultishotChance);
        boolean calculateMultishotChance = weaponMultishotChance % 1.0 > 0;
        if (calculateMultishotChance && multishotRNG < weaponMultishotChance % 1.0) {
            baseMultishot++;
        }
        return baseMultishot;
    }

    private double getBodyModifier(double bodyshotRNG, List<BodyModifier> bodyModifiers) {
        double maxOfRange = 0.0;
        double minOfRange = 0.0;
        for (BodyModifier bodyModifier : bodyModifiers) {
            maxOfRange = bodyModifier.getChanceToHit() + maxOfRange;
            if (bodyshotRNG >= minOfRange && bodyshotRNG < maxOfRange) {
                return bodyModifier.getModifierValue();
            }
            minOfRange = maxOfRange;
        }
        return 0.0;
    }

    private void updateRunningTotalDamageToHealth(Map<DamageType, Double> summedDamageToHealth, Map<DamageType, Double> damageToHealth) {
        for (DamageType damageType : damageToHealth.keySet()) {
            double currentDamageToHealthValues = summedDamageToHealth.get(damageType);
            summedDamageToHealth.put(damageType, currentDamageToHealthValues + damageToHealth.get(damageType));
        }
    }

    private void updateRunningTotalDamageToShields(Map<DamageType, Double> summedDamageToShields, Map<DamageType, Double> damageToShields) {
        for (DamageType damageType : damageToShields.keySet()) {
            double currentDamageToHealthValues = summedDamageToShields.get(damageType);
            summedDamageToShields.put(damageType, currentDamageToHealthValues + damageToShields.get(damageType));
        }
    }

    private boolean isDelayedDamageSource(DamageSource damageSource) {
        return damageSource.getDamageSourceType().equals(DamageSourceType.DELAYED) || damageSource.getDamageSourceType().equals(DamageSourceType.DELAYED_AOE);
    }
}
