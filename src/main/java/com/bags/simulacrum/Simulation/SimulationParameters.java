package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Weapon.Weapon;
import lombok.Data;

import java.util.List;

@Data
public class SimulationParameters {

    private double duration;
    private double headshotChance;
    private boolean limitAmmo;
    private int iterations;
    private Weapon moddedWeapon;
    private List<Target> targetList;

}
