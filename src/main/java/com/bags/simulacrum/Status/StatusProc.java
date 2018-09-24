package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;

public interface StatusProc {

    StatusPropertyMapper STATUS_PROPERTY_MAPPER = new StatusPropertyMapper();

    void apply(Target target);

    StatusProc withDamageType(DamageType damageType);

    boolean applyInstantly(); //TODO: Better name
}
