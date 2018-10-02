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
        this.fireRate = builder.getFireRate();
        this.magazineSize = builder.getMagazineSize();
        this.reloadTime = builder.getReloadTime();
        this.maxAmmo = builder.getMaxAmmo();
        this.triggerType = builder.getTriggerType();
        this.burstCount = builder.getBurstCount();
        this.spoolingSpeedDecreaseModifier = builder.getSpoolingSpeedDecreaseModifier();
        this.spoolThreshold = builder.getSpoolThreshold();
        this.chargingProperties = (builder.getChargingProperties() != null ? builder.getChargingProperties().copy() : new ChargingProperties(0.0, 0.0, 0.0, 0.0));
    }

    @Data
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

        public FireStateProperties build() {
            return new FireStateProperties(this);
        }
    }
}
