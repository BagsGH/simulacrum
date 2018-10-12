package com.bags.simulacrum;


import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageSourceType;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import com.bags.simulacrum.Simulation.Simulation;
import com.bags.simulacrum.Simulation.SimulationParameters;
import com.bags.simulacrum.Simulation.SimulationTargets;
import com.bags.simulacrum.Weapon.*;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bags.simulacrum.Damage.DamageType.HEAT;
import static com.bags.simulacrum.Damage.DamageType.TOXIN;


@Component
public class Engine {

    private static Logger logger = LoggerFactory.getLogger(Engine.class);

    private final WeaponModifier weaponModifier;

    private final Simulation simulation;

    @Autowired
    public Engine(WeaponModifier weaponModifier, Simulation simulation) {
        this.weaponModifier = weaponModifier;
        this.simulation = simulation;
    }

    public void runSimulation(Weapon weapon, SimulationParameters simulationParameters, List<Target> targetList) {
        simulationParameters.setModdedWeapon(weaponModifier.modWeapon(weapon));
        SimulationTargets simulationTargets = new SimulationTargets(targetList.get(0), targetList.subList(1, targetList.size() - 1)); //TODO: not safe if list only 1 item

        for (int i = 0; i < simulationParameters.getIterations(); i++) {
            simulation.runSimulation(simulationParameters);
            simulationParameters.setModdedWeapon(weaponModifier.modWeapon(weapon)); //TODO: replace this with a copy... but need this to reset the weapon
        }
    }

