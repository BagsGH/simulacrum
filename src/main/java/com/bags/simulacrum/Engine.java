package com.bags.simulacrum;


import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageSourceType;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Weapon.Mod;
import com.bags.simulacrum.Weapon.Weapon;
import com.bags.simulacrum.Weapon.WeaponModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Engine {

    private final WeaponModifier weaponModifier;

    @Autowired
    public Engine(WeaponModifier weaponModifier) {
        this.weaponModifier = weaponModifier;
    }

    public void start() {
        Weapon ignisWraith = setupIgnis();
        Weapon opticor = setupOpticor();
        Weapon lenz = setupLenz();
        Weapon plasmor = setupPlasmor();

        System.out.println("===Original weapons===");
        System.out.println(ignisWraith);
        System.out.println(opticor);
        System.out.println(lenz);
        System.out.println(plasmor);

        Mod heavyCalibre = new Mod();
        heavyCalibre.setDamageIncrease(1.65);
        heavyCalibre.setAccuracyIncrease(-0.55);

        Mod serration = new Mod();
        serration.setDamageIncrease(1.65);

        Mod splitChamber = new Mod();
        splitChamber.setMultishotIncrease(0.90);

        Mod maligForce = new Mod();
        maligForce.setStatusChanceIncrease(0.60);
        Damage toxin = new Damage(DamageType.TOXIN);
        toxin.setModAddedDamageRatio(0.60);
        maligForce.setDamage(toxin);

        Mod vileAccel = new Mod();
        vileAccel.setFireRateIncrease(0.90);
        vileAccel.setDamageIncrease(-0.15);

        Mod hellFire = new Mod();
        Damage heat = new Damage(DamageType.HEAT);
        heat.setModAddedDamageRatio(0.90);
        hellFire.setDamage(heat);

        Mod vs = new Mod();
        vs.setCriticalChanceIncrease(1.50);

        Mod ps = new Mod();
        ps.setCriticalDamageIncrease(1.20);

        ignisWraith.setMods(Arrays.asList(heavyCalibre, serration, splitChamber, maligForce, vileAccel, hellFire, vs, ps));
        //ignisWraith.setMods(Arrays.asList(heavyCalibre, serration));

        Mod heatdmg = new Mod();
        heatdmg.setDamage(new Damage(DamageType.HEAT, 0.0, 0.15));

        opticor.setMods(Arrays.asList(vileAccel, heavyCalibre, vs, ps, serration, heatdmg));

        Mod hellfire = new Mod();
        hellfire.setDamage(new Damage(DamageType.HEAT, 0.0, 0.90));

        Mod ryme = new Mod();
        ryme.setDamage(new Damage(DamageType.COLD, 0, 0.60));

        Mod cryo = new Mod();
        cryo.setDamage(new Damage(DamageType.COLD, 0.0, 0.90));

        //lenz.setMods(Arrays.asList(vileAccel, hellFire, heavyCalibre, splitChamber, serration, cryo, vs, ps));
        lenz.setMods(Arrays.asList(vileAccel, hellFire, heavyCalibre, splitChamber, serration, cryo, vs, ps));

        Mod blaze = new Mod();
        blaze.setDamage(new Damage(DamageType.HEAT, 0.0, 0.60));
        blaze.setDamageIncrease(0.60);
        Mod charged = new Mod();
        charged.setDamage(new Damage(DamageType.ELECTRICITY, 0.0, 0.90));

        Mod frigid = new Mod();
        frigid.setDamage(new Damage(DamageType.COLD, 0.0, 0.30));
        Mod toxicBarrage = new Mod();
        toxicBarrage.setDamage(new Damage(DamageType.TOXIN, 0.0, 0.15));


        plasmor.setMods(Arrays.asList(frigid, charged, blaze, toxicBarrage));

        Weapon ignisWraithModded = weaponModifier.modWeapon(ignisWraith);
        //Weapon opticorModded = weaponModifier.modWeapon(opticor);
        Weapon lenzModded = weaponModifier.modWeapon(lenz);
        //Weapon plasmorModded = weaponModifier.modWeapon(plasmor);

        System.out.println("===Modded weapons===");
        System.out.println(ignisWraithModded);
        //System.out.println(opticorModded);
        System.out.println(lenzModded);
        //System.out.println(plasmorModded);
    }

    private Weapon setupPlasmor() {
        Weapon plasmor = new Weapon();
        plasmor.setAccuracy(0.091);
        plasmor.setCriticalChance(0.220);

        plasmor.setCriticalDamage(1.60);
        plasmor.setHeadshotMultiplier(1.0);
        plasmor.setFireRate(1.1);
        plasmor.setMagazineSize(10);
        plasmor.setMaxAmmo(48);
        plasmor.setAccuracyMultiplier(1.0);
        plasmor.setMultishot(1.00);
        List<Damage> plasmorDamageSources = new ArrayList<>();

        plasmorDamageSources.add(new Damage(DamageType.RADIATION, 600));

        plasmor.setDamageSources(Arrays.asList(new DamageSource(DamageSourceType.PROJECTILE, plasmorDamageSources)));

        plasmor.setReloadTime(2.8);
        plasmor.setStatusChance(0.28);

        /* Fluff weapon information. **/
        plasmor.setNoiseLevel(Weapon.NoiseLevel.ALARMING);
        plasmor.setTriggerType(Weapon.TriggerType.SEMIAUTO);
        plasmor.setSlot(Weapon.Slot.PRIMARY);
        plasmor.setType(Weapon.WeaponType.SHOTGUN);
        plasmor.setAmmoType(Weapon.AmmoType.SHOTGUN);
        plasmor.setDisposition(Weapon.Disposition.NEUTRAL);
        plasmor.setRangeLimit(20.0);
        plasmor.setMasteryRank(10);
        plasmor.setName("Arca Plasmor");
        plasmor.setMods(new ArrayList<>());

        return plasmor;
    }

    private Weapon setupLenz() {
        Weapon lenz = new Weapon();
        lenz.setAccuracy(0.167);
        lenz.setCriticalChance(0.50);

        lenz.setChargeTime(1.20);
        lenz.setCriticalDamage(2.0);
        lenz.setHeadshotMultiplier(2.0);
        lenz.setFireRate(1.0);
        lenz.setMagazineSize(1);
        lenz.setMaxAmmo(6);
        lenz.setAccuracyMultiplier(1.0);
        lenz.setMultishot(1.00);
        List<Damage> lenzDamageSources = new ArrayList<>();

        DamageSource shotSource = new DamageSource(DamageSourceType.PROJECTILE, Arrays.asList(new Damage(DamageType.IMPACT, 50)));
        DamageSource impactExplosion = new DamageSource(DamageSourceType.HIT_AOE, Arrays.asList(new Damage(DamageType.COLD, 10)), 0, 10.0);
        DamageSource delayExplosion = new DamageSource(DamageSourceType.DELAYED_AOE, Arrays.asList(new Damage(DamageType.BLAST, 660)), 2.0, 10.0);

        lenz.setDamageSources(Arrays.asList(shotSource, impactExplosion, delayExplosion));

        lenz.setReloadTime(0.60);
        lenz.setStatusChance(0.05);

        /* Fluff weapon information. **/
        lenz.setNoiseLevel(Weapon.NoiseLevel.ALARMING);
        lenz.setTriggerType(Weapon.TriggerType.CHARGE);
        lenz.setSlot(Weapon.Slot.PRIMARY);
        lenz.setType(Weapon.WeaponType.BOW);
        lenz.setAmmoType(Weapon.AmmoType.BOW);
        lenz.setDisposition(Weapon.Disposition.NEUTRAL);
        lenz.setRangeLimit(-1.0);
        lenz.setMasteryRank(8);
        lenz.setName("Lenz");
        lenz.setMods(new ArrayList<>());

        return lenz;
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
        ignisWraith.setMultishot(1.00);
        List<Damage> ignisDamageTypes = new ArrayList<>();
        Damage heat = new Damage(DamageType.HEAT, 35.0);
        ignisDamageTypes.add(heat);
        DamageSource damageSource = new DamageSource(DamageSourceType.PROJECTILE, ignisDamageTypes);
        ignisWraith.setDamageSources(Arrays.asList(damageSource));

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

    private Weapon setupOpticor() {
        Weapon opticor = new Weapon();
        opticor.setAccuracy(1.00);
        opticor.setCriticalChance(0.20);

        opticor.setChargeTime(2.00);
        opticor.setCriticalDamage(2.5);
        opticor.setHeadshotMultiplier(2.0);
        opticor.setFireRate(1.0);
        opticor.setMagazineSize(5);
        opticor.setMaxAmmo(200);
        opticor.setAccuracyMultiplier(1.0);
        opticor.setMultishot(1.00);
        List<Damage> opticorDamageTypes = new ArrayList<>();
        List<Damage> opticorSecondaryDamageTypes = new ArrayList<>();

        opticorDamageTypes.add(new Damage(DamageType.PUNCTURE, 850));
        opticorDamageTypes.add(new Damage(DamageType.IMPACT, 100));
        opticorDamageTypes.add(new Damage(DamageType.SLASH, 50));
        opticorSecondaryDamageTypes.add(new Damage(DamageType.MAGNETIC, 400));

        opticor.setReloadTime(2.0);
        opticor.setStatusChance(.20);

        /* Fluff weapon information. **/
        opticor.setNoiseLevel(Weapon.NoiseLevel.ALARMING);
        opticor.setTriggerType(Weapon.TriggerType.CHARGE);
        opticor.setSlot(Weapon.Slot.PRIMARY);
        opticor.setType(Weapon.WeaponType.RIFLE);
        opticor.setAmmoType(Weapon.AmmoType.RIFLE);
        opticor.setDisposition(Weapon.Disposition.STRONG);
        opticor.setRangeLimit(-1.0);
        opticor.setMasteryRank(14);
        opticor.setName("Opticor");
        opticor.setMods(new ArrayList<>());


        return opticor;
    }

}
