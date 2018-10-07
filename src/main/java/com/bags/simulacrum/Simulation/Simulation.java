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
        List<Target> targetList = simulationParameters.getSimulationTargets().getAllTargets();
        SimulationSummary simulationSummary = new SimulationSummary();

        double deltaTime = config.getDeltaTime();
        double simulationDuration = simulationParameters.getDuration(); //60s //.01s == 6000
        int timeTicks = (int) (simulationDuration / deltaTime);

        WeaponStateMetrics finalWeaponStateMetrics = new WeaponStateMetrics();
        FiredWeaponSummary finalFiredWeaponSummary = new FiredWeaponSummary();

        weapon.initializeFiringState();
        Target primaryTarget = simulationParameters.getSimulationTargets().getPrimaryTarget(); //TODO: pass in list to the calls below, and if the DamageSource is AOE, hit all, else... first?
        primaryTarget.setTargetName("primary");
        Target targetCopy = primaryTarget.copy();//TODO: handle for multiple targets
        List<DelayedDamageSource> delayedDamageSources = new ArrayList<>();
        for (int i = 0; i < timeTicks; i++) {
            if (delayedDamageSources.size() > 0) {
                delayedDamageSources.forEach(ds -> ds.progressTime(deltaTime));
                FiredWeaponSummary summaryFromHandlingDelayedDamageSources = simulationHelper.handleDelayedDamageSources(delayedDamageSources, weapon.getStatusChance());
                finalFiredWeaponSummary.addHitPropertiesList(summaryFromHandlingDelayedDamageSources.getHitPropertiesListMap());
                finalFiredWeaponSummary.addStatusesApplied(summaryFromHandlingDelayedDamageSources.getStatusesAppliedMap());
                finalFiredWeaponSummary.addDamageMetrics(summaryFromHandlingDelayedDamageSources.getDamageMetricsMap());
                delayedDamageSources.removeIf(DelayedDamageSource::delayOver);
            }

            progressStatus(simulationParameters.getSimulationTargets(), deltaTime);
            FiredWeaponSummary statusApplicationSummary = simulationHelper.handleApplyingStatuses(simulationParameters.getSimulationTargets().getAllTargets());
            finalFiredWeaponSummary.addDamageMetrics(statusApplicationSummary.getDamageMetricsMap());
            primaryTarget.getStatuses().removeIf(Status::finished);

            FiringState firingState = weapon.firingStateProgressTime(deltaTime);
            if (firingState instanceof Fired) {
                FiredWeaponSummary firedWeaponSummary = simulationHelper.handleFireWeapon(weapon, simulationParameters.getSimulationTargets(), simulationParameters.getHeadshotChance());
                finalFiredWeaponSummary.addHitPropertiesList(firedWeaponSummary.getHitPropertiesListMap());
                finalFiredWeaponSummary.addStatusesApplied(firedWeaponSummary.getStatusesAppliedMap());
                finalFiredWeaponSummary.addDamageMetrics(firedWeaponSummary.getDamageMetricsMap());
                delayedDamageSources.addAll(firedWeaponSummary.getDelayedDamageSources());
            }
            finalWeaponStateMetrics.add(firingState.getClass(), deltaTime);

            if (simulationParameters.getSimulationTargets().getPrimaryTarget().isDead()) {
                simulationSummary.addKilledTarget(simulationParameters.getSimulationTargets().getPrimaryTarget());
                targetList.removeIf(Target::isDead);
                if (simulationParameters.isReplaceDeadTargets()) {
                    targetList.add(new Target()); //TODO: fix this to work with getSimulationTargets
                    targetList.add(targetCopy.copy());
                }
            }
//           //TODO: handle death of secondary targets
//            if (primaryTarget.isDead()) {
//                simulationSummary.addKilledTarget(primaryTarget);
//                targetList.removeIf(Target::isDead);
//                if (simulationParameters.isReplaceDeadTargets()) {
//                    targetList.add(new Target());
//                    targetList.add(targetCopy.copy());
//                }
//            }
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

    private List<Status> getProcsApplyingToTarget(Target target, double deltaTime) {
        List<Status> procsApplying = new ArrayList<>();
        target.getStatuses().forEach(status -> {
            status.progressTime(deltaTime);
            if (status.checkProgress()) {
                procsApplying.add(status);
            }
        });
        return procsApplying;
    }
}
