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
        }};
    }

    public Double getBonus(HealthType healthType, DamageType damageType) {
        return damagehealthMap.getOrDefault(new Pair<>(healthType, damageType), null);
    }
}
