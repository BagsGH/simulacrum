package com.bags.simulacrum.Armor;

import com.bags.simulacrum.Damage.DamageType;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class DamageBonusMapper {

    private static final Map<Pair<HealthType, DamageType>, Double> damagehealthMap;

    static {
        damagehealthMap = new HashMap<Pair<HealthType, DamageType>, Double>() {{
            /*Flesh*/
            put(new Pair<>(HealthType.FLESH, DamageType.IMPACT), -0.25);
            put(new Pair<>(HealthType.FLESH, DamageType.SLASH), 0.25);
            put(new Pair<>(HealthType.FLESH, DamageType.TOXIN), 0.50);
            put(new Pair<>(HealthType.FLESH, DamageType.GAS), -0.25);
            put(new Pair<>(HealthType.FLESH, DamageType.VIRAL), 0.50);

            /*Cloned Flesh*/
            put(new Pair<>(HealthType.CLONED_FLESH, DamageType.IMPACT), -0.25);
            put(new Pair<>(HealthType.CLONED_FLESH, DamageType.SLASH), 0.25);
            put(new Pair<>(HealthType.CLONED_FLESH, DamageType.HEAT), 0.25);
            put(new Pair<>(HealthType.CLONED_FLESH, DamageType.GAS), -0.50);
            put(new Pair<>(HealthType.CLONED_FLESH, DamageType.VIRAL), 0.75);
            put(new Pair<>(HealthType.CLONED_FLESH, DamageType.VOID), -0.50);

            /*Fossilized*/
            put(new Pair<>(HealthType.FOSSILIZED, DamageType.SLASH), 0.15);
            put(new Pair<>(HealthType.FOSSILIZED, DamageType.COLD), -0.25);
            put(new Pair<>(HealthType.FOSSILIZED, DamageType.TOXIN), -0.50);
            put(new Pair<>(HealthType.FOSSILIZED, DamageType.BLAST), 0.25);
            put(new Pair<>(HealthType.FOSSILIZED, DamageType.CORROSIVE), 0.75);
            put(new Pair<>(HealthType.FOSSILIZED, DamageType.RADIATION), -0.75);
            put(new Pair<>(HealthType.FOSSILIZED, DamageType.VOID), -0.50);

            /*Infested*/
            put(new Pair<>(HealthType.INFESTED, DamageType.SLASH), 0.25);
            put(new Pair<>(HealthType.INFESTED, DamageType.HEAT), 0.25);
            put(new Pair<>(HealthType.INFESTED, DamageType.GAS), 0.75);
            put(new Pair<>(HealthType.INFESTED, DamageType.RADIATION), -0.50);
            put(new Pair<>(HealthType.INFESTED, DamageType.VIRAL), -0.50);

            /*Infested Flesh*/
            put(new Pair<>(HealthType.INFESTED_FLESH, DamageType.SLASH), 0.50);
            put(new Pair<>(HealthType.INFESTED_FLESH, DamageType.COLD), -0.50);
            put(new Pair<>(HealthType.INFESTED_FLESH, DamageType.HEAT), 0.50);
            put(new Pair<>(HealthType.INFESTED_FLESH, DamageType.GAS), 0.50);

            /*Sinew*/
            put(new Pair<>(HealthType.SINEW, DamageType.PUNCTURE), 0.25);
            put(new Pair<>(HealthType.SINEW, DamageType.COLD), 0.25);
            put(new Pair<>(HealthType.SINEW, DamageType.BLAST), -0.50);
            put(new Pair<>(HealthType.SINEW, DamageType.RADIATION), 0.50);

            /*Machinery*/
            put(new Pair<>(HealthType.MACHINERY, DamageType.IMPACT), 0.25);
            put(new Pair<>(HealthType.MACHINERY, DamageType.ELECTRICITY), 0.50);
            put(new Pair<>(HealthType.MACHINERY, DamageType.TOXIN), -0.25);
            put(new Pair<>(HealthType.MACHINERY, DamageType.BLAST), 0.75);
            put(new Pair<>(HealthType.MACHINERY, DamageType.VIRAL), -0.25);
            put(new Pair<>(HealthType.MACHINERY, DamageType.VOID), -0.50);

            /*Robotic*/
            put(new Pair<>(HealthType.ROBOTIC, DamageType.PUNCTURE), 0.25);
            put(new Pair<>(HealthType.ROBOTIC, DamageType.SLASH), -0.25);
            put(new Pair<>(HealthType.ROBOTIC, DamageType.ELECTRICITY), 0.50);
            put(new Pair<>(HealthType.ROBOTIC, DamageType.TOXIN), -0.25);
            put(new Pair<>(HealthType.ROBOTIC, DamageType.RADIATION), 0.25);

            /*Shield*/
            put(new Pair<>(HealthType.SHIELD, DamageType.IMPACT), 0.50);
            put(new Pair<>(HealthType.SHIELD, DamageType.PUNCTURE), -0.20);
            put(new Pair<>(HealthType.SHIELD, DamageType.COLD), 0.50);
            put(new Pair<>(HealthType.SHIELD, DamageType.MAGNETIC), 0.75);
            put(new Pair<>(HealthType.SHIELD, DamageType.RADIATION), -0.25);

            /*Proto Shield*/
            put(new Pair<>(HealthType.PROTO_SHIELD, DamageType.IMPACT), 0.15);
            put(new Pair<>(HealthType.PROTO_SHIELD, DamageType.PUNCTURE), -0.50);
            put(new Pair<>(HealthType.PROTO_SHIELD, DamageType.HEAT), -0.50);
            put(new Pair<>(HealthType.PROTO_SHIELD, DamageType.TOXIN), 0.25);
            put(new Pair<>(HealthType.PROTO_SHIELD, DamageType.CORROSIVE), -0.50);
            put(new Pair<>(HealthType.PROTO_SHIELD, DamageType.MAGNETIC), 0.75);

            /*Ferrite Armor*/
            put(new Pair<>(HealthType.FERRITE, DamageType.PUNCTURE), 0.50);
            put(new Pair<>(HealthType.FERRITE, DamageType.SLASH), -0.15);
            put(new Pair<>(HealthType.FERRITE, DamageType.TOXIN), 0.25);
            put(new Pair<>(HealthType.FERRITE, DamageType.BLAST), -0.25);
            put(new Pair<>(HealthType.FERRITE, DamageType.CORROSIVE), 0.75);

            /*Alloy Armor*/
            put(new Pair<>(HealthType.ALLOY, DamageType.PUNCTURE), 0.15);
            put(new Pair<>(HealthType.ALLOY, DamageType.SLASH), -0.50);
            put(new Pair<>(HealthType.ALLOY, DamageType.COLD), 0.25);
            put(new Pair<>(HealthType.ALLOY, DamageType.ELECTRICITY), -0.50);
            put(new Pair<>(HealthType.ALLOY, DamageType.MAGNETIC), -0.50);
            put(new Pair<>(HealthType.ALLOY, DamageType.RADIATION), 0.75);
        }};
    }

    public Double getBonus(HealthType healthType, DamageType damageType) {
        return damagehealthMap.getOrDefault(new Pair<>(healthType, damageType), null);
    }
}