    public void start() {
        logger.info("Test");


        Weapon ignisWraith = setupIgnis();
//        Weapon opticor = setupOpticor();
//        Weapon lenz = setupLenz();
//        Weapon plasmor = setupPlasmor();

        System.out.println("===Original weapons===");
        System.out.println(ignisWraith);
//        System.out.println(opticor);
//        System.out.println(lenz);
//        System.out.println(plasmor);

        Mod hellFire = new Mod.ModBuilder(null).withDamage(new Damage(HEAT, 0.0, 0.90)).build();
        Mod maligForce = new Mod.ModBuilder(null).withDamage(new Damage(TOXIN, 0.0, 0.60)).withStatusChanceIncrease(0.60).build();
        Mod serration = new Mod.ModBuilder(null).withDamageIncrease(1.65).build();
        Mod heavyCaliber = new Mod.ModBuilder(null).withDamageIncrease(1.65).withAccuracyIncrease(-0.55).build();
        Mod vileAccel = new Mod.ModBuilder(null).withFireRateIncrease(0.90).withDamageIncrease(-0.15).build();
        Mod vitalSense = new Mod.ModBuilder(null).withCriticalDamageIncrease(1.20).build();
        Mod pointStrike = new Mod.ModBuilder(null).withCriticalChanceIncrease(1.50).build();
        Mod splitChamber = new Mod.ModBuilder(null).withMultishotIncrease(0.90).build();

        ignisWraith.setMods(Arrays.asList(heavyCaliber, serration, splitChamber, maligForce, vileAccel, hellFire, vitalSense, pointStrike));
        //ignisWraith.setMods(Arrays.asList(heavyCalibre, serration));

        Mod heatdmg = new Mod();
        heatdmg.setDamage(new Damage(DamageType.HEAT, 0.0, 0.15));

//        opticor.setMods(Arrays.asList(vileAccel, heavyCalibre, vs, ps, serration, heatdmg));

        Mod hellfire = new Mod();
        hellfire.setDamage(new Damage(DamageType.HEAT, 0.0, 0.90));

        Mod ryme = new Mod();
        ryme.setDamage(new Damage(DamageType.COLD, 0, 0.60));

        Mod cryo = new Mod();
        cryo.setDamage(new Damage(DamageType.COLD, 0.0, 0.90));

        //lenz.setMods(Arrays.asList(vileAccel, hellFire, heavyCalibre, splitChamber, serration, cryo, vs, ps));
//        lenz.setMods(Arrays.asList(vileAccel, hellFire, heavyCalibre, splitChamber, serration, cryo, vs, ps));

        Mod blaze = new Mod();
        blaze.setDamage(new Damage(DamageType.HEAT, 0.0, 0.60));
        blaze.setDamageIncrease(0.60);
        Mod charged = new Mod();
        charged.setDamage(new Damage(DamageType.ELECTRICITY, 0.0, 0.90));

        Mod frigid = new Mod();
        frigid.setDamage(new Damage(DamageType.COLD, 0.0, 0.30));
        Mod toxicBarrage = new Mod();
        toxicBarrage.setDamage(new Damage(TOXIN, 0.0, 0.15));


//        plasmor.setMods(Arrays.asList(frigid, charged, blaze, toxicBarrage));

        Weapon ignisWraithModded = weaponModifier.modWeapon(ignisWraith);
        //Weapon opticorModded = weaponModifier.modWeapon(opticor);
//        Weapon lenzModded = weaponModifier.modWeapon(lenz);
        //Weapon plasmorModded = weaponModifier.modWeapon(plasmor);

        System.out.println("===Modded weapons===");
        System.out.println(ignisWraithModded);
        logger.info("End");

        //System.out.println(opticorModded);
//        System.out.println(lenzModded);
        //System.out.println(plasmorModded);
    }

//    private Weapon setupPlasmor() {
//        Weapon plasmor = new Weapon();
//        plasmor.setAccuracy(0.091);
//        plasmor.setCriticalChance(0.220);
//
//        plasmor.setCriticalDamage(1.60);
//        plasmor.setHeadshotMultiplier(1.0);
//        plasmor.setFireRate(1.1);
//        plasmor.setMagazineSize(10);
//        plasmor.setMaxAmmo(48);
//        plasmor.setAccuracyMultiplier(1.0);
//        plasmor.setMultishot(1.00);
//        List<Damage> plasmorDamageSources = new ArrayList<>();
//
//        plasmorDamageSources.add(new Damage(DamageType.RADIATION, 600));
//
//        plasmor.setDamageSources(Arrays.asList(new DamageSource(DamageSourceType.PROJECTILE, plasmorDamageSources)));
//
//        plasmor.setReloadTime(2.8);
//        plasmor.setStatusChance(0.28);
//
//        /* Fluff weapon information. **/
//        plasmor.setNoiseLevel(Weapon.NoiseLevel.ALARMING);
//        plasmor.setTriggerType(Weapon.com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType.SEMIAUTO);
//        plasmor.setSlot(Weapon.Slot.PRIMARY);
//        plasmor.setDamageType(Weapon.WeaponType.SHOTGUN);
//        plasmor.setAmmoType(Weapon.AmmoType.SHOTGUN);
//        plasmor.setDisposition(Weapon.Disposition.NEUTRAL);
//        plasmor.setRangeLimit(20.0);
//        plasmor.setMasteryRank(10);
//        plasmor.setName("Arca Plasmor");
//        plasmor.setMods(new ArrayList<>());
//
//        return plasmor;
//    }

//    private Weapon setupLenz() {
//        Weapon lenz = new Weapon();
//        lenz.setAccuracy(0.167);
//        lenz.setCriticalChance(0.50);
//
//        lenz.setChargeTime(1.20);
//        lenz.setCriticalDamage(2.0);
//        lenz.setHeadshotMultiplier(2.0);
//        lenz.setFireRate(1.0);
//        lenz.setMagazineSize(1);
//        lenz.setMaxAmmo(6);
//        lenz.setAccuracyMultiplier(1.0);
//        lenz.setMultishot(1.00);
//        List<Damage> lenzDamageSources = new ArrayList<>();
//
//        DamageSource shotSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.IMPACT, 50)));
//        DamageSource impactExplosion = new DamageSource(DamageSourceType.HIT_AOE, Arrays.asList(new Damage(DamageType.COLD, 10)), 0, 10.0);
//        DamageSource delayExplosion = new DamageSource(DamageSourceType.DELAYED_AOE, Arrays.asList(new Damage(DamageType.BLAST, 660)), 2.0, 10.0);
//
//        lenz.setDamageSources(Arrays.asList(shotSource, impactExplosion, delayExplosion));
//
//        lenz.setReloadTime(0.60);
//        lenz.setStatusChance(0.05);
//
//        /* Fluff weapon information. **/
//        lenz.setNoiseLevel(Weapon.NoiseLevel.ALARMING);
//        lenz.setTriggerType(Weapon.com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType.CHARGE);
//        lenz.setSlot(Weapon.Slot.PRIMARY);
//        lenz.setDamageType(Weapon.WeaponType.BOW);
//        lenz.setAmmoType(Weapon.AmmoType.BOW);
//        lenz.setDisposition(Weapon.Disposition.NEUTRAL);
//        lenz.setRangeLimit(-1.0);
//        lenz.setMasteryRank(8);
//        lenz.setName("Lenz");
//        lenz.setMods(new ArrayList<>());
//
//        return lenz;
//    }

