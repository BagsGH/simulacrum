package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.WeaponInformationEnums.ChargingProperties;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType;
import lombok.Data;

@Data
public class FireStateProperties {

    private int magazineSize;
    private double reloadTime;
    private int maxAmmo;
    private TriggerType triggerType;

    private double fireRate;
    private double percentToCharge;
    private ChargingProperties chargingProperties;
    private int burstCount;
    private double spoolingSpeedDecreaseModifier;
    private int spoolThreshold;

    private int currentMagazineSize;

    public void subtractAmmo() {
        this.currentMagazineSize--;
    }

    public void loadMagazine() {
        this.currentMagazineSize = magazineSize;
    }

    private FireStateProperties(FireStatePropertiesBuilder builder) {
        this.fireRate = builder.fireRate;
        this.magazineSize = builder.magazineSize;
        this.reloadTime = builder.reloadTime;
        this.maxAmmo = builder.maxAmmo;
        this.triggerType = builder.triggerType;
        this.burstCount = builder.burstCount;
        this.spoolingSpeedDecreaseModifier = builder.spoolingSpeedDecreaseModifier;
        this.spoolThreshold = builder.spoolThreshold;
        this.chargingProperties = (builder.chargingProperties != null ? builder.chargingProperties.copy() : new ChargingProperties(0.0, 0.0, 0.0, 0.0));
        this.currentMagazineSize = builder.currentMagazineSize;
        this.percentToCharge = builder.percentToCharge;
    }

    public FireStateProperties copy() {
        return new FireStateProperties.FireStatePropertiesBuilder(this.triggerType, this.reloadTime, this.magazineSize, this.maxAmmo)
                .withFireRate(this.fireRate)
                .withBurstCount(this.burstCount)
                .withSpoolingSpeedDecreaseModifier(this.spoolingSpeedDecreaseModifier)
                .withSpoolThreshold(this.spoolThreshold)
                .withChargingProperties(this.chargingProperties.copy())
                .withPercentToCharge(this.percentToCharge)
                .withCurrentMagazineSize(this.currentMagazineSize)
                .build();
    }

    public static class FireStatePropertiesBuilder {

        private int magazineSize;
        private double reloadTime;
        private int maxAmmo;
        private TriggerType triggerType;

        private double fireRate;
        private int burstCount;
        private double spoolingSpeedDecreaseModifier;
        private int spoolThreshold;

        private int currentMagazineSize;
        private double percentToCharge;

        private ChargingProperties chargingProperties;

        public FireStatePropertiesBuilder(TriggerType triggerType, double reloadTime, int magazineSize, int maxAmmo) {
            this.triggerType = triggerType;
            this.reloadTime = reloadTime;
            this.magazineSize = magazineSize;
            this.maxAmmo = maxAmmo;
        }

        public FireStatePropertiesBuilder withFireRate(double fireRate) {
            this.fireRate = fireRate;
            return this;
        }

        public FireStatePropertiesBuilder withBurstCount(int burstCount) {
            this.burstCount = burstCount;
            return this;
        }

        public FireStatePropertiesBuilder withSpoolingSpeedDecreaseModifier(double spoolingSpeedDecreaseModifier) {
            this.spoolingSpeedDecreaseModifier = spoolingSpeedDecreaseModifier;
            return this;
        }

        public FireStatePropertiesBuilder withSpoolThreshold(int spoolThreshold) {
            this.spoolThreshold = spoolThreshold;
            return this;
        }

        public FireStatePropertiesBuilder withChargingProperties(ChargingProperties chargingProperties) {
            this.chargingProperties = chargingProperties;
            return this;
        }

        public FireStatePropertiesBuilder withPercentToCharge(double percentToCharge) {
            this.percentToCharge = percentToCharge;
            return this;
        }

        public FireStatePropertiesBuilder withCurrentMagazineSize(int currentMagazineSize) {
            this.currentMagazineSize = currentMagazineSize;
            return this;
        }

        public FireStateProperties build() {
            return new FireStateProperties(this);
        }
    }
}
