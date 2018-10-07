package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Entity.Target;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SimulationTargets {

    private Target primaryTarget;
    private List<Target> secondaryTargets;

    public List<Target> getAllTargets() {
        List<Target> allTargets = new ArrayList<>();
        allTargets.add(primaryTarget);
        allTargets.addAll(secondaryTargets);
        return allTargets;
    }

    public SimulationTargets(Target primary, List<Target> secondaryTargets) {
        this.primaryTarget = primary;
        this.secondaryTargets = secondaryTargets != null ? secondaryTargets : new ArrayList<>();
    }

    public SimulationTargets copy() {
        return new SimulationTargets(this.primaryTarget, this.secondaryTargets);
    }
}
