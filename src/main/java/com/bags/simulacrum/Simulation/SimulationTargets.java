package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
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
        return new SimulationTargets(this.primaryTarget.copy(), this.secondaryTargets.stream().map(Target::copy).collect(Collectors.toList()));
    }
}
