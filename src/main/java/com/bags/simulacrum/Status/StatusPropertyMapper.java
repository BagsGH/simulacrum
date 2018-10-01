package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.DamageType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.bags.simulacrum.Damage.DamageType.*;

@Component
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class StatusPropertyMapper {

    private static final Map<DamageType, Double> statusProcDurationMap;

    static {
        statusProcDurationMap = new HashMap<DamageType, Double>() {{
            put(IMPACT, 1.0);
            put(PUNCTURE, 6.0);
            put(SLASH, 6.0);
            put(COLD, 6.0);
            put(ELECTRICITY, 3.0);
            put(HEAT, 6.0);
            put(TOXIN, 8.0);
            put(VOID, 3.0);
            put(BLAST, 1.0);
            put(CORROSIVE, -1.0);
            put(GAS, 8.0);
            put(MAGNETIC, 4.0);
            put(RADIATION, 12.0);
            put(VIRAL, 6.0);
        }};
    }

    public Double getStatusProcDuration(DamageType statusPROCType) {
        return statusProcDurationMap.getOrDefault(statusPROCType, 0.0);
    }

    private static final Map<DamageType, Double> statusProcModifierMap;

    static {
        statusProcModifierMap = new HashMap<DamageType, Double>() {{
            put(PUNCTURE, 0.70);
            put(SLASH, 1.45);
            put(COLD, -0.50);
            put(ELECTRICITY, 0.50);
            put(HEAT, 2.50);
            put(TOXIN, 3.50);
            put(CORROSIVE, 0.25);
            put(GAS, 3.50);
            put(MAGNETIC, -0.75);
            put(VIRAL, -0.50);
        }};
    }

    public Double getStatusProcModifier(DamageType statusPROCType) {
        return statusProcModifierMap.getOrDefault(statusPROCType, 0.0);
    }

    private static final Map<DamageType, String> statusTypeClassNameMap;

    static {
        statusTypeClassNameMap = new HashMap<DamageType, String>() {{
            put(IMPACT, getClassName(Knockback.class));
            put(PUNCTURE, getClassName(Weakened.class));
            put(SLASH, getClassName(Bleed.class));
            put(COLD, getClassName(Freeze.class));
            put(ELECTRICITY, getClassName(TeslaChain.class));
            put(HEAT, getClassName(Ignite.class));
            put(TOXIN, getClassName(Poison.class));
            put(VOID, getClassName(BulletAttractor.class));
            put(BLAST, getClassName(Knockdown.class));
            put(CORROSIVE, getClassName(Corrosion.class));
            put(GAS, getClassName(ToxinCloud.class));
            put(MAGNETIC, getClassName(Disrupt.class));
            put(RADIATION, getClassName(Confusion.class));
            put(VIRAL, getClassName(Virus.class));
            put(null, getClassName(UnimplementedStatus.class));
        }};
    }

    public Status getStatusProcClass(DamageType statusProcType) { //TODO: should not need this if.
        Class<?> clazz = null;
        try {
            clazz = Class.forName(statusTypeClassNameMap.getOrDefault(statusProcType, getClassName(UnimplementedStatus.class)));
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
        Status proc = null;
        if (object != null) {
            proc = (Status) object;
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
            put(IMPACT, 0);
            put(PUNCTURE, 0);
            put(SLASH, 7);
            put(COLD, 0);
            put(ELECTRICITY, 1);
            put(HEAT, 7);
            put(TOXIN, 9);
            put(VOID, 0);
            put(BLAST, 0);
            put(CORROSIVE, 0);
            put(GAS, 1);
            put(MAGNETIC, 0);
            put(RADIATION, 0);
            put(VIRAL, 0);
        }};
    }

    public int getStatusProcTicks(DamageType statusPROCType) {
        return damageTickMap.getOrDefault(statusPROCType, 0);
    }

    private static final Map<DamageType, List<DamageType>> innateDamagesUsedForProcDamageMap;

    static {
        innateDamagesUsedForProcDamageMap = new HashMap<DamageType, List<DamageType>>() {{ //TODO: Flesh out map?
            put(IMPACT, new ArrayList<>());
            put(PUNCTURE, new ArrayList<>());
            put(SLASH, Arrays.asList(IMPACT, PUNCTURE, TRUE, COLD, ELECTRICITY, HEAT, TOXIN, VOID, BLAST, CORROSIVE, GAS, MAGNETIC, RADIATION, VIRAL));
            put(COLD, new ArrayList<>());
            put(ELECTRICITY, Arrays.asList(IMPACT, PUNCTURE, SLASH, TRUE, COLD, HEAT, TOXIN, VOID, BLAST, CORROSIVE, GAS, MAGNETIC, RADIATION, VIRAL));
            put(HEAT, Arrays.asList(IMPACT, PUNCTURE, SLASH, TRUE, COLD, ELECTRICITY, TOXIN, VOID, BLAST, CORROSIVE, GAS, MAGNETIC, RADIATION, VIRAL));
            put(TOXIN, Arrays.asList(IMPACT, PUNCTURE, SLASH, TRUE, COLD, ELECTRICITY, HEAT, VOID, BLAST, CORROSIVE, GAS, MAGNETIC, RADIATION, VIRAL));
            put(VOID, new ArrayList<>());
            put(BLAST, new ArrayList<>());
            put(CORROSIVE, new ArrayList<>());
            put(GAS, Arrays.asList(IMPACT, PUNCTURE, SLASH, TRUE, COLD, ELECTRICITY, HEAT, VOID, BLAST, CORROSIVE, GAS, MAGNETIC, RADIATION, VIRAL));
            put(MAGNETIC, new ArrayList<>());
            put(RADIATION, new ArrayList<>());
            put(VIRAL, new ArrayList<>());
        }};
    }

    public List<DamageType> getInnateDamageTypesUsed(DamageType statusProcType) {
        return innateDamagesUsedForProcDamageMap.getOrDefault(statusProcType, new ArrayList<>());
    }

    private static final Map<DamageType, List<DamageType>> addedElementalDamageTypesUsedForProcDamageMap;

    static {
        addedElementalDamageTypesUsedForProcDamageMap = new HashMap<DamageType, List<DamageType>>() {{ //TODO: Flesh out map?
            put(IMPACT, new ArrayList<>());
            put(PUNCTURE, new ArrayList<>());
            put(SLASH, new ArrayList<>());
            put(COLD, new ArrayList<>());
            put(ELECTRICITY, Arrays.asList(ELECTRICITY));
            put(HEAT, Arrays.asList(HEAT));
            put(TOXIN, Arrays.asList(TOXIN));
            put(VOID, new ArrayList<>());
            put(BLAST, new ArrayList<>());
            put(CORROSIVE, new ArrayList<>());
            put(GAS, Arrays.asList(TOXIN));
            put(MAGNETIC, new ArrayList<>());
            put(RADIATION, new ArrayList<>());
            put(VIRAL, new ArrayList<>());
        }};
    }

    public List<DamageType> getAddedElementalDamageTypesUsed(DamageType statusProcType) {
        return addedElementalDamageTypesUsedForProcDamageMap.getOrDefault(statusProcType, new ArrayList<>());
    }
}
