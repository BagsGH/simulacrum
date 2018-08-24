package com.bags.simulacrum.Damage;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ElementalDamageMapper {

    private static final Map<ElementalPair, DamageType> damageCombinationMap;

    static {
        damageCombinationMap = new HashMap<ElementalPair, DamageType>() {{
            /*Heat*/
            put(new ElementalPair(DamageType.HEAT, DamageType.HEAT), DamageType.HEAT);
            put(new ElementalPair(DamageType.HEAT, DamageType.ELECTRICITY), DamageType.RADIATION);
            put(new ElementalPair(DamageType.HEAT, DamageType.TOXIN), DamageType.GAS);
            put(new ElementalPair(DamageType.HEAT, DamageType.COLD), DamageType.BLAST);

            /*Electric*/
            put(new ElementalPair(DamageType.ELECTRICITY, DamageType.ELECTRICITY), DamageType.ELECTRICITY);
            put(new ElementalPair(DamageType.ELECTRICITY, DamageType.COLD), DamageType.MAGNETIC);
            put(new ElementalPair(DamageType.ELECTRICITY, DamageType.HEAT), DamageType.RADIATION);
            put(new ElementalPair(DamageType.ELECTRICITY, DamageType.TOXIN), DamageType.CORROSIVE);

            /*Cold*/
            put(new ElementalPair(DamageType.COLD, DamageType.COLD), DamageType.COLD);
            put(new ElementalPair(DamageType.COLD, DamageType.ELECTRICITY), DamageType.MAGNETIC);
            put(new ElementalPair(DamageType.COLD, DamageType.HEAT), DamageType.BLAST);
            put(new ElementalPair(DamageType.COLD, DamageType.TOXIN), DamageType.VIRAL);


            /*Toxin*/
            put(new ElementalPair(DamageType.TOXIN, DamageType.TOXIN), DamageType.TOXIN);
            put(new ElementalPair(DamageType.TOXIN, DamageType.ELECTRICITY), DamageType.CORROSIVE);
            put(new ElementalPair(DamageType.TOXIN, DamageType.HEAT), DamageType.GAS);
            put(new ElementalPair(DamageType.TOXIN, DamageType.COLD), DamageType.VIRAL);
        }};
    }

    public DamageType combineElements(DamageType damageType1, DamageType damageType2) {
        return damageCombinationMap.getOrDefault(new ElementalPair(damageType1, damageType2), null);
    }
}
