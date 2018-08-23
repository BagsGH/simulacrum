package com.bags.simulacrum.Damage;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ElementalDamageMapper {

    private static final Map<ElementalPair, Damage.DamageType> damageCombinationMap;

    static {
        damageCombinationMap = new HashMap<ElementalPair, Damage.DamageType>() {{
            /*Heat*/
            put(new ElementalPair(Damage.DamageType.HEAT, Damage.DamageType.HEAT), Damage.DamageType.HEAT);
            put(new ElementalPair(Damage.DamageType.HEAT, Damage.DamageType.ELECTRICITY), Damage.DamageType.RADIATION);
            put(new ElementalPair(Damage.DamageType.HEAT, Damage.DamageType.TOXIN), Damage.DamageType.GAS);
            put(new ElementalPair(Damage.DamageType.HEAT, Damage.DamageType.COLD), Damage.DamageType.BLAST);

            /*Electric*/
            put(new ElementalPair(Damage.DamageType.ELECTRICITY, Damage.DamageType.ELECTRICITY), Damage.DamageType.ELECTRICITY);
            put(new ElementalPair(Damage.DamageType.ELECTRICITY, Damage.DamageType.COLD), Damage.DamageType.MAGNETIC);
            put(new ElementalPair(Damage.DamageType.ELECTRICITY, Damage.DamageType.HEAT), Damage.DamageType.RADIATION);
            put(new ElementalPair(Damage.DamageType.ELECTRICITY, Damage.DamageType.TOXIN), Damage.DamageType.CORROSIVE);

            /*Cold*/
            put(new ElementalPair(Damage.DamageType.COLD, Damage.DamageType.COLD), Damage.DamageType.COLD);
            put(new ElementalPair(Damage.DamageType.COLD, Damage.DamageType.ELECTRICITY), Damage.DamageType.MAGNETIC);
            put(new ElementalPair(Damage.DamageType.COLD, Damage.DamageType.HEAT), Damage.DamageType.BLAST);
            put(new ElementalPair(Damage.DamageType.COLD, Damage.DamageType.TOXIN), Damage.DamageType.VIRAL);


            /*Toxin*/
            put(new ElementalPair(Damage.DamageType.TOXIN, Damage.DamageType.TOXIN), Damage.DamageType.TOXIN);
            put(new ElementalPair(Damage.DamageType.TOXIN, Damage.DamageType.ELECTRICITY), Damage.DamageType.CORROSIVE);
            put(new ElementalPair(Damage.DamageType.TOXIN, Damage.DamageType.HEAT), Damage.DamageType.GAS);
            put(new ElementalPair(Damage.DamageType.TOXIN, Damage.DamageType.COLD), Damage.DamageType.VIRAL);
        }};
    }

    public Damage.DamageType combineElements(Damage.DamageType damage1, Damage.DamageType damage2) {
        System.out.println("Pair: " + new ElementalPair(damage1, damage2));
        return damageCombinationMap.getOrDefault(new ElementalPair(damage1, damage2), null);
    }
}
