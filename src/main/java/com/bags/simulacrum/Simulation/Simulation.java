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
        List<Target> targetList = simulationParameters.getTargetList();
        SimulationSummary simulationSummary = new SimulationSummary();

        double deltaTime = config.getDeltaTime();
        double simulationDuration = simulationParameters.getDuration(); //60s //.01s == 6000
        int timeTicks = (int) (simulationDuration / deltaTime);

        WeaponStateMetrics finalWeaponStateMetrics = new WeaponStateMetrics();
        FiredWeaponSummary finalFiredWeaponSummary = new FiredWeaponSummary().getEmptySummary();

        HitProperties statusTickHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);

        weapon.initializeFiringState();
        Target target = targetList.get(0);
        List<DelayedDamageSource> delayedDamageSources = new ArrayList<>();
        for (int i = 0; i < timeTicks; i++) {
            if (delayedDamageSources.size() > 0) {
                delayedDamageSources.forEach(ds -> ds.progressTime(deltaTime));
                FiredWeaponSummary summaryFromHandlingDelayedDamageSources = simulationHelper.handleDelayedDamageSources(delayedDamageSources, target, weapon.getStatusChance());
                finalFiredWeaponSummary.addHitPropertiesList(summaryFromHandlingDelayedDamageSources.getHitPropertiesList());
                finalFiredWeaponSummary.addStatusesApplied(summaryFromHandlingDelayedDamageSources.getStatusesApplied());
                finalFiredWeaponSummary.addDamageMetrics(summaryFromHandlingDelayedDamageSources.getDamageMetrics());
                delayedDamageSources.removeIf(DelayedDamageSource::delayOver);
            }

            List<Status> procsApplying = target.statusProgressTime(deltaTime); //TODO: leave this in charge of target?
            List<DamageMetrics> damageMetricsFromAppliedStatuses = simulationHelper.handleApplyingStatuses(procsApplying, statusTickHitProperties, target);
            for (DamageMetrics individualDamageMetrics : damageMetricsFromAppliedStatuses) {
                finalFiredWeaponSummary.addStatusDamageToHealth(individualDamageMetrics.getDamageToHealth());
                finalFiredWeaponSummary.addStatusDamageToShields(individualDamageMetrics.getDamageToShields());
            }
            target.getStatuses().removeIf(Status::finished);

            FiringState firingState = weapon.firingStateProgressTime(deltaTime);
            if (firingState instanceof Fired) {
                FiredWeaponSummary firedWeaponSummary = simulationHelper.handleFireWeapon(weapon, target, simulationParameters.getHeadshotChance());
                finalFiredWeaponSummary.addHitPropertiesList(firedWeaponSummary.getHitPropertiesList());
                finalFiredWeaponSummary.addStatusesApplied(firedWeaponSummary.getStatusesApplied());
                finalFiredWeaponSummary.addDamageMetrics(firedWeaponSummary.getDamageMetrics());

                delayedDamageSources.addAll(firedWeaponSummary.getDelayedDamageSources());
            }
            finalWeaponStateMetrics.add(firingState.getClass(), deltaTime);
            if (target.isDead()) {
                simulationSummary.addKilledTarget(target);
                targetList.removeIf(Target::isDead);
                if (simulationParameters.isReplaceDeadTargets()) {
                    targetList.add(new Target()); //TODO: copy target implementation
                }
            }
        }
        simulationSummary.setWeaponStateMetrics(finalWeaponStateMetrics);
        simulationSummary.setFiredWeaponSummary(finalFiredWeaponSummary);

        return simulationSummary;
    }
}
