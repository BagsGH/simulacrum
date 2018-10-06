package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Damage.ElementalDamageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bags.simulacrum.Damage.DamageType.*;

@Component
public class DamageModHelper {

    private List<Mod> originalWeaponMods;

    private final ElementalDamageMapper elementalDamageMapper;

    @Autowired
    public DamageModHelper(ElementalDamageMapper elementalDamageMapper) {
        this.elementalDamageMapper = elementalDamageMapper;
    }

    public DamageSource calculateDamageSources(DamageSource damageSource, List<Mod> mods) {
        this.originalWeaponMods = mods;
        DamageSource modifiedDamageSource = damageSource.copyWithoutDamages();

        calculateRawDamageModsForInnateDamages(damageSource, modifiedDamageSource);

        List<Damage> baseDamagesFromSourceAfterRawDamageMods = calculateRawDamageModIncreases(damageSource.getDamages());
        double sumOfAllDamages = sumAllDamageTypes(baseDamagesFromSourceAfterRawDamageMods);
        List<Damage> ipsDamages = calculateIPSDamageMods(baseDamagesFromSourceAfterRawDamageMods);

        modifiedDamageSource.setAddedElementalDamages(calculateElementalDamageAddedByMods(sumOfAllDamages));
        List<Damage> elementalDamagesAddedByMods = calculateElementalDamageAddedByMods(sumOfAllDamages);

        List<Damage> orderedElementalDamagesFromWeaponAndMods = orderDamageTypesBasedOnModIndex(baseDamagesFromSourceAfterRawDamageMods, elementalDamagesAddedByMods);
        List<Damage> finalElementalDamages = combineElementalDamages(orderedElementalDamagesFromWeaponAndMods);

        modifiedDamageSource.setDamages(mergeElementalAndIPS(finalElementalDamages, ipsDamages));

        return modifiedDamageSource;
    }

    private void calculateRawDamageModsForInnateDamages(DamageSource damageSource, DamageSource modifiedDamageSource) {
        List<Damage> innateDamagesFromSourceAfterRawDamageMods = calculateRawDamageModIncreases(damageSource.getDamages());
        modifiedDamageSource.setModifiedInnateDamages(innateDamagesFromSourceAfterRawDamageMods);
    }

    private double sumAllDamageTypes(List<Damage> defaultDamagesModded) {
        return defaultDamagesModded.stream().mapToDouble(Damage::getDamageValue).sum();
    }

    private List<Damage> calculateRawDamageModIncreases(List<Damage> damagesFromSource) {
        double rawDamageIncrease = originalWeaponMods.stream().filter(mod -> mod.getDamageIncrease() != 0).mapToDouble(Mod::getDamageIncrease).sum();

        List<Damage> moddedBaseDamagesFromSource = new ArrayList<>();
        damagesFromSource.forEach(damage -> {
            moddedBaseDamagesFromSource.add(new Damage(damage.getDamageType(), damage.getDamageValue() * (1 + rawDamageIncrease)));
        });
        return moddedBaseDamagesFromSource;
    }

    private List<Damage> calculateIPSDamageMods(List<Damage> baseDamagesAfterRawDamageMods) {
        List<Damage> ipsDamages = new ArrayList<>();
        Map<DamageType, Double> ipsDamageIncreaseMap = new HashMap<>();
        ipsDamageIncreaseMap.put(IMPACT, 0.0);
        ipsDamageIncreaseMap.put(PUNCTURE, 0.0);
        ipsDamageIncreaseMap.put(SLASH, 0.0);

        populateIPSDamageIncreaseMap(ipsDamageIncreaseMap);

        for (Damage damage : baseDamagesAfterRawDamageMods) {
            DamageType damageType = damage.getDamageType();
            if (DamageType.isIPS(damageType)) {
                ipsDamages.add(new Damage(damageType, damage.getDamageValue() * (1 + ipsDamageIncreaseMap.get(damageType)), 0.0));
            }
        }

        return ipsDamages;
    }

    private void populateIPSDamageIncreaseMap(Map<DamageType, Double> ipsDamageIncreaseMap) {
        for (Mod mod : originalWeaponMods) {
            Damage modDamage = mod.getDamage();
            if (modDamage != null && DamageType.isIPS(modDamage.getDamageType())) {
                DamageType modIPSDamageType = modDamage.getDamageType();
                ipsDamageIncreaseMap.put(modIPSDamageType, ipsDamageIncreaseMap.get(modIPSDamageType) + modDamage.getModAddedDamageRatio());
            }
        }
    }

