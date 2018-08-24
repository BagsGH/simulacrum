package com.bags.simulacrum.Damage;

import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ElementalDamageMapper {

    private static final Map<Pair<DamageType, DamageType>, DamageType> damageCombinationMap;

    static {
        damageCombinationMap = new HashMap<Pair<DamageType, DamageType>, DamageType>() {{
            /*Heat*/
            put(new Pair<>(DamageType.HEAT, DamageType.HEAT), DamageType.HEAT);
            put(new Pair<>(DamageType.HEAT, DamageType.ELECTRICITY), DamageType.RADIATION);
            put(new Pair<>(DamageType.HEAT, DamageType.TOXIN), DamageType.GAS);
            put(new Pair<>(DamageType.HEAT, DamageType.COLD), DamageType.BLAST);

            /*Electric*/
            put(new Pair<>(DamageType.ELECTRICITY, DamageType.ELECTRICITY), DamageType.ELECTRICITY);
            put(new Pair<>(DamageType.ELECTRICITY, DamageType.COLD), DamageType.MAGNETIC);
            put(new Pair<>(DamageType.ELECTRICITY, DamageType.HEAT), DamageType.RADIATION);
            put(new Pair<>(DamageType.ELECTRICITY, DamageType.TOXIN), DamageType.CORROSIVE);

            /*Cold*/
            put(new Pair<>(DamageType.COLD, DamageType.COLD), DamageType.COLD);
            put(new Pair<>(DamageType.COLD, DamageType.ELECTRICITY), DamageType.MAGNETIC);
            put(new Pair<>(DamageType.COLD, DamageType.HEAT), DamageType.BLAST);
            put(new Pair<>(DamageType.COLD, DamageType.TOXIN), DamageType.VIRAL);


            /*Toxin*/
            put(new Pair<>(DamageType.TOXIN, DamageType.TOXIN), DamageType.TOXIN);
            put(new Pair<>(DamageType.TOXIN, DamageType.ELECTRICITY), DamageType.CORROSIVE);
            put(new Pair<>(DamageType.TOXIN, DamageType.HEAT), DamageType.GAS);
            put(new Pair<>(DamageType.TOXIN, DamageType.COLD), DamageType.VIRAL);
        }};
    }

    public DamageType combineElements(DamageType damageType1, DamageType damageType2) {
        return damageCombinationMap.getOrDefault(new Pair<>(damageType1, damageType2), null);
    }
}
