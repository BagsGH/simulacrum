package com.bags.simulacrum;


import com.bags.simulacrum.Damage.Damage;
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

    @Autowired
    private WeaponModifier weaponModifier;

    public void start() {
        weaponModifier = new WeaponModifier();
        Weapon ignisWraith = setupIgnis();
        Weapon opticor = setupOpticor();

        System.out.println("===Original weapons===");
        System.out.println(ignisWraith);
        System.out.println(opticor);

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

        Mod heatdmg = new Mod();
        heatdmg.setDamage(new Damage(DamageType.HEAT, 0.0, 0.15));

        opticor.setMods(Arrays.asList(vileAccel, heavyCalibre, vs, ps, serration, heatdmg));


        Weapon ignisWraithModded = weaponModifier.modWeapon(ignisWraith);
        Weapon opticorModded = weaponModifier.modWeapon(opticor);

        System.out.println("===Modded weapons===");
        System.out.println(ignisWraithModded);
        System.out.println(opticorModded);
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
        ignisWraith.setMultishot(0.00);
        List<Damage> ignisDamageTypes = new ArrayList<>();
        Damage heat = new Damage(DamageType.HEAT, 35.0);
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
        opticor.setMultishot(0.00);
        List<Damage> opticorDamageTypes = new ArrayList<>();
        List<Damage> opticorSecondaryDamageTypes = new ArrayList<>();

        opticorDamageTypes.add(new Damage(DamageType.PUNCTURE, 850));
        opticorDamageTypes.add(new Damage(DamageType.IMPACT, 100));
        opticorDamageTypes.add(new Damage(DamageType.SLASH, 50));
        opticorSecondaryDamageTypes.add(new Damage(DamageType.MAGNETIC, 400));

        opticor.setDamageTypes(opticorDamageTypes);
        opticor.setSecondaryDamageTypes(opticorSecondaryDamageTypes);
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
