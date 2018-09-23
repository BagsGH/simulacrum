package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;

public interface StatusPROC {

    public void applyStatusToTarget(Target target);

    public StatusPROC withProperties(DamageType damageType, double damage);
}
