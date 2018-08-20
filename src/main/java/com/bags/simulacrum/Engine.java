package com.bags.simulacrum;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Engine {

    @Autowired
    private WeaponModifier weaponModifier;

    public void start() {
        weaponModifier = new WeaponModifier();
        Weapon ignisWraith = setupIgnis();

        System.out.println("===Original weapons===");
        System.out.println(ignisWraith);

        Mod vitalSense = new Mod();
        vitalSense.setName("Vital Sense");
        vitalSense.setCriticalDamageIncrease(1.20);
        ignisWraith.addMod(vitalSense);

        Mod pointStrike = new Mod();
        pointStrike.setName("Point Strike");
        pointStrike.setCriticalChanceIncrease(1.50);
        ignisWraith.addMod(pointStrike);

        Mod vileAccel = new Mod();
        vileAccel.setFireRateIncrease(0.90);
        vileAccel.setAccuracyIncrease(-0.50);
        ignisWraith.addMod(vileAccel);

        Mod malignantForce = new Mod();
        malignantForce.setName("Malignant Force");
        ignisWraith.addMod(malignantForce);


        Weapon ignisWraithModded = weaponModifier.modifyWeapon(ignisWraith);

        System.out.println("===Modded weapons===");
        System.out.println(ignisWraithModded);
    }

    private Weapon setupIgnis() {
        Weapon ignisWraith = new Weapon();
        /* Important weapon stuff. **/
        ignisWraith.setAccuracy(1.0);
        ignisWraith.setCriticalChance(.17);
        ignisWraith.setCriticalDamage(2.5);
        ignisWraith.setHeadshotMultiplier(2.0);
        ignisWraith.setFireRate(8.0);
        ignisWraith.setMagazineSize(200);
        ignisWraith.setMaxAmmo(800);
        ignisWraith.setAccuracyMultiplier(0.0);
        List<Damage> ignisDamageTypes = new ArrayList<>();
        Damage heat = new Damage();
        heat.setValue(35);
        heat.setType(Damage.DamageType.HEAT);
        ignisDamageTypes.add(heat);

        ignisWraith.setDamageTypes(ignisDamageTypes);
        ignisWraith.setReloadTime(1.7);
        ignisWraith.setStatusChance(.29);

        /* Fluff weapon information. **/
        ignisWraith.setNoiseLevel(Weapon.NoiseLevel.ALARMING);
        ignisWraith.setTriggerType(Weapon.TriggerType.HELD);
        ignisWraith.setSlot(Weapon.Slot.PRIMARY);
        ignisWraith.setType(Weapon.WeaponType.RIFLE);
        ignisWraith.setAmmoType(Weapon.AmmoType.RIFLE);
        ignisWraith.setDisposition(Weapon.Disposition.MILD);
        ignisWraith.setRangeLimit(27.0);
        ignisWraith.setMasteryRank(9);
        ignisWraith.setName("Ignis Wraith");
        ignisWraith.setMods(new ArrayList<>());
        return ignisWraith;
    }

}
