package com.bags.simulacrum.Entity;

public enum BodyPart {
    HEAD,
    TORSO,
    FANNY_PACK,
    GUN,
    BACK_CONSOLE,
    BACK,
    EXPOSED_CHEST,
    ORANGE_POSTULES,
    FACE,
    MOUTH;

    public boolean isHead(BodyPart bodyPart) {
        return bodyPart.equals(HEAD) || bodyPart.equals(FACE) || bodyPart.equals(MOUTH) || bodyPart.equals(HEAD) || bodyPart.equals(GUN);
    }
}
