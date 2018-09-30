package com.bags.simulacrum.Simulation;

import org.springframework.stereotype.Component;

@Component
public class RandomNumberGenerator {

    public double getRandomPercentage() {
        return Math.random();
    }
}
