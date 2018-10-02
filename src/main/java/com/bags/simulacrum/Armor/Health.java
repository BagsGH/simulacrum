package com.bags.simulacrum.Armor;

import lombok.Data;

@Data
public class Health {

    private double healthValueMax;
    private double healthValue;
    private HealthClass healthClass;

    public Health(HealthClass hc, double healthValue) {
        this.healthValueMax = healthValue;
        this.healthValue = healthValue;
        this.healthClass = hc;
    }

    public void subtractHealthValue(double value) {
        healthValue = healthValue - value;
    }

    public Health copy() {
        Health copiedHealth = new Health(this.healthClass, this.healthValue);
        copiedHealth.setHealthValueMax(this.healthValue);
        return copiedHealth;
    }
}
