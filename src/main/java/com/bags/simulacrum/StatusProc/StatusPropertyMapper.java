package com.bags.simulacrum.StatusProc;

import com.bags.simulacrum.Damage.DamageType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class StatusPropertyMapper {

    private static final Map<DamageType, Double> statusProcDurationMap;

    static {
        statusProcDurationMap = new HashMap<DamageType, Double>() {{
            put(DamageType.IMPACT, 1.0);
            put(DamageType.PUNCTURE, 6.0);
            put(DamageType.SLASH, 6.0);
            put(DamageType.COLD, 6.0);
            put(DamageType.ELECTRICITY, 3.0);
            put(DamageType.HEAT, 6.0);
            put(DamageType.TOXIN, 8.0);
            put(DamageType.VOID, 3.0);
            put(DamageType.BLAST, 1.0);
            put(DamageType.CORROSIVE, -1.0);
            put(DamageType.GAS, 8.0);
            put(DamageType.MAGNETIC, 4.0);
            put(DamageType.RADIATION, 12.0);
            put(DamageType.VIRAL, 6.0);
        }};
    }

    public Double getStatusProcDuration(DamageType statusPROCType) {
        return statusProcDurationMap.getOrDefault(statusPROCType, 0.0);
    }

    private static final Map<DamageType, Double> statusProcModifierMap;

    static {
        statusProcModifierMap = new HashMap<DamageType, Double>() {{
            put(DamageType.PUNCTURE, 0.70);
            put(DamageType.SLASH, 1.45);
            put(DamageType.COLD, -0.50);
            put(DamageType.ELECTRICITY, 0.50);
            put(DamageType.HEAT, 2.50);
            put(DamageType.TOXIN, 3.50);
            put(DamageType.CORROSIVE, 0.25);
            put(DamageType.GAS, 3.50);
            put(DamageType.MAGNETIC, -0.75);
            put(DamageType.VIRAL, -0.50);
        }};
    }

    public Double getStatusProcModifier(DamageType statusPROCType) {
        return statusProcModifierMap.getOrDefault(statusPROCType, 0.0);
    }

    private static final Map<DamageType, String> statusTypeClassNameMap;

    static {
        statusTypeClassNameMap = new HashMap<DamageType, String>() {{
            put(DamageType.IMPACT, getClassName(Knockback.class));
            put(DamageType.PUNCTURE, getClassName(Weakened.class));
            put(DamageType.SLASH, getClassName(Bleed.class));
            put(DamageType.COLD, getClassName(Freeze.class));
            put(DamageType.ELECTRICITY, getClassName(TeslaChain.class));
            put(DamageType.HEAT, getClassName(Ignite.class));
            put(DamageType.TOXIN, getClassName(Poison.class));
            put(DamageType.VOID, getClassName(BulletAttractor.class));
            put(DamageType.BLAST, getClassName(Knockdown.class));
            put(DamageType.CORROSIVE, getClassName(Corrosion.class));
            put(DamageType.GAS, getClassName(ToxinCloud.class));
            put(DamageType.MAGNETIC, getClassName(Disrupt.class));
            put(DamageType.RADIATION, getClassName(Confusion.class));
            put(DamageType.VIRAL, getClassName(Virus.class));
            put(null, getClassName(UnimplementedProc.class));
        }};
    }

    public StatusProc getStatusProcClass(DamageType statusProcType) { //TODO: should not need this if.
        Class<?> clazz = null;
        try {
            clazz = Class.forName(statusTypeClassNameMap.getOrDefault(statusProcType, getClassName(UnimplementedProc.class)));
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
            proc.setDamageType(statusProcType);
            proc.setDuration(getStatusProcDuration(statusProcType));
            proc.setDamageTicks(getStatusProcTicks(statusProcType));
        }
        return proc;
    }

    private static String getClassName(Class clazz) {
        return clazz.getName().replace("class ", "");
    }

    private static final Map<DamageType, Integer> damageTickMap;

    static {
        damageTickMap = new HashMap<DamageType, Integer>() {{ //TODO: Flesh out map?
            put(DamageType.IMPACT, 0);
            put(DamageType.PUNCTURE, 0);
            put(DamageType.SLASH, 7);
            put(DamageType.COLD, 0);
            put(DamageType.ELECTRICITY, 1);
            put(DamageType.HEAT, 7);
            put(DamageType.TOXIN, 9);
            put(DamageType.VOID, 0);
            put(DamageType.BLAST, 0);
            put(DamageType.CORROSIVE, 0);
            put(DamageType.GAS, 1);
            put(DamageType.MAGNETIC, 0);
            put(DamageType.RADIATION, 0);
            put(DamageType.VIRAL, 0);
        }};
    }

    public int getStatusProcTicks(DamageType statusPROCType) {
        return damageTickMap.getOrDefault(statusPROCType, 0);
    }
}
