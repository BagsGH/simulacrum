package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Entity.Target;

import java.util.ArrayList;

import static com.bags.simulacrum.Damage.DamageSourceType.DOT;

public class Weakened extends Status {
    @Override
    public void apply(Target target) {

    }

    @Override
    public DamageSource getDamageSource() {
        return new DamageSource(DOT, new ArrayList<>());
    }


    @Override
    public boolean applyInstantly() {
        return false;
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
