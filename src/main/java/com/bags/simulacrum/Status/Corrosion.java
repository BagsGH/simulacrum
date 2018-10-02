package com.bags.simulacrum.Status;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.ArrayList;

import static com.bags.simulacrum.Damage.DamageSourceType.DOT;

@Data
public class Corrosion extends Status {

    private static final double ARMOR_REDUCTION_RATIO = 0.25;

    private Corrosion(DamageType damageType, double duration, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.numberOfDamageTicks = damageTicks;
    }

    public Corrosion() {

    }

    @Override
    public void apply(Target target) {
        Health armor = target.getArmor();
        if (armor != null) {
            double currentArmor = armor.getHealthValue();
            armor.setHealthValue(currentArmor * (1 - ARMOR_REDUCTION_RATIO));
        }
    }

    @Override
    public DamageSource getDamageSource() {
        return new DamageSource(DOT, new ArrayList<>());
    }

    @Override
    public boolean applyInstantly() {
        return true;
    }

    @Override
    public void progressTime(double deltaTime) {

    }

    @Override
    public boolean checkProgress() {
        return false;
    }

    @Override
    public void setupTimers() {
    }

    @Override
    public boolean finished() {
        return false;
    }
}