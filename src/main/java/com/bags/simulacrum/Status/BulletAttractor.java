package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Entity.Target;

import java.util.ArrayList;

import static com.bags.simulacrum.Damage.DamageSourceType.DOT;

public class BulletAttractor extends Status {
    @Override
    public DamageSource apply(Target target) {
        return new DamageSource(DOT, new ArrayList<>());
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

    @Override
    public void removeStatusEffects() {

    }
}