    private Weapon setupIgnis() {
        Weapon ignisWraith = new Weapon(); //TriggerType triggerType, double reloadTime, int magazineSize, int maxAmmo
        ignisWraith.setFireStateProperties(new FireStateProperties.FireStatePropertiesBuilder(TriggerType.HELD, 1.70, 800, 200)
                .withFireRate(8.0)
                .build());
        /* Important weapon stuff. **/
        ignisWraith.setAccuracy(1.0);
        ignisWraith.setCriticalChance(.17);
        ignisWraith.setCriticalDamage(2.5);
        ignisWraith.setHeadshotMultiplier(2.0);
        ignisWraith.setAccuracyMultiplier(0.0);
        ignisWraith.setMultishot(1.00);
        List<Damage> ignisDamageTypes = new ArrayList<>();
        Damage heat = new Damage(DamageType.HEAT, 35.0);
        ignisDamageTypes.add(heat);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, ignisDamageTypes);
        ignisWraith.setDamageSources(Arrays.asList(damageSource));

        ignisWraith.setStatusChance(.29);

        /* Fluff weapon information. **/
        WeaponInformation weaponInformation = new WeaponInformation(WeaponClass.RIFLE, WeaponSlot.PRIMARY, 9, AmmoType.RIFLE, NoiseLevel.ALARMING, Disposition.MILD);
        ignisWraith.setWeaponInformation(weaponInformation);

        ignisWraith.setTriggerType(TriggerType.HELD);
        ignisWraith.setRangeLimit(27.0);
        ignisWraith.setName("Ignis Wraith");
        ignisWraith.setMods(new ArrayList<>());
        return ignisWraith;
    }

//    private Weapon setupOpticor() {
//        Weapon opticor = new Weapon();
//        opticor.setAccuracy(1.00);
//        opticor.setCriticalChance(0.20);
//
//        opticor.setChargeTime(2.00);
//        opticor.setCriticalDamage(2.5);
//        opticor.setHeadshotMultiplier(2.0);
//        opticor.setFireRate(1.0);
//        opticor.setMagazineSize(5);
//        opticor.setMaxAmmo(200);
//        opticor.setAccuracyMultiplier(1.0);
//        opticor.setMultishot(1.00);
//        List<Damage> opticorDamageTypes = new ArrayList<>();
//        List<Damage> opticorSecondaryDamageTypes = new ArrayList<>();
//
//        opticorDamageTypes.add(new Damage(DamageType.PUNCTURE, 850));
//        opticorDamageTypes.add(new Damage(DamageType.IMPACT, 100));
//        opticorDamageTypes.add(new Damage(DamageType.SLASH, 50));
//        opticorSecondaryDamageTypes.add(new Damage(DamageType.MAGNETIC, 400));
//
//        opticor.setReloadTime(2.0);
//        opticor.setStatusChance(.20);
//
//        /* Fluff weapon information. **/
//        opticor.setNoiseLevel(Weapon.NoiseLevel.ALARMING);
//        opticor.setTriggerType(Weapon.com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType.CHARGE);
//        opticor.setSlot(Weapon.Slot.PRIMARY);
//        opticor.setDamageType(Weapon.WeaponType.RIFLE);
//        opticor.setAmmoType(Weapon.AmmoType.RIFLE);
//        opticor.setDisposition(Weapon.Disposition.STRONG);
//        opticor.setRangeLimit(-1.0);
//        opticor.setMasteryRank(14);
//        opticor.setName("Opticor");
//        opticor.setMods(new ArrayList<>());
//
//
//        return opticor;
//    }

}
