package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Weapon.Weapon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Simulation {

    private static final double TIME_STEP = 0.01;

    @Autowired
    private Random random;

    public void runSimulation(SimulationParameters simulationParameters, Weapon weapon, List<Target> targetList) {
        double simulationDuration = simulationParameters.getDuration(); //60s //.01s == 6000
        int timeTicks = (int) (simulationDuration / TIME_STEP);
        /*
        Handle first shot.
         */
        if (weapon.getTriggerType().equals(Weapon.TriggerType.CHARGE)) {
            //handle charge weapon
        } else {
            //handle other types
        }

        /*
        Setup firing mechanism. Load charge time, reload time, fire speed, etc from the weapon.
        On fire, it calls engineHelper.handle fire gun
        Setup metrics tracking.
         */

        for (int i = 1; i < timeTicks; i++) {
            //TODO:Firing mechanism class?
            /*
            boolean fire = firingMechanism.progressTime(TIME_STEP); //maybe return wepaon status instead of boolean? ie reloading, charging, firing for metrics?
           // if returns start_reload, calculate metrics per the reload.
            if(fire) { metrics = engineHelper.fireGun(Weapon, target);}

            List<proc> procsDealingDamageThisTick = target.procs.progressTime(TIME_STEP);
            for(proc : procsDealingDamage)
            {
                proc.apply(target) // store these metrics
            }
             */
        }

    }

}
