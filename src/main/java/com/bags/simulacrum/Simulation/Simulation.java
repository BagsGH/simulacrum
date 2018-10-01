package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Configuration.SimulationConfig;
import com.bags.simulacrum.Entity.Target;
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

    private final EngineHelper engineHelper;

    @Autowired
    public Simulation(SimulationConfig config, RandomNumberGenerator randomNumberGenerator, EngineHelper engineHelper) {
        this.config = config;
        this.randomNumberGenerator = randomNumberGenerator;
        this.engineHelper = engineHelper;
    }

    @PostConstruct
    public void test() {
        System.out.println("config.getDeltaTime(): " + config.getDeltaTime());
    }

    public void runSimulation(SimulationParameters simulationParameters, Weapon weapon, List<Target> targetList) {
        double deltaTime = config.getDeltaTime();
        double simulationDuration = simulationParameters.getDuration(); //60s //.01s == 6000
        int timeTicks = (int) (simulationDuration / deltaTime);
        /*
        Setup firing mechanism. Load charge time, reload time, fire speed, etc from the weapon.
        On fire, it calls engineHelper.handle fire gun
        Setup metrics tracking.
         */

        weapon.initializeWeaponStatus();

        WeaponStateMetrics weaponStateMetrics = new WeaponStateMetrics();

        for (int i = 1; i < timeTicks; i++) {

            FiringState firingState = weapon.getWeaponState().progressTime(deltaTime);
            if (firingState instanceof Fired) {
                FiredWeaponMetrics firedWeaponMetrics = engineHelper.handleFireWeapon(weapon, targetList.get(0), simulationParameters.getHeadshotChance());
            }

            weaponStateMetrics.add(firingState.getClass(), deltaTime);

            /*

            List<proc> procsDealingDamageThisTick = target.procs.progressTime(DELTA_TIME);
            for(proc : procsDealingDamage)
            {
                metrics = proc.apply(target) // store these metrics
            }
             */
        }

    }
}
