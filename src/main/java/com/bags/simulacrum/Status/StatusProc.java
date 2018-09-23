package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;

public interface StatusProc {

    public void applyStatusToTarget(Target target);

    public StatusProc withProperties(DamageType damageType, double damage);
}
