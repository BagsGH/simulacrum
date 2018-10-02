package com.bags.simulacrum.Damage;

import java.util.Arrays;
import java.util.List;

public enum DamageType {
    PUNCTURE,
    IMPACT,
    SLASH,
    ELECTRICITY,
    COLD,
    HEAT,
    TOXIN,
    TRUE,
    VOID,
    BLAST,
    CORROSIVE,
    GAS,
    MAGNETIC,
    RADIATION,
    VIRAL;

    public final static List<DamageType> elementalDamageTypes = Arrays.asList(ELECTRICITY, COLD, HEAT, TOXIN,
            BLAST, VOID, CORROSIVE, GAS, MAGNETIC, RADIATION, VIRAL);

    public static boolean isElemental(DamageType damageType) {
        return damageType.equals(ELECTRICITY) || damageType.equals(COLD) || damageType.equals(HEAT) || damageType.equals(TOXIN) || damageType.equals(BLAST) ||
                damageType.equals(CORROSIVE) || damageType.equals(GAS) || damageType.equals(MAGNETIC) || damageType.equals(RADIATION) || damageType.equals(VIRAL);
    }

    public static boolean isCombinedElemental(DamageType damageType) {
        return damageType.equals(BLAST) || damageType.equals(CORROSIVE) || damageType.equals(GAS) || damageType.equals(MAGNETIC) || damageType.equals(RADIATION) || damageType.equals(VIRAL);
    }

    public static boolean isIPS(DamageType damageType) {
        return damageType.equals(IMPACT) || damageType.equals(PUNCTURE) || damageType.equals(SLASH);
    }
}