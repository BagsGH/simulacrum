package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;

public interface StatusProc {

    StatusPropertyMapper STATUS_PROPERTY_MAPPER = new StatusPropertyMapper();

    public void applyStatusToTarget(Target target);

    public StatusProc withDamageType(DamageType damageType);
}
