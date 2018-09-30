package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Configuration.SimulationConfig;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Weapon.Status.Fired;
import com.bags.simulacrum.Weapon.Status.FiringStatus;
import com.bags.simulacrum.Weapon.TriggerType;
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
        double simulationDuration = simulationParameters.getDuration(); //60s //.01s == 6000
        int timeTicks = (int) (simulationDuration / config.getDeltaTime());
        /*
        Handle first shot.
         */
        if (weapon.getTriggerType().equals(TriggerType.CHARGE)) {
            //handle charge weapon
        } else {
            //handle other types
        }

        /*
        Setup firing mechanism. Load charge time, reload time, fire speed, etc from the weapon.
        On fire, it calls engineHelper.handle fire gun
        Setup metrics tracking.
         */

        weapon.resetWeaponStatus();

        for (int i = 1; i < timeTicks; i++) {
            FiringStatus firingStatus = weapon.getWeaponStatus().progressTime(config.getDeltaTime());
            if (firingStatus instanceof Fired) {
                engineHelper.handleFireWeapon(weapon, targetList.get(0), simulationParameters.getHeadshotChance());
            }
            //TODO:Firing mechanism class?
            /*
            boolean fire = firingMechanism.progressTime(DELTA_TIME); //maybe return wepaon status instead of boolean? ie reloading, charging, firing for metrics?
           // if returns start_reload, calculate metrics per the reload.
            if(fire) { metrics = engineHelper.fireGun(Weapon, target);}

            List<proc> procsDealingDamageThisTick = target.procs.progressTime(DELTA_TIME);
            for(proc : procsDealingDamage)
            {
                proc.apply(target) // store these metrics
            }
             */
        }

    }

}
