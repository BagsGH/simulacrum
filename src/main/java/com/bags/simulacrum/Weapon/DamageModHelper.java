package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Damage.ElementalDamageMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DamageModHelper {

    private Weapon originalWeapon;

    public List<Damage> calculateDamageSources(Weapon originalWeapon) {
        this.originalWeapon = originalWeapon;

        List<Damage> baseDamageSourcesAfterRawDamageMods = calculateModdedDamageValues(originalWeapon.getDamageTypes()); //#1
        double sumOfAllDamages = sumAllDamageTypes(baseDamageSourcesAfterRawDamageMods);
        List<Damage> ipsDamageSources = calculateIPSDamageMods(baseDamageSourcesAfterRawDamageMods); //#2
        List<Damage> elementalDamageSourcesAddedByMods = calculateElementalDamageAddedByMods(sumOfAllDamages); //#3
        List<Damage> orderedElementalDamageSourcesFromWeaponAndMods = orderDamageTypesBasedOnModIndex(baseDamageSourcesAfterRawDamageMods, elementalDamageSourcesAddedByMods);
        List<Damage> finalElementalDamageSources = combineDamageTypes(orderedElementalDamageSourcesFromWeaponAndMods); //#4

        //TODO: make the next line redundant by fixing this in #2. Weird to merge back in.
        return mergeElementalAndIPS(finalElementalDamageSources, ipsDamageSources);
    }

    public List<Damage> calculateSecondaryDamageSources(Weapon originalWeapon) {
        this.originalWeapon = originalWeapon;
        return this.originalWeapon.getSecondaryDamageTypes() != null ? calculateModdedDamageValues(this.originalWeapon.getSecondaryDamageTypes()) : null;
    }

    private double sumAllDamageTypes(List<Damage> defaultDamagesModded) {
        return defaultDamagesModded.stream().mapToDouble(Damage::getDamageValue).sum();
    }

    private List<Damage> calculateModdedDamageValues(List<Damage> damageTypes) {
        double damageIncrease = originalWeapon.getMods().stream().filter(mod -> mod.getDamageIncrease() != 0).mapToDouble(Mod::getDamageIncrease).sum();

        List<Damage> moddedDamageTypes = new ArrayList<>();
        damageTypes.forEach(damageSource -> {
            moddedDamageTypes.add(new Damage(damageSource.getType(), damageSource.getDamageValue() * (1 + damageIncrease)));
        });
        return moddedDamageTypes;
    }

    private List<Damage> calculateIPSDamageMods(List<Damage> baseDamageSourcesAfterRawDamageMods) {
        List<Damage> ipsDamageSources = new ArrayList<>();
        Map<DamageType, Double> ipsDamageIncreaseMap = new HashMap<>();
        ipsDamageIncreaseMap.put(DamageType.IMPACT, 0.0);
        ipsDamageIncreaseMap.put(DamageType.PUNCTURE, 0.0);
        ipsDamageIncreaseMap.put(DamageType.SLASH, 0.0);

        populateIPSDamageIncreaseMap(ipsDamageIncreaseMap);

        for (Damage damageSource : baseDamageSourcesAfterRawDamageMods) {
            if (DamageType.isIPS(damageSource.getType())) {
                DamageType damageType = damageSource.getType();
                ipsDamageSources.add(new Damage(damageType, damageSource.getDamageValue() * (1 + ipsDamageIncreaseMap.get(damageType)), 0.0));
            }
        }

        return ipsDamageSources;
    }

    private void populateIPSDamageIncreaseMap(Map<DamageType, Double> ipsDamageIncreaseMap) {
        for (Mod mod : originalWeapon.getMods()) {
            Damage modDamage = mod.getDamage();
            if (modDamage != null && DamageType.isIPS(modDamage.getType())) {
                DamageType modIPSDamageType = modDamage.getType();
                ipsDamageIncreaseMap.put(modIPSDamageType, ipsDamageIncreaseMap.get(modIPSDamageType) + modDamage.getModAddedDamageRatio());
            }
        }
    }

    private List<Damage> calculateElementalDamageAddedByMods(Double baseDamage) {
        List<Damage> elementalDamageSourcesAddedByMods = new ArrayList<>();

        for (Mod mod : originalWeapon.getMods()) {
            if (modAddsElementalDamageSource(mod)) {
                Damage modDamageSource = mod.getDamage();
                modDamageSource.setDamageValue(baseDamage * modDamageSource.getModAddedDamageRatio());
                elementalDamageSourcesAddedByMods.add(modDamageSource);
            }
        }

        return elementalDamageSourcesAddedByMods;
    }

    private boolean modAddsElementalDamageSource(Mod mod) {
        return mod.getDamage() != null && DamageType.isElemental(mod.getDamage().getType());
    }

    private List<Damage> orderDamageTypesBasedOnModIndex(List<Damage> moddedBaseDamage, List<Damage> moddedElementalDamage) {
        List<Damage> mergedList = new ArrayList<>();
        mergedList.addAll(moddedElementalDamage);
        for (Damage baseDamage : moddedBaseDamage) {
            if (DamageType.isElemental(baseDamage.getType())) {
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
            }
        }

        return mergedList;
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
                DamageType combinedDamageType = mapper.combineElements(d1.getType(), d2.getType());
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

    private List<Damage> mergeElementalAndIPS(List<Damage> combinedElementalDamageTypes, List<Damage> moddedIPSDamage) {
        List<Damage> finalDamageSourceList = new ArrayList<>();
        finalDamageSourceList.addAll(combinedElementalDamageTypes);
        finalDamageSourceList.addAll(moddedIPSDamage);

        return finalDamageSourceList;
    }
}
