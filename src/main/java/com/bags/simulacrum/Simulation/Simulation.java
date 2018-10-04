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

    public void runSimulation(SimulationParameters simulationParameters) {
        Weapon weapon = simulationParameters.getModdedWeapon();
        List<Target> targetList = simulationParameters.getTargetList();

        double deltaTime = config.getDeltaTime();
        double simulationDuration = simulationParameters.getDuration(); //60s //.01s == 6000
        int timeTicks = (int) (simulationDuration / deltaTime);

        WeaponStateMetrics finalWeaponStateMetrics = new WeaponStateMetrics();
        FiredWeaponMetrics finalFiredWeaponMetrics = new FiredWeaponMetrics();

        HitProperties statusTickHitProperties = new HitProperties(0, 0.0, 0.0, 0.0);

        weapon.initializeFiringState();
        Target target = targetList.get(0);
        List<DelayedDamageSource> delayedDamageSources = new ArrayList<>();
        for (int i = 0; i < timeTicks; i++) {
            /*
             * Handle delayed damages.
             */
            for (DelayedDamageSource delayedDamageSource : delayedDamageSources) {
                delayedDamageSource.progressTime(deltaTime);
                if (delayedDamageSource.delayOver()) {
                    DamageMetrics damageMetricsFromDelayedDamage = targetDamageHelper.applyDamageSourceDamageToTarget(delayedDamageSource.getDamageSource(), delayedDamageSource.getHitProperties(), target);
                }
            }
            delayedDamageSources.removeIf(DelayedDamageSource::delayOver);

            /*
             * Handle fired state.
             */
            FiringState firingState = weapon.firingStateProgressTime(deltaTime);
            if (firingState instanceof Fired) {
                FiredWeaponMetrics firedWeaponMetrics = simulationHelper.handleFireWeapon(weapon, target, simulationParameters.getHeadshotChance());
                delayedDamageSources.addAll(firedWeaponMetrics.getDelayedDamageSources());
            }

            finalWeaponStateMetrics.add(firingState.getClass(), deltaTime);

            /*
             * Handle statuses over time.
             */
            List<Status> procsApplying = target.statusProgressTime(deltaTime);
            for (Status status : procsApplying) {
                DamageMetrics damageMetricsFromStatusTick = targetDamageHelper.applyDamageSourceDamageToTarget(status.apply(target), statusTickHitProperties, target);
            }
            target.getStatuses().removeIf(Status::finished);
        }

    }
}
