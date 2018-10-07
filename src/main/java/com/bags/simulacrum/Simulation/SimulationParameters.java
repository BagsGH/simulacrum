package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Weapon.Weapon;
import lombok.Data;

@Data
public class SimulationParameters {

    private double duration;
    private double headshotChance;
    private boolean limitAmmo;
    private boolean replaceDeadTargets;
    private int iterations;
    private Weapon moddedWeapon;
    private SimulationTargets simulationTargets;

}