    private List<Damage> calculateElementalDamageAddedByMods(Double baseDamage) {
        List<Damage> elementalDamagesAddedByMods = new ArrayList<>();

        for (Mod mod : originalWeaponMods) {
            if (modAddsElementalDamage(mod)) {
                Damage modDamage = mod.getDamage();
                elementalDamagesAddedByMods.add(new Damage(modDamage.getDamageType(), baseDamage * modDamage.getModAddedDamageRatio()));
            }
        }

        return elementalDamagesAddedByMods;
    }

    private boolean modAddsElementalDamage(Mod mod) {
        return mod.getDamage() != null && DamageType.isElemental(mod.getDamage().getDamageType());
    }

    private List<Damage> orderDamageTypesBasedOnModIndex(List<Damage> moddedBaseDamages, List<Damage> modAddedElementalDamages) {
        List<Damage> elementalDamagesMergedList = new ArrayList<>(modAddedElementalDamages);
        for (Damage baseDamage : moddedBaseDamages) {
            if (DamageType.isElemental(baseDamage.getDamageType())) {
                boolean thereIsAModAddedDamageThatIsTheSameTypeAsABaseDamage = false;
                for (Damage modAddedDamage : elementalDamagesMergedList) {
                    if (modAddedElementalDamageHasMatchOnBase(baseDamage, modAddedDamage)) {
                        sumDamagesOfSameType(baseDamage, modAddedDamage);
                        thereIsAModAddedDamageThatIsTheSameTypeAsABaseDamage = true;
                    }
                }
                if (!thereIsAModAddedDamageThatIsTheSameTypeAsABaseDamage) {
                    elementalDamagesMergedList.add(baseDamage);
                }
            }
        }

        return elementalDamagesMergedList;
    }

    private boolean modAddedElementalDamageHasMatchOnBase(Damage damage1, Damage damage2) {
        return damage2.getDamageType().equals(damage1.getDamageType());
    }

    private void sumDamagesOfSameType(Damage baseDamage, Damage modAddedDamage) {
        modAddedDamage.setDamageValue(modAddedDamage.getDamageValue() + baseDamage.getDamageValue());
        modAddedDamage.setModAddedDamageRatio(0.0);
    }

    private List<Damage> combineElementalDamages(List<Damage> orderedElementalDamages) {
        List<Damage> combinedElementalDamages = new ArrayList<>();
        if (orderedElementalDamages.size() < 2) {
            return orderedElementalDamages;
        }

        for (int i = 0; i < orderedElementalDamages.size(); i++) {
            Damage damage1 = orderedElementalDamages.get(i);
            if (endOfList(orderedElementalDamages, i)) {
                combinedElementalDamages.add(damage1);
                break;
            }
            if (DamageType.isCombinedElemental(damage1.getDamageType())) {
                combinedElementalDamages.add(damage1);
            } else {
                Damage damage2 = orderedElementalDamages.get(i + 1);
                DamageType combinedDamageType = elementalDamageMapper.combineElements(damage1.getDamageType(), damage2.getDamageType());
                if (validCombination(combinedDamageType)) {
                    Damage combinedDamage = new Damage(combinedDamageType, damage1.getDamageValue() + damage2.getDamageValue(), 0.00);
                    combinedElementalDamages.add(combinedDamage);
                    i++;
                } else {
                    combinedElementalDamages.add(damage1);
                }
            }
        }
        return sumIdenticalTypes(combinedElementalDamages);
    }

    private boolean endOfList(List<Damage> orderedElementalDamageTypes, int i) {
        return i == orderedElementalDamageTypes.size() - 1;
    }

    private boolean validCombination(DamageType combinedDamageType) {
        return combinedDamageType != null;
    }

    private List<Damage> sumIdenticalTypes(List<Damage> combinedElementalDamages) {
        List<Damage> finalizedDamageTypes = new ArrayList<>();
        for (DamageType damageType : DamageType.getElementalDamageTypes()) {
            double summedDamageForType = 0.0;
            int count = 0;
            Damage possibleLoner = null;
            for (Damage damage : combinedElementalDamages) {
                if (damage.getDamageType().equals(damageType)) {
                    possibleLoner = damage;
                    summedDamageForType += damage.getDamageValue();
                    count++;
                }
            }
            if (count == 1) {
                finalizedDamageTypes.add(possibleLoner);
            } else if (count > 1) {
                finalizedDamageTypes.add(new Damage(damageType, summedDamageForType));
            }
        }
        return finalizedDamageTypes;
    }

    private List<Damage> mergeElementalAndIPS(List<Damage> combinedElementalDamages, List<Damage> moddedIPSDamages) {
        List<Damage> finalDamagesList = new ArrayList<>();
        finalDamagesList.addAll(combinedElementalDamages);
        finalDamagesList.addAll(moddedIPSDamages);

        return finalDamagesList;
    }
}
