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
        SimulationTargets targets = simulationParameters.getSimulationTargets(); //TODO: dont use target list, maybe a local Targets obj
        SimulationSummary simulationSummary = new SimulationSummary();

        double deltaTime = config.getDeltaTime();
        double simulationDuration = simulationParameters.getDuration(); //60s //.01s == 6000
        int timeTicks = (int) (simulationDuration / deltaTime);

        WeaponStateMetrics finalWeaponStateMetrics = new WeaponStateMetrics();
        FiredWeaponSummary finalFiredWeaponSummary = new FiredWeaponSummary();

        weapon.initializeFiringState();

        //TODO: pass in list to the calls below, and if the DamageSource is AOE, hit all, else... first?
        Target targetCopy = simulationParameters.getSimulationTargets().getPrimaryTarget().copy();//TODO: handle for multiple targets
        List<Target> copiedSecondaryTargets = simulationParameters.getSimulationTargets().getSecondaryTargets().stream().map(Target::copy).collect(Collectors.toList());
        SimulationTargets cloningVat = new SimulationTargets(targetCopy, copiedSecondaryTargets);

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

            //if ((int) simulationParameters.getSimulationTargets().getAllTargets().stream().anyMatch(t -> t.getStatuses().size() > 0)) {
            progressStatus(simulationParameters.getSimulationTargets(), deltaTime);
            FiredWeaponSummary statusApplicationSummary = simulationHelper.handleApplyingStatuses(simulationParameters.getSimulationTargets().getAllTargets());
            finalFiredWeaponSummary.addDamageMetrics(statusApplicationSummary.getDamageMetricsMap());
            simulationParameters.getSimulationTargets().getAllTargets().forEach(target -> target.getStatuses().removeIf(Status::finished));
            //}

            FiringState firingState = weapon.firingStateProgressTime(deltaTime);
            if (firingState instanceof Fired) {
                FiredWeaponSummary firedWeaponSummary = simulationHelper.handleFireWeapon(weapon, simulationParameters.getSimulationTargets(), simulationParameters.getHeadshotChance());
                finalFiredWeaponSummary.addHitPropertiesMap(firedWeaponSummary.getHitPropertiesListMap());
                finalFiredWeaponSummary.addStatusesAppliedMap(firedWeaponSummary.getStatusesAppliedMap());
                finalFiredWeaponSummary.addDamageMetrics(firedWeaponSummary.getDamageMetricsMap());
                delayedDamageSources.addAll(firedWeaponSummary.getDelayedDamageSources());
            }
            finalWeaponStateMetrics.add(firingState.getClass(), deltaTime);

            if (targets.getAllTargets().stream().anyMatch(Target::isDead)) {
                simulationSummary.addKilledTarget(simulationParameters.getSimulationTargets().getPrimaryTarget());
                targetList.removeIf(Target::isDead);
                if (simulationParameters.isReplaceDeadTargets()) {
                    targetList.add(cloningVat.getPrimaryTarget().copy()); //TODO: fix this to work with getSimulationTargets
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
