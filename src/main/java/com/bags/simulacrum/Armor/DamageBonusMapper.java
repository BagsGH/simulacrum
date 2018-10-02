package com.bags.simulacrum.Armor;

import com.bags.simulacrum.Damage.DamageType;
import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.bags.simulacrum.Damage.DamageType.*;

@Component
public class DamageBonusMapper {

    private static final Map<Pair<HealthClass, DamageType>, Double> damagehealthMap;

    static {
        damagehealthMap = new HashMap<Pair<HealthClass, DamageType>, Double>() {{
            /*Flesh*/
            put(new Pair<>(HealthClass.FLESH, IMPACT), -0.25);
            put(new Pair<>(HealthClass.FLESH, SLASH), 0.25);
            put(new Pair<>(HealthClass.FLESH, TOXIN), 0.50);
            put(new Pair<>(HealthClass.FLESH, GAS), -0.25);
            put(new Pair<>(HealthClass.FLESH, VIRAL), 0.50);

            /*Cloned Flesh*/
            put(new Pair<>(HealthClass.CLONED_FLESH, IMPACT), -0.25);
            put(new Pair<>(HealthClass.CLONED_FLESH, SLASH), 0.25);
            put(new Pair<>(HealthClass.CLONED_FLESH, HEAT), 0.25);
            put(new Pair<>(HealthClass.CLONED_FLESH, GAS), -0.50);
            put(new Pair<>(HealthClass.CLONED_FLESH, VIRAL), 0.75);
            put(new Pair<>(HealthClass.CLONED_FLESH, VOID), -0.50);

            /*Fossilized*/
            put(new Pair<>(HealthClass.FOSSILIZED, SLASH), 0.15);
            put(new Pair<>(HealthClass.FOSSILIZED, COLD), -0.25);
            put(new Pair<>(HealthClass.FOSSILIZED, TOXIN), -0.50);
            put(new Pair<>(HealthClass.FOSSILIZED, BLAST), 0.25);
            put(new Pair<>(HealthClass.FOSSILIZED, CORROSIVE), 0.75);
            put(new Pair<>(HealthClass.FOSSILIZED, RADIATION), -0.75);
            put(new Pair<>(HealthClass.FOSSILIZED, VOID), -0.50);

            /*Infested*/
            put(new Pair<>(HealthClass.INFESTED, SLASH), 0.25);
            put(new Pair<>(HealthClass.INFESTED, HEAT), 0.25);
            put(new Pair<>(HealthClass.INFESTED, GAS), 0.75);
            put(new Pair<>(HealthClass.INFESTED, RADIATION), -0.50);
            put(new Pair<>(HealthClass.INFESTED, VIRAL), -0.50);

            /*Infested Flesh*/
            put(new Pair<>(HealthClass.INFESTED_FLESH, SLASH), 0.50);
            put(new Pair<>(HealthClass.INFESTED_FLESH, COLD), -0.50);
            put(new Pair<>(HealthClass.INFESTED_FLESH, HEAT), 0.50);
            put(new Pair<>(HealthClass.INFESTED_FLESH, GAS), 0.50);

            /*Sinew*/
            put(new Pair<>(HealthClass.SINEW, PUNCTURE), 0.25);
            put(new Pair<>(HealthClass.SINEW, COLD), 0.25);
            put(new Pair<>(HealthClass.SINEW, BLAST), -0.50);
            put(new Pair<>(HealthClass.SINEW, RADIATION), 0.50);

            /*Machinery*/
            put(new Pair<>(HealthClass.MACHINERY, IMPACT), 0.25);
            put(new Pair<>(HealthClass.MACHINERY, ELECTRICITY), 0.50);
            put(new Pair<>(HealthClass.MACHINERY, TOXIN), -0.25);
            put(new Pair<>(HealthClass.MACHINERY, BLAST), 0.75);
            put(new Pair<>(HealthClass.MACHINERY, VIRAL), -0.25);
            put(new Pair<>(HealthClass.MACHINERY, VOID), -0.50);

            /*Robotic*/
            put(new Pair<>(HealthClass.ROBOTIC, PUNCTURE), 0.25);
            put(new Pair<>(HealthClass.ROBOTIC, SLASH), -0.25);
            put(new Pair<>(HealthClass.ROBOTIC, ELECTRICITY), 0.50);
            put(new Pair<>(HealthClass.ROBOTIC, TOXIN), -0.25);
            put(new Pair<>(HealthClass.ROBOTIC, RADIATION), 0.25);

            /*Shield*/
            put(new Pair<>(HealthClass.SHIELD, IMPACT), 0.50);
            put(new Pair<>(HealthClass.SHIELD, PUNCTURE), -0.20);
            put(new Pair<>(HealthClass.SHIELD, COLD), 0.50);
            put(new Pair<>(HealthClass.SHIELD, MAGNETIC), 0.75);
            put(new Pair<>(HealthClass.SHIELD, RADIATION), -0.25);

            /*Proto Shield*/
            put(new Pair<>(HealthClass.PROTO_SHIELD, IMPACT), 0.15);
            put(new Pair<>(HealthClass.PROTO_SHIELD, PUNCTURE), -0.50);
            put(new Pair<>(HealthClass.PROTO_SHIELD, HEAT), -0.50);
            put(new Pair<>(HealthClass.PROTO_SHIELD, TOXIN), 0.25);
            put(new Pair<>(HealthClass.PROTO_SHIELD, CORROSIVE), -0.50);
            put(new Pair<>(HealthClass.PROTO_SHIELD, MAGNETIC), 0.75);

            /*Ferrite Armor*/
            put(new Pair<>(HealthClass.FERRITE, PUNCTURE), 0.50);
            put(new Pair<>(HealthClass.FERRITE, SLASH), -0.15);
            put(new Pair<>(HealthClass.FERRITE, TOXIN), 0.25);
            put(new Pair<>(HealthClass.FERRITE, BLAST), -0.25);
            put(new Pair<>(HealthClass.FERRITE, CORROSIVE), 0.75);

            /*Alloy Armor*/
            put(new Pair<>(HealthClass.ALLOY, PUNCTURE), 0.15);
            put(new Pair<>(HealthClass.ALLOY, SLASH), -0.50);
            put(new Pair<>(HealthClass.ALLOY, COLD), 0.25);
            put(new Pair<>(HealthClass.ALLOY, ELECTRICITY), -0.50);
            put(new Pair<>(HealthClass.ALLOY, MAGNETIC), -0.50);
            put(new Pair<>(HealthClass.ALLOY, RADIATION), 0.75);
        }};
    }

    public double getBonusModifier(DamageType damageType, HealthClass healthClass) {
        return damagehealthMap.getOrDefault(new Pair<>(healthClass, damageType), 0.0);
    }
}
