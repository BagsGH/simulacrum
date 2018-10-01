package com.bags.simulacrum.StatusProc;

import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Reflection documenation:
 * https://stackoverflow.com/questions/6094575/creating-an-instance-using-the-class-name-and-calling-constructor
 */
@Data
public abstract class StatusProc { //TODO: try to figure out how to not use default constructor

    StatusPropertyMapper STATUS_PROPERTY_MAPPER = new StatusPropertyMapper();

    protected double duration;
    protected int damageTicks;
    protected DamageType damageType;

    abstract public void apply(Target target);

    public StatusProc withDamageType(String procClassName, DamageType damageType) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("com.bags.simulacrum.StatusProc" + "." + procClassName);
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
        StatusProc proc = null;
        if (object != null) {
            proc = (StatusProc) object;
            proc.setDamageTicks(STATUS_PROPERTY_MAPPER.getStatusProcTicks(damageType));
            proc.setDuration(STATUS_PROPERTY_MAPPER.getStatusProcDuration(damageType));
            proc.setDamageType(damageType);
        }
        return proc;
    }

    abstract public boolean applyInstantly(); //TODO: Better name
}
