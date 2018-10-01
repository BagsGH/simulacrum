package com.bags.simulacrum.Status;

import com.bags.simulacrum.Entity.Target;

public class Disrupt extends Status {
    @Override
    public void apply(Target target) {

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
}
