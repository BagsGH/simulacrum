package com.bags.simulacrum.Entity;

import lombok.Data;

@Data
public class BodyModifier {

    private BodyPart bodyPart;
    private boolean isHead;
    private double modifierValue;
    private double chanceToHit;

}
