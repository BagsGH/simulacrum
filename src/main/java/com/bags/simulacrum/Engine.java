package com.bags.simulacrum;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class Engine {

    @Autowired
    private WeaponModder weaponModder;

    public void start() {
        weaponModder = new WeaponModder();
        Weapon ignisWraith = setupIgnis();
        Weapon ignisWraithModded = setupIgnis();

        System.out.println("===Original weapons===");
        System.out.println(ignisWraith);
        System.out.println(ignisWraithModded);

        //critdmg
        Mod vital = new Mod();
        vital.setCriticalDamageIncrease(1.20);
        Mod hammer = new Mod();
        hammer.setCriticalDamageIncrease(.15);
        ignisWraithModded.addMod(vital);
        ignisWraithModded.addMod(hammer);

        //critchance
        Mod pointStrike = new Mod();
        pointStrike.setCriticalChanceIncrease(1.50);
        Mod criticalDelay = new Mod();
        criticalDelay.setCriticalChanceIncrease(0.08);
        ignisWraithModded.addMod(pointStrike);
        ignisWraithModded.addMod(criticalDelay);

        //attackspeed
        Mod vileAccel = new Mod();
        vileAccel.setFireRateIncrease(0.90);
        Mod vilePrecision = new Mod();
        vilePrecision.setFireRateIncrease(-0.36);
        ignisWraithModded.addMod(vileAccel);
        ignisWraithModded.addMod(vilePrecision);

        //acc
        Mod guidedOrdinace = new Mod();
        guidedOrdinace.setAccuracyIncrease(0.30);
        Mod heavyCalibr = new Mod();
        heavyCalibr.setAccuracyIncrease(-0.55);
        ignisWraithModded.addMod(heavyCalibr);
        ignisWraithModded.addMod(guidedOrdinace);

        modWeapon(ignisWraithModded, ignisWraith);

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
        Map<Weapon.DamageType, Double> ignisDamageTypes = new HashMap<>();
        ignisDamageTypes.put(Weapon.DamageType.HEAT, 35.0);
        ignisWraith.setDamage(ignisDamageTypes);
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

    private void modWeapon(Weapon weaponToMod, Weapon originalWeapon) {
        List<Mod> weaponMods = weaponToMod.getMods();
        System.out.println(weaponMods);

        weaponModder.calculateCriticalChance(weaponToMod, weaponMods);
        weaponModder.calculateCriticalDamage(weaponToMod, weaponMods);
        weaponModder.calculateFireRate(weaponToMod, weaponMods);
        weaponToMod.setAccuracy(weaponModder.calculateAccuracy(weaponToMod.getAccuracy(), weaponMods));
    }


}
