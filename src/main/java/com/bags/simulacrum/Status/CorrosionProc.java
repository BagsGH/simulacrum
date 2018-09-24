package com.bags.simulacrum.Status;

import com.bags.simulacrum.Armor.Health;
import com.bags.simulacrum.Armor.HealthClass;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.List;

@Data
public class CorrosionProc implements StatusProc {

    private double duration;
    private int damageTicks;
    private DamageType damageType;

    private static final double ARMOR_REDUCTION_RATIO = 0.25;

    private CorrosionProc(DamageType damageType, double duration, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.damageTicks = damageTicks;
    }

    public CorrosionProc() {

    }

    @Override
    public void apply(Target target) {
        Health armor = findArmor(target.getHealth());
        if (armor != null) {
            double currentArmor = armor.getHealthValue();
            armor.setHealthValue(currentArmor * (1 - ARMOR_REDUCTION_RATIO));
        }
    }

    @Override
    public StatusProc withDamageType(DamageType damageType) {
        double duration = STATUS_PROPERTY_MAPPER.getStatusProcDuration(damageType);
        int ticks = STATUS_PROPERTY_MAPPER.getStatusProcTicks(damageType);

        return new CorrosionProc(damageType, duration, ticks);
    }

    private Health findArmor(List<Health> health) {
        return health.stream().filter(h -> HealthClass.isArmor(h.getHealthClass())).findFirst().orElse(null);
    }

    @Override
    public boolean applyInstantly() {
        return true;
    }
}