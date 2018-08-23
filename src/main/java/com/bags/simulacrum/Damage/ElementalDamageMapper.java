package com.bags.simulacrum.Damage;

import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ElementalDamageMapper {

    private static final Map<Pair<Damage, Damage>, Damage> damageCombinationMap;

    static {
        damageCombinationMap = new HashMap<Pair<Damage, Damage>, Damage>() {{
            /*Heat*/
            put(new Pair<>(new Damage(Damage.DamageType.HEAT), new Damage(Damage.DamageType.HEAT)), new Damage(Damage.DamageType.HEAT));
            put(new Pair<>(new Damage(Damage.DamageType.HEAT), new Damage(Damage.DamageType.ELECTRICITY)), new Damage(Damage.DamageType.RADIATION));
            put(new Pair<>(new Damage(Damage.DamageType.HEAT), new Damage(Damage.DamageType.TOXIN)), new Damage(Damage.DamageType.GAS));
            put(new Pair<>(new Damage(Damage.DamageType.HEAT), new Damage(Damage.DamageType.COLD)), new Damage(Damage.DamageType.BLAST));

            /*Electric*/
            put(new Pair<>(new Damage(Damage.DamageType.ELECTRICITY), new Damage(Damage.DamageType.ELECTRICITY)), new Damage(Damage.DamageType.ELECTRICITY));
            put(new Pair<>(new Damage(Damage.DamageType.ELECTRICITY), new Damage(Damage.DamageType.HEAT)), new Damage(Damage.DamageType.RADIATION));
            put(new Pair<>(new Damage(Damage.DamageType.ELECTRICITY), new Damage(Damage.DamageType.TOXIN)), new Damage(Damage.DamageType.CORROSIVE));
            put(new Pair<>(new Damage(Damage.DamageType.ELECTRICITY), new Damage(Damage.DamageType.COLD)), new Damage(Damage.DamageType.MAGNETIC));

            /*Cold*/
            put(new Pair<>(new Damage(Damage.DamageType.COLD), new Damage(Damage.DamageType.COLD)), new Damage(Damage.DamageType.COLD));
            put(new Pair<>(new Damage(Damage.DamageType.COLD), new Damage(Damage.DamageType.ELECTRICITY)), new Damage(Damage.DamageType.MAGNETIC));
            put(new Pair<>(new Damage(Damage.DamageType.COLD), new Damage(Damage.DamageType.HEAT)), new Damage(Damage.DamageType.BLAST));
            put(new Pair<>(new Damage(Damage.DamageType.COLD), new Damage(Damage.DamageType.TOXIN)), new Damage(Damage.DamageType.VIRAL));

            /*Toxin*/
            put(new Pair<>(new Damage(Damage.DamageType.TOXIN), new Damage(Damage.DamageType.TOXIN)), new Damage(Damage.DamageType.TOXIN));
            put(new Pair<>(new Damage(Damage.DamageType.TOXIN), new Damage(Damage.DamageType.ELECTRICITY)), new Damage(Damage.DamageType.CORROSIVE));
            put(new Pair<>(new Damage(Damage.DamageType.TOXIN), new Damage(Damage.DamageType.COLD)), new Damage(Damage.DamageType.VIRAL));
            put(new Pair<>(new Damage(Damage.DamageType.TOXIN), new Damage(Damage.DamageType.HEAT)), new Damage(Damage.DamageType.GAS));
        }};
    }

    public Damage combineElements(Damage damage1, Damage damage2) {
        return damageCombinationMap.getOrDefault(new Pair<>(damage1, damage2), null);
    }
}
