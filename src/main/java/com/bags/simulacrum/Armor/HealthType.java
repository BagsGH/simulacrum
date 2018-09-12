package com.bags.simulacrum.Armor;

public enum HealthType {
    CLONED_FLESH,
    FERRITE,
    ALLOY,
    MACHINERY,
    FLESH,
    SHIELD,
    PROTO_SHIELD,
    ROBOTIC,
    INFESTED,
    INFESTED_FLESH,
    FOSSILIZED,
    SINEW,
    OBJECT;

    public static boolean isArmor(HealthType healthType) {
        return healthType.equals(FERRITE) || healthType.equals(ALLOY);
    }

}