package com.bags.simulacrum;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Engine  {

    public void start() {
        Weapon ignisWraith = setupIgnis();

        System.out.println(ignisWraith);
    }

    private Weapon setupIgnis() {
        Weapon ignisWraith = new Weapon();
        /* Important weapon stuff. **/
        ignisWraith.setAccuracy(100.0);
        ignisWraith.setCriticalChance(17.0);
        ignisWraith.setCriticalMultiplier(2.5);
        ignisWraith.setHeadshotMultiplier(2.0);
        ignisWraith.setFireRate(8.0);
        ignisWraith.setMagazineSize(200);
        ignisWraith.setMaxAmmo(800);
        ignisWraith.setAccuracyMultiplier(0.0);
        Map<Weapon.DamageType, Double> ignisDamageTypes = new HashMap();
        ignisDamageTypes.put(Weapon.DamageType.HEAT, 35.0);
        ignisWraith.setDamage(ignisDamageTypes);

        /* Fluff weapon information. **/
        ignisWraith.setNoiseLevel(Weapon.NoiseLevel.ALARMING);
        ignisWraith.setReloadTime(1.7);
        ignisWraith.setStatusChance(29.0);
        ignisWraith.setTriggerType(Weapon.TriggerType.HELD);
        ignisWraith.setSlot(Weapon.Slot.PRIMARY);
        ignisWraith.setType(Weapon.WeaponType.RIFLE);
        ignisWraith.setAmmoType(Weapon.AmmoType.RIFLE);
        ignisWraith.setDisposition(Weapon.Disposition.MILD);
        ignisWraith.setRangeLimit(27.0);
        ignisWraith.setMasteryRank(9);
        ignisWraith.setName("Ignis Wraith");
        return ignisWraith;
    }
}
