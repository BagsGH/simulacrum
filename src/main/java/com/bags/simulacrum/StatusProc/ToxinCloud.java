package com.bags.simulacrum.StatusProc;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;

public class ToxinCloud implements StatusProc {
    @Override
    public void apply(Target target) {

    }

    @Override
    public StatusProc withDamageType(DamageType damageType) {
        return null;
    }

    @Override
    public boolean applyInstantly() {
        return false;
    }
}
