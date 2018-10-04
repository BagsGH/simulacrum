package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Weapon.WeaponInformationEnums.ChargingProperties;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType;
import lombok.Data;

@Data
public class FireStateProperties {

    private double reloadTime;
    private TriggerType triggerType;

    private double fireRate;
    private double percentToCharge;
    private ChargingProperties chargingProperties;
    private int burstCount;
    private double spoolingSpeedDecreaseModifier;
    private int spoolThreshold;

    private Ammunition ammunition;

    public void subtractAmmo() {
        this.ammunition.subtractAmmo();
    }

    public void loadMagazine() {
        this.ammunition.loadMagazine();
    }

    public int getCurrentAmmo() {
        return this.ammunition.getCurrentAmmo();
    }

    public int getMaxAmmo() {
        return this.ammunition.getMaxAmmo();
    }

    public int getCurrentMagazineSize() {
        return this.ammunition.getCurrentMagazineSize();
    }

    public int getMagazineSize() {
        return this.ammunition.getMagazineSize();
    }

    public void setMagazineSize(int newMagazineSize) {
        this.ammunition.setMagazineSize(newMagazineSize);
    }

    public void setCurrentMagazineSize(int currentMagazineSize) {
        this.ammunition.setCurrentMagazineSize(currentMagazineSize);
    }

    public void setMaxAmmo(int maxAmmo) {
        this.ammunition.setMaxAmmo(maxAmmo);
    }

    public void setCurrentAmmo(int currentAmmo) {
        this.ammunition.setCurrentAmmo(currentAmmo);
    }

    private FireStateProperties(FireStatePropertiesBuilder builder) {
        this.fireRate = builder.fireRate;
        this.reloadTime = builder.reloadTime;
        this.triggerType = builder.triggerType;
        this.burstCount = builder.burstCount;
        this.spoolingSpeedDecreaseModifier = builder.spoolingSpeedDecreaseModifier;
        this.spoolThreshold = builder.spoolThreshold;
        this.chargingProperties = (builder.chargingProperties != null ? builder.chargingProperties.copy() : new ChargingProperties(0.0, 0.0, 0.0, 0.0));
        this.percentToCharge = builder.percentToCharge;
        this.ammunition = builder.ammunition.copy();
    }

    public FireStateProperties copy() {
        return new FireStateProperties.FireStatePropertiesBuilder(this.triggerType, this.reloadTime)
                .withFireRate(this.fireRate)
                .withBurstCount(this.burstCount)
                .withSpoolingSpeedDecreaseModifier(this.spoolingSpeedDecreaseModifier)
                .withSpoolThreshold(this.spoolThreshold)
                .withChargingProperties(this.chargingProperties.copy())
                .withPercentToCharge(this.percentToCharge)
                .withAmmunition(this.ammunition.copy())
                .build();
    }

    public static class FireStatePropertiesBuilder {

        private double reloadTime;
        private TriggerType triggerType;

        private double fireRate;
        private int burstCount;
        private double spoolingSpeedDecreaseModifier;
        private int spoolThreshold;

        private double percentToCharge;
        private Ammunition ammunition;
        private ChargingProperties chargingProperties;

        public FireStatePropertiesBuilder(TriggerType triggerType, double reloadTime, int maxAmmo, int magazineSize) {
            this.triggerType = triggerType;
            this.reloadTime = reloadTime;
            this.ammunition = new Ammunition(maxAmmo, maxAmmo, magazineSize, magazineSize);
        }

        private FireStatePropertiesBuilder(TriggerType triggerType, double reloadTime) {
            this.triggerType = triggerType;
            this.reloadTime = reloadTime;
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

        public FireStatePropertiesBuilder withAmmunition(Ammunition ammunition) {
            this.ammunition = ammunition;
            return this;
        }

        public FireStateProperties build() {
            return new FireStateProperties(this);
        }
    }
}
