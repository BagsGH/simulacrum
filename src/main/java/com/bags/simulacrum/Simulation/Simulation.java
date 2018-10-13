package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Configuration.SimulationConfig;
import com.bags.simulacrum.Damage.DelayedDamageSource;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Status.Status;
import com.bags.simulacrum.Weapon.State.Fired;
import com.bags.simulacrum.Weapon.State.FiringState;
import com.bags.simulacrum.Weapon.Weapon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Simulation {

    private final SimulationConfig config;

    private final SimulationHelper simulationHelper;


    @Autowired
    public Simulation(SimulationConfig config, SimulationHelper simulationHelper) {
        this.config = config;
        this.simulationHelper = simulationHelper;
    }

    @PostConstruct
    public void test() {
        System.out.println("config.getDeltaTime(): " + config.getDeltaTime());
    }

    public SimulationSummary runSimulation(SimulationParameters simulationParameters) {
        Weapon weapon = simulationParameters.getModdedWeapon();
        SimulationTargets targets = simulationParameters.getSimulationTargets();
        double headshotChance = simulationParameters.getHeadshotChance();
        boolean replaceDeadTargets = simulationParameters.isReplaceDeadTargets();
        double simulationDuration = simulationParameters.getDuration(); //60s //.01s == 6000

        double deltaTime = config.getDeltaTime();
        int timeTicks = (int) (simulationDuration / deltaTime);

        SimulationSummary simulationSummary = new SimulationSummary();
        WeaponStateMetrics finalWeaponStateMetrics = new WeaponStateMetrics();
        FiredWeaponSummary finalFiredWeaponSummary = new FiredWeaponSummary();

        weapon.initializeFiringState();

        SimulationTargets cloningVat = new SimulationTargets(targets.getPrimaryTarget().copy(), targets.getSecondaryTargets().stream().map(Target::copy).collect(Collectors.toList()));

        List<DelayedDamageSource> delayedDamageSources = new ArrayList<>();
        for (int i = 0; i < timeTicks; i++) {
            if (delayedDamageSources.size() > 0) {
                delayedDamageSources.forEach(ds -> ds.progressTime(deltaTime));
                FiredWeaponSummary summaryFromHandlingDelayedDamageSources = simulationHelper.handleDelayedDamageSources(delayedDamageSources, weapon.getStatusChance());
                finalFiredWeaponSummary.addHitPropertiesMap(summaryFromHandlingDelayedDamageSources.getHitPropertiesListMap());
                finalFiredWeaponSummary.addStatusesAppliedMap(summaryFromHandlingDelayedDamageSources.getStatusesAppliedMap());
                finalFiredWeaponSummary.addDamageMetrics(summaryFromHandlingDelayedDamageSources.getDamageMetricsMap());
                delayedDamageSources.removeIf(DelayedDamageSource::delayOver);
            }

            progressStatus(targets, deltaTime);
            FiredWeaponSummary statusApplicationSummary = simulationHelper.handleApplyingStatuses(targets.getAllTargets());
            finalFiredWeaponSummary.addDamageMetrics(statusApplicationSummary.getDamageMetricsMap());
            targets.getAllTargets().forEach(target -> target.getStatuses().removeIf(Status::finished));

            FiringState firingState = weapon.firingStateProgressTime(deltaTime);
            if (firingState instanceof Fired) {
                FiredWeaponSummary firedWeaponSummary = simulationHelper.handleFireWeapon(weapon, targets, headshotChance);
                finalFiredWeaponSummary.addHitPropertiesMap(firedWeaponSummary.getHitPropertiesListMap());
                finalFiredWeaponSummary.addStatusesAppliedMap(firedWeaponSummary.getStatusesAppliedMap());
                finalFiredWeaponSummary.addDamageMetrics(firedWeaponSummary.getDamageMetricsMap());
                delayedDamageSources.addAll(firedWeaponSummary.getDelayedDamageSources());
            }
            finalWeaponStateMetrics.add(firingState.getClass(), deltaTime);

            handleDeadTargets(targets, cloningVat, simulationSummary, replaceDeadTargets);
        }
        simulationSummary.setWeaponStateMetrics(finalWeaponStateMetrics);
        simulationSummary.setFiredWeaponSummary(finalFiredWeaponSummary);

        return simulationSummary;
    }

    private void progressStatus(SimulationTargets simulationTargets, double deltaTime) {
        for (Target individualTarget : simulationTargets.getAllTargets()) {
            List<Status> statuses = individualTarget.getStatuses();
            statuses.forEach(status -> status.progressTime(deltaTime));
        }
    }

    private void handleDeadTargets(SimulationTargets targets, SimulationTargets cloningVat, SimulationSummary simulationSummary, boolean replaceDeadTargets) {
        if (targets.getAllTargets().stream().anyMatch(Target::isDead)) {
            if (targets.getPrimaryTarget().isDead()) {
                simulationSummary.addKilledTarget(targets.getPrimaryTarget());
                if (replaceDeadTargets) {
                    targets.setPrimaryTarget(cloningVat.getPrimaryTarget().copy());
                } else {
                    targets.setPrimaryTarget(null);
                }
            }
            List<Target> secondaryTargets = targets.getSecondaryTargets();
            List<Target> newSecondaryTargetList = new ArrayList<>();
            for (Target secondaryTarget : secondaryTargets) {
                if (secondaryTarget.isDead()) {
                    simulationSummary.addKilledTarget(secondaryTarget);
                    if (replaceDeadTargets) {
                        cloningVat.getSecondaryTargetById(secondaryTarget.getTargetId()).ifPresent(target -> newSecondaryTargetList.add(target.copy()));
                    }
                } else {
                    newSecondaryTargetList.add(secondaryTarget);
                }
            }
            targets.setSecondaryTargets(newSecondaryTargetList);
        }
    }
}
