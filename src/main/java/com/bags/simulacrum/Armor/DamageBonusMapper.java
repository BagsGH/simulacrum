package com.bags.simulacrum.Armor;

import com.bags.simulacrum.Damage.DamageType;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class DamageBonusMapper {

    private static final Map<Pair<HealthClass, DamageType>, Double> damagehealthMap;

    static {
        damagehealthMap = new HashMap<Pair<HealthClass, DamageType>, Double>() {{
            /*Flesh*/
            put(new Pair<>(HealthClass.FLESH, DamageType.IMPACT), -0.25);
            put(new Pair<>(HealthClass.FLESH, DamageType.SLASH), 0.25);
            put(new Pair<>(HealthClass.FLESH, DamageType.TOXIN), 0.50);
            put(new Pair<>(HealthClass.FLESH, DamageType.GAS), -0.25);
            put(new Pair<>(HealthClass.FLESH, DamageType.VIRAL), 0.50);

            /*Cloned Flesh*/
            put(new Pair<>(HealthClass.CLONED_FLESH, DamageType.IMPACT), -0.25);
            put(new Pair<>(HealthClass.CLONED_FLESH, DamageType.SLASH), 0.25);
            put(new Pair<>(HealthClass.CLONED_FLESH, DamageType.HEAT), 0.25);
            put(new Pair<>(HealthClass.CLONED_FLESH, DamageType.GAS), -0.50);
            put(new Pair<>(HealthClass.CLONED_FLESH, DamageType.VIRAL), 0.75);
            put(new Pair<>(HealthClass.CLONED_FLESH, DamageType.VOID), -0.50);

            /*Fossilized*/
            put(new Pair<>(HealthClass.FOSSILIZED, DamageType.SLASH), 0.15);
            put(new Pair<>(HealthClass.FOSSILIZED, DamageType.COLD), -0.25);
            put(new Pair<>(HealthClass.FOSSILIZED, DamageType.TOXIN), -0.50);
            put(new Pair<>(HealthClass.FOSSILIZED, DamageType.BLAST), 0.25);
            put(new Pair<>(HealthClass.FOSSILIZED, DamageType.CORROSIVE), 0.75);
            put(new Pair<>(HealthClass.FOSSILIZED, DamageType.RADIATION), -0.75);
            put(new Pair<>(HealthClass.FOSSILIZED, DamageType.VOID), -0.50);

            /*Infested*/
            put(new Pair<>(HealthClass.INFESTED, DamageType.SLASH), 0.25);
            put(new Pair<>(HealthClass.INFESTED, DamageType.HEAT), 0.25);
            put(new Pair<>(HealthClass.INFESTED, DamageType.GAS), 0.75);
            put(new Pair<>(HealthClass.INFESTED, DamageType.RADIATION), -0.50);
            put(new Pair<>(HealthClass.INFESTED, DamageType.VIRAL), -0.50);

            /*Infested Flesh*/
            put(new Pair<>(HealthClass.INFESTED_FLESH, DamageType.SLASH), 0.50);
            put(new Pair<>(HealthClass.INFESTED_FLESH, DamageType.COLD), -0.50);
            put(new Pair<>(HealthClass.INFESTED_FLESH, DamageType.HEAT), 0.50);
            put(new Pair<>(HealthClass.INFESTED_FLESH, DamageType.GAS), 0.50);

            /*Sinew*/
            put(new Pair<>(HealthClass.SINEW, DamageType.PUNCTURE), 0.25);
            put(new Pair<>(HealthClass.SINEW, DamageType.COLD), 0.25);
            put(new Pair<>(HealthClass.SINEW, DamageType.BLAST), -0.50);
            put(new Pair<>(HealthClass.SINEW, DamageType.RADIATION), 0.50);

            /*Machinery*/
            put(new Pair<>(HealthClass.MACHINERY, DamageType.IMPACT), 0.25);
            put(new Pair<>(HealthClass.MACHINERY, DamageType.ELECTRICITY), 0.50);
            put(new Pair<>(HealthClass.MACHINERY, DamageType.TOXIN), -0.25);
            put(new Pair<>(HealthClass.MACHINERY, DamageType.BLAST), 0.75);
            put(new Pair<>(HealthClass.MACHINERY, DamageType.VIRAL), -0.25);
            put(new Pair<>(HealthClass.MACHINERY, DamageType.VOID), -0.50);

            /*Robotic*/
            put(new Pair<>(HealthClass.ROBOTIC, DamageType.PUNCTURE), 0.25);
            put(new Pair<>(HealthClass.ROBOTIC, DamageType.SLASH), -0.25);
            put(new Pair<>(HealthClass.ROBOTIC, DamageType.ELECTRICITY), 0.50);
            put(new Pair<>(HealthClass.ROBOTIC, DamageType.TOXIN), -0.25);
            put(new Pair<>(HealthClass.ROBOTIC, DamageType.RADIATION), 0.25);

            /*Shield*/
            put(new Pair<>(HealthClass.SHIELD, DamageType.IMPACT), 0.50);
            put(new Pair<>(HealthClass.SHIELD, DamageType.PUNCTURE), -0.20);
            put(new Pair<>(HealthClass.SHIELD, DamageType.COLD), 0.50);
            put(new Pair<>(HealthClass.SHIELD, DamageType.MAGNETIC), 0.75);
            put(new Pair<>(HealthClass.SHIELD, DamageType.RADIATION), -0.25);

            /*Proto Shield*/
            put(new Pair<>(HealthClass.PROTO_SHIELD, DamageType.IMPACT), 0.15);
            put(new Pair<>(HealthClass.PROTO_SHIELD, DamageType.PUNCTURE), -0.50);
            put(new Pair<>(HealthClass.PROTO_SHIELD, DamageType.HEAT), -0.50);
            put(new Pair<>(HealthClass.PROTO_SHIELD, DamageType.TOXIN), 0.25);
            put(new Pair<>(HealthClass.PROTO_SHIELD, DamageType.CORROSIVE), -0.50);
            put(new Pair<>(HealthClass.PROTO_SHIELD, DamageType.MAGNETIC), 0.75);

            /*Ferrite Armor*/
            put(new Pair<>(HealthClass.FERRITE, DamageType.PUNCTURE), 0.50);
            put(new Pair<>(HealthClass.FERRITE, DamageType.SLASH), -0.15);
            put(new Pair<>(HealthClass.FERRITE, DamageType.TOXIN), 0.25);
            put(new Pair<>(HealthClass.FERRITE, DamageType.BLAST), -0.25);
            put(new Pair<>(HealthClass.FERRITE, DamageType.CORROSIVE), 0.75);

            /*Alloy Armor*/
            put(new Pair<>(HealthClass.ALLOY, DamageType.PUNCTURE), 0.15);
            put(new Pair<>(HealthClass.ALLOY, DamageType.SLASH), -0.50);
            put(new Pair<>(HealthClass.ALLOY, DamageType.COLD), 0.25);
            put(new Pair<>(HealthClass.ALLOY, DamageType.ELECTRICITY), -0.50);
            put(new Pair<>(HealthClass.ALLOY, DamageType.MAGNETIC), -0.50);
            put(new Pair<>(HealthClass.ALLOY, DamageType.RADIATION), 0.75);
        }};
    }

    public double getBonus(HealthClass healthClass, DamageType damageType) {
        return damagehealthMap.getOrDefault(new Pair<>(healthClass, damageType), 0.0);
    }
}
