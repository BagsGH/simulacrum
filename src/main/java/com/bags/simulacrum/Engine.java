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

        System.out.println("===Original weapons===");
        System.out.println(ignisWraith);

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

        Weapon ignisWraithModded = weaponModifier.modWeapon(ignisWraith);

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
        ignisWraith.setMultishot(0.00);
        List<Damage> ignisDamageTypes = new ArrayList<>();
        Damage heat = new Damage();
        heat.setDamageValue(35);
        heat.setType(DamageType.HEAT);
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
