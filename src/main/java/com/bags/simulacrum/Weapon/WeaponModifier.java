package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import org.decimal4j.util.DoubleRounder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WeaponModifier {

    private List<Mod> weaponMods;
    private Weapon originalWeapon;

    public Weapon modWeapon(Weapon weapon) {
        originalWeapon = weapon;
        weaponMods = originalWeapon.getMods();
        Weapon modifiedWeapon = copyWeaponToMod();

        List<Damage> moddedBaseDamage = calculateModdedDamageValues();
        List<Damage> moddedElementalDamage = calculateElementalDamageAddedByMods(moddedBaseDamage);
        List<Damage> orderedElementalDamageTypes = orderDamageTypes(moddedBaseDamage, moddedElementalDamage);

        modifiedWeapon.setDamageTypes(orderedElementalDamageTypes);

//        modifiedWeapon.setDamageTypes(moddedBaseDamage);


        modifiedWeapon.setFireRate(calculateModdedFireRate());
        modifiedWeapon.setAccuracy(calculateModdedAccuracy());
        modifiedWeapon.setMagazineSize(calculateModdedMagazineSize());
        modifiedWeapon.setMaxAmmo(calculateModdedMaxAmmo());
        modifiedWeapon.setReloadTime(calculateModdedReloadTime());
        modifiedWeapon.setChargeTime(calculateModdedChargeTime());
        modifiedWeapon.setCriticalChance(calculateModdedCriticalChance());
        modifiedWeapon.setCriticalDamage(calculateModdedCriticalDamage());
        modifiedWeapon.setStatusChance(calculateModdedStatusChance());
        //modifiedWeapon.setDamageTypes(calculateModdedDamageTypes());

        return modifiedWeapon;
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
                        modAddedDamage.setModElementalDamageRatio(0.0);
                        thereIsAModThatIsTheSameType = true;
                    }
                }
                if (!thereIsAModThatIsTheSameType) {
                    mergedList.add(baseDamage);
                }
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
            if (mod.getDamage() != null) {
                Damage modsDamage = mod.getDamage();
                modsDamage.setDamageValue(baseWeaponDamage * modsDamage.getModElementalDamageRatio());
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
            Damage newD = new Damage(d);
            newD.setDamageValue(newD.getDamageValue() * (1 + damageIncrease));
            moddedDamageTypes.add(newD);
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
