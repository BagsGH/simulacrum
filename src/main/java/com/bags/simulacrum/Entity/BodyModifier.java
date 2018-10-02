package com.bags.simulacrum.Entity;

import lombok.Data;

@Data
public class BodyModifier {

    private BodyPart bodyPart;
    private double modifierValue;
    private double chanceToHit;

    public BodyModifier(BodyPart bodyPart, double modifierValue, double chanceToHit) {
        this.bodyPart = bodyPart;
        this.modifierValue = modifierValue;
        this.chanceToHit = chanceToHit;
    }

    public BodyModifier(BodyPart bodyPart, double modifierValue) {
        this.bodyPart = bodyPart;
        this.modifierValue = modifierValue;
    }

    public BodyModifier copy() {
        return new BodyModifier(this.bodyPart, this.modifierValue, this.chanceToHit);
    }

}
