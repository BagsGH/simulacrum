package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.List;

@Data
public class SimulationSummary {

    private WeaponStateMetrics weaponStateMetrics;
    private FiredWeaponSummary firedWeaponSummary;
    private List<Target> killedTargets;
}
