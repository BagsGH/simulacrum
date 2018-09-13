package com.bags.simulacrum.Armor;

public enum HealthClass {
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

    public static boolean isArmor(HealthClass healthClass) {
        return healthClass.equals(FERRITE) || healthClass.equals(ALLOY);
    }

    public static boolean isShield(HealthClass healthClass) {
        return healthClass.equals(PROTO_SHIELD) || healthClass.equals(SHIELD);
    }
}