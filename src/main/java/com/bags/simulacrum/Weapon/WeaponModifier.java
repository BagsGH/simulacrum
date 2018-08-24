package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.ElementalDamageMapper;
import org.decimal4j.util.DoubleRounder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WeaponModifier {

    private List<Mod> weaponMods;
    private Weapon originalWeapon;

    public Weapon modWeapon(Weapon weapon) {
        originalWeapon = weapon;
        weaponMods = originalWeapon.getMods();
        Weapon modifiedWeapon = copyWeaponToMod();

        List<Damage> defaultDamagesModded = calculateModdedDamageValues();
        List<Damage> moddedIPSDamage = calculateIPSDamageMods(defaultDamagesModded);
        List<Damage> moddedElementalDamage = calculateElementalDamageAddedByMods(defaultDamagesModded);
        List<Damage> orderedDamageTypes = orderDamageTypes(defaultDamagesModded, moddedElementalDamage);
        List<Damage> combinedElementalDamageTypes = combineDamageTypes(orderedDamageTypes);
        List<Damage> allDamageTypes = mergeElementalAndIPS(combinedElementalDamageTypes, moddedIPSDamage);
        modifiedWeapon.setDamageTypes(allDamageTypes);

        modifiedWeapon.setFireRate(calculateModdedFireRate());
        modifiedWeapon.setAccuracy(calculateModdedAccuracy());
        modifiedWeapon.setMagazineSize(calculateModdedMagazineSize());
        modifiedWeapon.setMaxAmmo(calculateModdedMaxAmmo());
        modifiedWeapon.setReloadTime(calculateModdedReloadTime());
        modifiedWeapon.setChargeTime(calculateModdedChargeTime());
        modifiedWeapon.setCriticalChance(calculateModdedCriticalChance());
        modifiedWeapon.setCriticalDamage(calculateModdedCriticalDamage());
        modifiedWeapon.setStatusChance(calculateModdedStatusChance());
        modifiedWeapon.setMultishot(calculateModdedMultishot());

        return modifiedWeapon;
    }

    private List<Damage> mergeElementalAndIPS(List<Damage> combinedElementalDamageTypes, List<Damage> moddedIPSDamage) {
        List<Damage> finalL = new ArrayList<Damage>();
        int size = combinedElementalDamageTypes.size();
        for (int i = 0; i < size; i++) {
            if (Damage.isIPS(combinedElementalDamageTypes.get(i))) {
                combinedElementalDamageTypes.remove(i);
                size--;
            }
        }

        finalL.addAll(combinedElementalDamageTypes);
        finalL.addAll(moddedIPSDamage);


        return finalL;
    }

    private List<Damage> calculateIPSDamageMods(List<Damage> moddedBaseDamage) {
        List<Damage> baseDamageWithIPSMods = new ArrayList<>();
        Map<Damage.DamageType, Double> ipsDamageIncreaseMap = new HashMap<>();
        ipsDamageIncreaseMap.put(Damage.DamageType.IMPACT, 0.0);
        ipsDamageIncreaseMap.put(Damage.DamageType.PUNCTURE, 0.0);
        ipsDamageIncreaseMap.put(Damage.DamageType.SLASH, 0.0);

        for (Mod mod : originalWeapon.getMods()) {
            Damage modDamage = mod.getDamage();
            if (modDamage != null && Damage.isIPS(modDamage)) {
                Damage.DamageType modIPSDamageType = modDamage.getType();
                ipsDamageIncreaseMap.put(modIPSDamageType, ipsDamageIncreaseMap.get(modIPSDamageType) + modDamage.getModAddedDamageRatio());
            }
        }
        for (Damage d : moddedBaseDamage) {
            if (Damage.isIPS(d)) {
                Damage.DamageType damageType = d.getType();
                baseDamageWithIPSMods.add(new Damage(damageType, d.getDamageValue() * (1 + ipsDamageIncreaseMap.get(damageType)), 0.0));
            }
        }

        return baseDamageWithIPSMods;
    }

    private double calculateModdedMultishot() {
        double multishotIncrease = weaponMods.stream().filter(mod -> mod.getMultishotIncrease() != 0).mapToDouble(Mod::getMultishotIncrease).sum();
        return originalWeapon.getMultishot() + multishotIncrease;
    }

    private List<Damage> combineDamageTypes(List<Damage> orderedElementalDamageTypes) {
        ElementalDamageMapper mapper = new ElementalDamageMapper();
        List<Damage> combinedElementalDamages = new ArrayList<>();
        if (orderedElementalDamageTypes.size() < 2) {
            return orderedElementalDamageTypes;
        }
        for (int i = 0; i < orderedElementalDamageTypes.size() - 1; i++) {
            if (orderedElementalDamageTypes.size() >= 2) {
                Damage d1 = orderedElementalDamageTypes.get(i);
                Damage d2 = orderedElementalDamageTypes.get(i + 1);
                Damage.DamageType combinedDamageType = mapper.combineElements(d1.getType(), d2.getType());
                if (combinedDamageType != null) {
                    Damage CombinedDamage = new Damage(combinedDamageType, d1.getDamageValue() + d2.getDamageValue(), 0.00);
                    combinedElementalDamages.add(CombinedDamage);
                    i++;
                } else {
                    combinedElementalDamages.add(d1);
                    if (i + 2 >= orderedElementalDamageTypes.size()) {
                        combinedElementalDamages.add(d2);
                    }
                }
            }
        }

        return combinedElementalDamages;
    }

    private List<Damage> orderDamageTypes(List<Damage> moddedBaseDamage, List<Damage> moddedElementalDamage) {
        List<Damage> mergedList = new ArrayList<>();
        mergedList.addAll(moddedElementalDamage);
        for (Damage baseDamage : moddedBaseDamage) {
            if (Damage.isElemental(baseDamage)) {
                boolean thereIsAModThatIsTheSameType = false;
                for (Damage modAddedDamage : mergedList) {
                    if (modAddedDamage.getType().equals(baseDamage.getType())) {
                        modAddedDamage.setDamageValue(modAddedDamage.getDamageValue() + baseDamage.getDamageValue());
                        modAddedDamage.setModAddedDamageRatio(0.0);
                        thereIsAModThatIsTheSameType = true;
                    }
                }
                if (!thereIsAModThatIsTheSameType) {
                    mergedList.add(baseDamage);
                }
            } else {
                mergedList.add(baseDamage);
            }
        }

        return mergedList;
    }

    private List<Damage> calculateElementalDamageAddedByMods(List<Damage> moddedBaseWeaponDamage) {
        double baseWeaponDamage = 0;
        for (Damage individualDamage : moddedBaseWeaponDamage) {
            baseWeaponDamage += individualDamage.getDamageValue();
        }

        List<Damage> elementalDamageAddedByMods = new ArrayList<>();

        for (Mod mod : originalWeapon.getMods()) {
            if (mod.getDamage() != null && Damage.isElemental(mod.getDamage())) {
                Damage modsDamage = mod.getDamage();
                modsDamage.setDamageValue(baseWeaponDamage * modsDamage.getModAddedDamageRatio());
                elementalDamageAddedByMods.add(modsDamage);
            }
        }

        return elementalDamageAddedByMods;
    }

    private List<Damage> calculateModdedDamageValues() {
        double damageIncrease = weaponMods.stream().filter(mod -> mod.getDamageIncrease() != 0).mapToDouble(Mod::getDamageIncrease).sum();

        List<Damage> damageTypes = originalWeapon.getDamageTypes();
        List<Damage> moddedDamageTypes = new ArrayList<>();
        damageTypes.forEach(d -> {
            moddedDamageTypes.add(new Damage(d.getType(), d.getDamageValue() * (1 + damageIncrease), 0.0));
        });
        return moddedDamageTypes;
    }

    private Weapon copyWeaponToMod() {
        return new Weapon(originalWeapon.getName(), originalWeapon.getMasteryRank(), originalWeapon.getSlot(), originalWeapon.getType(), originalWeapon.getTriggerType(), originalWeapon.getAmmoType(),
                originalWeapon.getRangeLimit(), originalWeapon.getNoiseLevel(), originalWeapon.getMaxAmmo(), originalWeapon.getDisposition(), originalWeapon.getMods());
    }

    private double calculateModdedFireRate() {
        double fireRateIncrease = weaponMods.stream().filter(mod -> mod.getFireRateIncrease() != 0).mapToDouble(Mod::getFireRateIncrease).sum();
        return DoubleRounder.round(originalWeapon.getFireRate() * (1 + fireRateIncrease), 3);
    }

    private double calculateModdedAccuracy() {
        double accuracyIncrease = weaponMods.stream().filter(mod -> mod.getAccuracyIncrease() != 0.0).mapToDouble(Mod::getAccuracyIncrease).sum();
        return originalWeapon.getAccuracy() + accuracyIncrease;
    }

    private int calculateModdedMagazineSize() {
        double magazineSizeIncrease = weaponMods.stream().filter(mod -> mod.getMagazineSizeIncrease() != 0.0).mapToDouble(Mod::getMagazineSizeIncrease).sum();
        return (int) Math.round((double) originalWeapon.getMagazineSize() * (1 + magazineSizeIncrease));
    }

    private int calculateModdedMaxAmmo() {
        double maxAmmoSizeIncrease = weaponMods.stream().filter(mod -> mod.getMaxAmmoIncrease() != 0.0).mapToDouble(Mod::getMaxAmmoIncrease).sum();
        return (int) Math.round((double) originalWeapon.getMaxAmmo() * (1 + maxAmmoSizeIncrease));
    }

    private double calculateModdedReloadTime() {
        double reloadSpeedIncrease = weaponMods.stream().filter(mod -> mod.getReloadTimeIncrease() != 0.0).mapToDouble(Mod::getReloadTimeIncrease).sum();
        return DoubleRounder.round(originalWeapon.getReloadTime() / (1 + reloadSpeedIncrease), 3);
    }

    private double calculateModdedChargeTime() {
        double chargeTimeIncrease = weaponMods.stream().filter(mod -> mod.getFireRateIncrease() != 0).mapToDouble(mod -> mod.getFireRateIncrease() * weaponTypeFireRateBonusMultiplier(mod.getFireRateIncrease())).sum();
        return DoubleRounder.round(originalWeapon.getChargeTime() / (1 + chargeTimeIncrease), 3);
    }

    /**
     * If the weapon type is a bow, it gets a 2.0 multiplier to the individual mod's positive fire rate bonus. If it's a negative fire rate bonus mod the multiplier is just 1.0.
     * Other charge weapons are not affected and always have a multiplier of 1.0.
     *
     * @param fireRateIncrease The individual mod's raw increase or decrease to fire rate.
     * @return A multiplier to use when calculating an individual mod's affect on the fire rate.
     */
    private double weaponTypeFireRateBonusMultiplier(double fireRateIncrease) {
        return fireRateIncrease > 0 && originalWeapon.getType().equals(Weapon.WeaponType.BOW) ? 2.0 : 1.0;
    }

    private double calculateModdedCriticalDamage() {
        double criticalDamageIncrease = weaponMods.stream().filter(mod -> mod.getCriticalDamageIncrease() != 0).mapToDouble(Mod::getCriticalDamageIncrease).sum();
        return DoubleRounder.round(originalWeapon.getCriticalDamage() * (1 + criticalDamageIncrease), 3);
    }

    private double calculateModdedCriticalChance() {
        double criticalChanceIncrease = weaponMods.stream().filter(mod -> mod.getCriticalChanceIncrease() != 0).mapToDouble(Mod::getCriticalChanceIncrease).sum();
        return DoubleRounder.round(originalWeapon.getCriticalChance() * (1 + criticalChanceIncrease), 3);
    }

    private double calculateModdedStatusChance() {
        double statusChanceIncrease = weaponMods.stream().filter(mod -> mod.getStatusChanceIncrease() != 0).mapToDouble(Mod::getStatusChanceIncrease).sum();
        return DoubleRounder.round(originalWeapon.getStatusChance() * (1 + statusChanceIncrease), 3);
    }
}
