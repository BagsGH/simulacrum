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

    public FiredWeaponSummary handleFireWeapon(Weapon weapon, Target target, double headshotChance) { //tODO: secondary targets for aoe...?
        List<DelayedDamageSource> delayedDamageSources = new ArrayList<>();
        Map<DamageType, Double> summedDamageToHealth = DamageSummary.initialDamageMap();
        Map<DamageType, Double> summedDamageToShields = DamageSummary.initialDamageMap();
        List<HitProperties> hitPropertiesList = new ArrayList<>();

        double multishotRNG = random.getRandom();
        double headshotRNG = random.getRandom(); //TODO: maybe if the accuracy is bad, calculate this independently for multishot?
        double bodyshotRNG = random.getRandom(); //TODO: maybe if the accuracy is bad, calculate this independently for multishot?

        int multishots = getMultishotLevel(weapon.getMultishot(), multishotRNG);
        double headshotModifier = headshotRNG < headshotChance ? target.getHeadshotModifier() : 0.0;
        double bodyModifier = !(headshotRNG < headshotChance) ? getBodyModifier(bodyshotRNG, target.getBodyModifiers()) : 0.0;

        for (int i = 0; i < multishots; i++) {
            double criticalHitRNG = random.getRandom();
            double statusProcRNG = random.getRandom(); //TODO: handle procs
            int critLevel = getCritLevel(weapon.getCriticalChance(), criticalHitRNG);
            double weaponCriticalDamageMultiplier = critLevel > 0 ? weapon.getCriticalDamage() : 0.0;
            HitProperties hitProperties = new HitProperties(critLevel, weaponCriticalDamageMultiplier, headshotModifier, bodyModifier);

            for (DamageSource damageSource : weapon.getDamageSources()) {
                if (!isDelayedDamageSource(damageSource)) {
                    DamageSummary damageSummary = targetDamageHelper.applyDamageSourceDamageToTarget(damageSource, hitProperties, target);
                    updateRunningTotalDamageToHealth(summedDamageToHealth, damageSummary.getDamageToHealth());
                    updateRunningTotalDamageToShields(summedDamageToShields, damageSummary.getDamageToShields());

                } else {
                    delayedDamageSources.add(new DelayedDamageSource(damageSource, damageSource.getDelay())); //TODO: calculate crits etc now or later? //TODO: new up a new damageSource?
                }
            }

            hitPropertiesList.add(hitProperties);

        }

        return new FiredWeaponSummary(hitPropertiesList, new DamageSummary(target, summedDamageToHealth, summedDamageToShields), delayedDamageSources);
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
            double currentDamageToShieldValues = summedDamageToShields.get(damageType);
            summedDamageToShields.put(damageType, currentDamageToShieldValues + damageToShields.get(damageType));
        }
    }

    private boolean isDelayedDamageSource(DamageSource damageSource) {
        return damageSource.getDamageSourceType().equals(DamageSourceType.DELAYED) || damageSource.getDamageSourceType().equals(DamageSourceType.DELAYED_AOE);
    }
}
