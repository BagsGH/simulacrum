package com.bags.simulacrum.StatusProc;

import com.bags.simulacrum.Damage.DamageType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
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

    private static final Map<DamageType, StatusProc> statusTypeMap;

    static {
        statusTypeMap = new HashMap<DamageType, StatusProc>() {{
            put(DamageType.IMPACT, new KnockbackProc());
            put(DamageType.PUNCTURE, new UnimplementedProc());
            put(DamageType.SLASH, new BleedProc());
            put(DamageType.COLD, new UnimplementedProc());
            put(DamageType.ELECTRICITY, new UnimplementedProc());
            put(DamageType.HEAT, new IgniteProc());
            put(DamageType.TOXIN, new UnimplementedProc());
            put(DamageType.VOID, new UnimplementedProc());
            put(DamageType.BLAST, new UnimplementedProc());
            put(DamageType.CORROSIVE, new CorrosionProc());
            put(DamageType.GAS, new UnimplementedProc());
            put(DamageType.MAGNETIC, new UnimplementedProc());
            put(DamageType.RADIATION, new UnimplementedProc());
            put(DamageType.VIRAL, new UnimplementedProc());
            put(null, new UnimplementedProc());
        }};
    }

    public StatusProc getStatusProcClass(DamageType statusProcType) { //TODO: should not need this if.
        if (statusProcType == null) {
            return new UnimplementedProc();
        }
        return statusTypeMap.getOrDefault(statusProcType, new UnimplementedProc()).withDamageType(statusProcType);
    }

    private static final Map<DamageType, Integer> damageTickMap;

    static {
        damageTickMap = new HashMap<DamageType, Integer>() {{
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
