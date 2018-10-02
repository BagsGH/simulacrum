package com.bags.simulacrum.Weapon.WeaponInformationEnums;

import org.junit.Before;
import org.junit.Test;

public class ChargingPropertiesTest {

    ChargingProperties subject;

    @Before
    public void setup() {
        subject = new ChargingProperties(0.0, 0.0, 0.0, 0.0);
    }

    @Test
    public void getChargeTime() {
        subject.getChargeTime();
    }

    @Test
    public void getMinBonusDamageFromCharging() {
        subject.getMinBonusDamageFromCharging();
    }

    @Test
    public void getMaxBonusDamageFromCharging() {
        subject.getMaxBonusDamageFromCharging();
    }

    @Test
    public void getMinChargePercentage() {
        subject.getMinChargePercentage();
    }

    @Test
    public void setChargeTime() {
        subject.setChargeTime(5);
    }

    @Test
    public void setMinBonusDamageFromCharging() {
        subject.setMinBonusDamageFromCharging(5);
    }

    @Test
    public void setMaxBonusDamageFromCharging() {
        subject.setMaxBonusDamageFromCharging(5);
    }

    @Test
    public void setMinChargePercentage() {
        subject.setMinBonusDamageFromCharging(5);
    }
}