package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Reflection documentation:
 * https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor
 */
@Data
public abstract class Status {

    StatusFactory STATUS_PROPERTY_MAPPER = new StatusFactory();

    protected double duration;
    protected DamageType damageType;
    protected double damagePerTick;
    protected int numberOfDamageTicks;
    protected double progressToNextTick;
    protected double durationPerTick;
    protected int tickProgress;

    abstract public DamageSource apply(Target target);

    abstract public void progressTime(double deltaTime);

    abstract public boolean checkProgress();

    abstract public void setupTimers();

    abstract public boolean finished();

    abstract public void removeStatusEffects();

    public Status copy() {

        Class<?> clazz = null;
        try {
            clazz = Class.forName(getClass().getName().replace("class ", ""));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Constructor<?> constructor = null;
        try {
            constructor = clazz != null ? clazz.getConstructor() : null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object object = null;
        try {
            object = (constructor != null ? constructor.newInstance() : null);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        Status copy = null;
        if (object != null) {
            copy = (Status) object;
            copy.setDuration(this.duration);
            copy.setDamageType(this.damageType);
            copy.setDamagePerTick(this.damagePerTick);
            copy.setNumberOfDamageTicks(this.numberOfDamageTicks);
            copy.setProgressToNextTick(this.progressToNextTick);
            copy.setDurationPerTick(this.durationPerTick);
            copy.setTickProgress(this.tickProgress);
        }
        return copy;
    }
}
