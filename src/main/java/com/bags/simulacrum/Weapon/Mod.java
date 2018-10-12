package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import lombok.Data;

@Data
public class Mod {

    private ModInformation modInformation;
    private double rangeLimitIncrease;
    private double fireRateIncrease;
    private double accuracyIncrease;
    private double magazineSizeIncrease;
    private double maxAmmoIncrease;
    private double reloadTimeIncrease;
    private double criticalChanceIncrease;
    private double criticalDamageIncrease;
    private double statusChanceIncrease;
    private double headshotMultiplierIncrease;
    private double damageIncrease;
    private Damage damage;
    private double multishotIncrease;

    public Mod() {
    }

    public Mod(Damage damage) {
        this.damage = damage;
    }

    public Mod copy() {
        return new ModBuilder(this.modInformation)
                .withRangeLimitIncrease(this.rangeLimitIncrease)
                .withFireRateIncrease(this.fireRateIncrease)
                .withAccuracyIncrease(this.accuracyIncrease)
                .withMagazineSizeIncrease(this.magazineSizeIncrease)
                .withMaxAmmoIncrease(this.maxAmmoIncrease)
                .withReloadTimeIncrease(this.reloadTimeIncrease)
                .withCriticalChanceIncrease(this.criticalChanceIncrease)
                .withCriticalDamageIncrease(this.criticalDamageIncrease)
                .withStatusChanceIncrease(this.statusChanceIncrease)
                .withHeadshotMultiplierIncrease(this.headshotMultiplierIncrease)
                .withDamageIncrease(this.damageIncrease)
                .withMultishotIncrease(this.multishotIncrease)
                .withDamage(this.damage)
                .build();
    }

    private Mod(ModBuilder builder) {
        this.modInformation = builder.modInformation != null ? builder.modInformation.copy() : null;
        this.rangeLimitIncrease = builder.rangeLimitIncrease;
        this.fireRateIncrease = builder.fireRateIncrease;
        this.accuracyIncrease = builder.accuracyIncrease;
        this.magazineSizeIncrease = builder.magazineSizeIncrease;
        this.maxAmmoIncrease = builder.maxAmmoIncrease;
        this.reloadTimeIncrease = builder.reloadTimeIncrease;
        this.criticalChanceIncrease = builder.criticalChanceIncrease;
        this.criticalDamageIncrease = builder.criticalDamageIncrease;
        this.statusChanceIncrease = builder.statusChanceIncrease;
        this.headshotMultiplierIncrease = builder.headshotMultiplierIncrease;
        this.damageIncrease = builder.damageIncrease;
        this.damage = builder.damage != null ? builder.damage.copy() : null;
        this.multishotIncrease = builder.multishotIncrease;
    }

    public static class ModBuilder {

        private ModInformation modInformation;
        private double rangeLimitIncrease;
        private double fireRateIncrease;
        private double accuracyIncrease;
        private double magazineSizeIncrease;
        private double maxAmmoIncrease;
        private double reloadTimeIncrease;
        private double criticalChanceIncrease;
        private double criticalDamageIncrease;
        private double statusChanceIncrease;
        private double headshotMultiplierIncrease;
        private double damageIncrease;
        private double multishotIncrease;
        private Damage damage;

        public ModBuilder(ModInformation modInformation) {
            this.modInformation = modInformation;
        }

        public ModBuilder withRangeLimitIncrease(double rangeLimitIncrease) {
            this.rangeLimitIncrease = rangeLimitIncrease;
            return this;
        }

        public ModBuilder withFireRateIncrease(double fireRateIncrease) {
            this.fireRateIncrease = fireRateIncrease;
            return this;
        }

        public ModBuilder withAccuracyIncrease(double accuracyIncrease) {
            this.accuracyIncrease = accuracyIncrease;
            return this;
        }

        public ModBuilder withMagazineSizeIncrease(double magazineSizeIncrease) {
            this.magazineSizeIncrease = magazineSizeIncrease;
            return this;
        }

        public ModBuilder withMaxAmmoIncrease(double maxAmmoIncrease) {
            this.maxAmmoIncrease = maxAmmoIncrease;
            return this;
        }

        public ModBuilder withReloadTimeIncrease(double reloadTimeIncrease) {
            this.reloadTimeIncrease = reloadTimeIncrease;
            return this;
        }

        public ModBuilder withCriticalChanceIncrease(double criticalChanceIncrease) {
            this.criticalChanceIncrease = criticalChanceIncrease;
            return this;
        }

        public ModBuilder withCriticalDamageIncrease(double criticalDamageIncrease) {
            this.criticalDamageIncrease = criticalDamageIncrease;
            return this;
        }

        public ModBuilder withStatusChanceIncrease(double statusChanceIncrease) {
            this.statusChanceIncrease = statusChanceIncrease;
            return this;
        }

        public ModBuilder withHeadshotMultiplierIncrease(double headshotMultiplierIncrease) {
            this.headshotMultiplierIncrease = headshotMultiplierIncrease;
            return this;
        }

        public ModBuilder withDamageIncrease(double damageIncrease) {
            this.damageIncrease = damageIncrease;
            return this;
        }

        public ModBuilder withMultishotIncrease(double multishotIncrease) {
            this.multishotIncrease = multishotIncrease;
            return this;
        }

        public ModBuilder withDamage(Damage damage) {
            this.damage = damage;
            return this;
        }


        public Mod build() {
            return new Mod(this);
        }
    }
}
