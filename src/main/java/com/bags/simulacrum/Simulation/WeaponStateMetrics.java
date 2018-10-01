package com.bags.simulacrum.Simulation;

import com.bags.simulacrum.Weapon.State.*;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.HashMap;
import java.util.Map;

public class WeaponStateMetrics {

    private Map<Class, MutableInt> stateTimings;

    public WeaponStateMetrics() {
        setupStateTimingMap();
    }

    private void setupStateTimingMap() {
        stateTimings = new HashMap<>();
        stateTimings.put(Auto.class, new MutableInt());
        stateTimings.put(Bursting.class, new MutableInt());
        stateTimings.put(Charging.class, new MutableInt());
        stateTimings.put(Fired.class, new MutableInt());
        stateTimings.put(OutOfAmmo.class, new MutableInt());
        stateTimings.put(Ready.class, new MutableInt());
        stateTimings.put(Reloading.class, new MutableInt());
        stateTimings.put(Spooling.class, new MutableInt());
    }

    public void add(Class clazz, double addend) {
        stateTimings.get(clazz).add(addend * 100);
    }

    public int getValue(Class clazz) {
        return stateTimings.getOrDefault(clazz, new MutableInt()).getValue();
    }
}
