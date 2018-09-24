package com.bags.simulacrum.Simulation;

import org.springframework.stereotype.Component;

@Component
public class Random {

    public double getRandom() {
        return Math.random();
    }
}
