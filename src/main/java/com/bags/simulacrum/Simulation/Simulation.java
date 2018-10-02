package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Configuration.SimulationConfig;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Status.Status;
import com.bags.simulacrum.Weapon.State.Fired;
import com.bags.simulacrum.Weapon.State.FiringState;
import com.bags.simulacrum.Weapon.Weapon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class Simulation {

    private final SimulationConfig config;

    private final RandomNumberGenerator randomNumberGenerator;

    private final SimulationHelper simulationHelper;

    @Autowired
    public Simulation(SimulationConfig config, RandomNumberGenerator randomNumberGenerator, SimulationHelper simulationHelper) {
        this.config = config;
        this.randomNumberGenerator = randomNumberGenerator;
        this.simulationHelper = simulationHelper;
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


        weapon.initializeFiringState();
        for (int i = 1; i < timeTicks; i++) {

            FiringState firingState = weapon.firingStateProgressTime(deltaTime);
            if (firingState instanceof Fired) {
                FiredWeaponMetrics firedWeaponMetrics = simulationHelper.handleFireWeapon(weapon, targetList.get(0), simulationParameters.getHeadshotChance());
            }

            finalWeaponStateMetrics.add(firingState.getClass(), deltaTime);

            List<Status> procsApplying = targetList.get(0).statusProgressTime(deltaTime);
            for (Status status : procsApplying) {
                status.apply(targetList.get(0)); //TODO: apply should return metrics
            }
            // Handle delayed damage sources
        }

    }
}
