package com.bags.simulacrum.Weapon.WeaponInformationEnums;

import lombok.Data;

@Data
public class ChargingProperties {

    private double chargeTime;
    private double minBonusDamageFromCharging;
    private double maxBonusDamageFromCharging;
    private double minChargePercentage;

    public ChargingProperties(double chargeTime, double minChargePercentage, double minBonusDamageFromCharging, double maxBonusDamageFromCharging) {
        this.chargeTime = chargeTime;
        this.minBonusDamageFromCharging = minBonusDamageFromCharging;
        this.maxBonusDamageFromCharging = maxBonusDamageFromCharging;
        this.minChargePercentage = minChargePercentage;
    }

    public ChargingProperties copy() {
        return new ChargingProperties(this.getChargeTime(), this.getMinChargePercentage(), this.getMinBonusDamageFromCharging(), this.getMaxBonusDamageFromCharging());
    }
}
