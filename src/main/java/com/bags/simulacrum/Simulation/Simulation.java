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

    private final RandomNumberGenerator randomNumberGenerator;

    private final SimulationHelper simulationHelper;

    private final TargetDamageHelper targetDamageHelper;

    @Autowired
    public Simulation(SimulationConfig config, RandomNumberGenerator randomNumberGenerator, SimulationHelper simulationHelper, TargetDamageHelper targetDamageHelper) {
        this.config = config;
        this.randomNumberGenerator = randomNumberGenerator;
        this.simulationHelper = simulationHelper;
        this.targetDamageHelper = targetDamageHelper;
    }

    @PostConstruct
    public void test() {
        System.out.println("config.getDeltaTime(): " + config.getDeltaTime());
    }

    public SimulationSummary runSimulation(SimulationParameters simulationParameters) {
        Weapon weapon = simulationParameters.getModdedWeapon();
        List<Target> targetList = simulationParameters.getTargetList();

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

            FiringState firingState = weapon.firingStateProgressTime(deltaTime);
            if (firingState instanceof Fired) {
                FiredWeaponSummary firedWeaponSummary = simulationHelper.handleFireWeapon(weapon, target, simulationParameters.getHeadshotChance());
                finalFiredWeaponSummary.addHitPropertiesList(firedWeaponSummary.getHitPropertiesList());
                finalFiredWeaponSummary.addStatusesApplied(firedWeaponSummary.getStatusesApplied());
                finalFiredWeaponSummary.addDamageMetrics(firedWeaponSummary.getDamageMetrics());

                delayedDamageSources.addAll(firedWeaponSummary.getDelayedDamageSources());
            }
            finalWeaponStateMetrics.add(firingState.getClass(), deltaTime);

            List<Status> procsApplying = target.statusProgressTime(deltaTime); //TODO: leave this in charge of target?
            List<DamageMetrics> damageMetricsFromAppliedStatuses = simulationHelper.handleApplyingStatuses(procsApplying, statusTickHitProperties, target);
            for (DamageMetrics individualDamageMetrics : damageMetricsFromAppliedStatuses) {
                finalFiredWeaponSummary.addStatusDamageToHealth(individualDamageMetrics.getDamageToHealth());
                finalFiredWeaponSummary.addStatusDamageToShields(individualDamageMetrics.getDamageToShields());
            }
            target.getStatuses().removeIf(Status::finished);
        }
        SimulationSummary simulationSummary = new SimulationSummary();
        simulationSummary.setWeaponStateMetrics(finalWeaponStateMetrics);
        simulationSummary.setFiredWeaponSummary(finalFiredWeaponSummary);

        return simulationSummary;
    }
}
