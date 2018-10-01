package com.bags.simulacrum.Weapon.State;

public interface FiringState {
    FiringState progressTime(double deltaTime);
}