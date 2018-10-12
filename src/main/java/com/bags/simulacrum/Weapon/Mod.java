package com.bags.simulacrum.Weapon;

import com.bags.simulacrum.Damage.Damage;
import lombok.Data;

@Data
public class Mod {

    private String name;
    private int level;
    private int drain;
    private String rarity;
    private Polarity polarity;
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
    private int index;

    public enum Polarity { //TODO: own class
        DASH,
        D,
        V,
        EQUALS
    }

    public Mod() {
    }

    public Mod(Damage damage) {
        this.damage = damage;
    }

    public Mod copy() {
        return new ModBuilder(this.name, this.index, this.level, this.drain, this.rarity, this.polarity)
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
                .withDamage(this.damage.copy())
                .build();
    }

    private Mod(ModBuilder builder) {
        this.name = builder.name;
        this.level = builder.level;
        this.drain = builder.drain;
        this.rarity = builder.rarity;
        this.polarity = builder.polarity;
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
        this.damage = builder.damage.copy();
        this.multishotIncrease = builder.multishotIncrease;
        this.index = builder.index;
    }

    public static class ModBuilder {
        private String name;
        private int level;
        private int drain;
        private String rarity;
        private Polarity polarity;
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
        private int index;


        public ModBuilder(String name, int level, int drain, String rarity, Polarity polarity) {
            this.name = name;
            this.level = level;
            this.drain = drain;
            this.rarity = rarity;
            this.polarity = polarity;
        }

        public ModBuilder(String name, int index, int level, int drain, String rarity, Polarity polarity) {
            this.name = name;
            this.index = index;
            this.level = level;
            this.drain = drain;
            this.rarity = rarity;
            this.polarity = polarity;
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
