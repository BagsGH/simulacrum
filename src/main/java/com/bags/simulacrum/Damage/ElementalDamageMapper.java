package com.bags.simulacrum.Damage;

import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.bags.simulacrum.Damage.DamageType.*;

@Component
public class ElementalDamageMapper {

    private static final Map<Pair<DamageType, DamageType>, DamageType> damageCombinationMap;

    static {
        damageCombinationMap = new HashMap<Pair<DamageType, DamageType>, DamageType>() {{
            /*Heat*/
            put(new Pair<>(HEAT, HEAT), HEAT);
            put(new Pair<>(HEAT, ELECTRICITY), RADIATION);
            put(new Pair<>(HEAT, TOXIN), GAS);
            put(new Pair<>(HEAT, COLD), BLAST);

            /*Electric*/
            put(new Pair<>(ELECTRICITY, ELECTRICITY), ELECTRICITY);
            put(new Pair<>(ELECTRICITY, COLD), MAGNETIC);
            put(new Pair<>(ELECTRICITY, HEAT), RADIATION);
            put(new Pair<>(ELECTRICITY, TOXIN), CORROSIVE);

            /*Cold*/
            put(new Pair<>(COLD, COLD), COLD);
            put(new Pair<>(COLD, ELECTRICITY), MAGNETIC);
            put(new Pair<>(COLD, HEAT), BLAST);
            put(new Pair<>(COLD, TOXIN), VIRAL);


            /*Toxin*/
            put(new Pair<>(TOXIN, TOXIN), TOXIN);
            put(new Pair<>(TOXIN, ELECTRICITY), CORROSIVE);
            put(new Pair<>(TOXIN, HEAT), GAS);
            put(new Pair<>(TOXIN, COLD), VIRAL);
        }};
    }

    public DamageType combineElements(DamageType damageType1, DamageType damageType2) {
        return damageCombinationMap.getOrDefault(new Pair<>(damageType1, damageType2), null);
    }
}
