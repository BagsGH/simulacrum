package com.bags.simulacrum.Armor;

import lombok.Data;

@Data
public class Health {

    private double valueMax;
    private double value;
    private HealthClass healthClass;

    public Health(HealthClass hc, double value) {
        this.valueMax = value;
        this.value = value;
        this.healthClass = hc;
    }
}
