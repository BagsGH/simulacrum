package com.bags.simulacrum.Weapon;

import lombok.Data;

@Data
public class ModInformation {

    private String name;
    private int level;
    private int drain;
    private String rarity;
    private Polarity polarity;
    private int index;

    public ModInformation(String name, int index, int level, int drain, String rarity, Polarity polarity) {
        this.name = name;
        this.level = level;
        this.drain = drain;
        this.rarity = rarity;
        this.polarity = polarity;
        this.index = index;
    }

    public ModInformation copy() {
        return new ModInformation(this.name, this.index, this.level, this.drain, this.rarity, this.polarity);
    }
}
