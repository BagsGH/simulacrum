package com.bags.simulacrum;

import lombok.Data;

@Data
public class Damage {

    private DamageType type;
    private double value;


    public enum DamageType {
        PUNCTURE,
        IMPACT,
        SLASH,
        ELECTRICITY,
        COLD,
        HEAT,
        TOXIN,
        TRUE,
        VOID,
        BLAST,
        CORROSIVE,
        GAS,
        MAGNETIC,
        RADIATION,
        VIRAL
    }

}
