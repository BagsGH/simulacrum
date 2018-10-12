package com.bags.simulacrum.Status;

import com.bags.simulacrum.Configuration.StatusProcConfig;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Simulation.DamageMetrics;
import com.bags.simulacrum.Simulation.RandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class StatusProcHelper {

    private final RandomNumberGenerator randomNumberGenerator;

    private final StatusFactory statusFactory;

    private final StatusProcConfig config;

    @PostConstruct
    public void test() {
        System.out.println("config.getIpsStatusWeight(): " + config.getIpsStatusWeight());
    }

    @Autowired
    public StatusProcHelper(RandomNumberGenerator randomNumberGenerator, StatusFactory statusFactory, StatusProcConfig config) {
        this.randomNumberGenerator = randomNumberGenerator;
        this.statusFactory = statusFactory;
        this.config = config;
    }

    public Status constructStatusProc(DamageSource damageSource, Map<DamageType, Double> damageDoneToHealth, Map<DamageType, Double> damageDoneToShields) {
        Map<DamageType, Double> weightedDamagePerType = DamageMetrics.initialDamageMap();
        Map<DamageType, Double> damagePerType = DamageMetrics.initialDamageMap();

        populateDamageMaps(damageDoneToHealth, weightedDamagePerType, damagePerType);
        populateDamageMaps(damageDoneToShields, weightedDamagePerType, damagePerType);

        double totalDamageWithIPSWeights = 0.0;
        for (DamageType damageType : weightedDamagePerType.keySet()) {
            totalDamageWithIPSWeights += weightedDamagePerType.get(damageType);
        }

        Map<DamageType, Double> statusPROCChanceMap = DamageMetrics.initialDamageMap();
        for (DamageType damageType : weightedDamagePerType.keySet()) {
            statusPROCChanceMap.put(damageType, weightedDamagePerType.get(damageType) / totalDamageWithIPSWeights);
        }

        double statusTypeRNG = randomNumberGenerator.getRandomPercentage();

        return statusFactory.getStatusProc(damageSource, getStatusProcType(statusPROCChanceMap, statusTypeRNG));
    }

    private void populateDamageMaps(Map<DamageType, Double> damageDoneToHealth, Map<DamageType, Double> weightedDamagePerType, Map<DamageType, Double> damagePerType) {
        for (DamageType damageType : damageDoneToHealth.keySet()) {
            double weightedNewValue = damageDoneToHealth.get(damageType) * (DamageType.isIPS(damageType) ? config.getIpsStatusWeight() : 1.0) + weightedDamagePerType.get(damageType);
            weightedDamagePerType.put(damageType, weightedNewValue);
            double newValue = damageDoneToHealth.get(damageType) + damagePerType.get(damageType);
            damagePerType.put(damageType, newValue);
        }
    }

    private DamageType getStatusProcType(Map<DamageType, Double> statusPROCChanceMap, double statusTypeRNG) {
        double minOfChanceRangeForStatusType = 0.0;
        double maxOfChanceRangeForStatusType = 0.0;
        for (DamageType damageType : statusPROCChanceMap.keySet()) {
            if (DamageType.isIPS(damageType)) {
                maxOfChanceRangeForStatusType = statusPROCChanceMap.get(damageType) + maxOfChanceRangeForStatusType;
                if (statusTypeRNG >= minOfChanceRangeForStatusType && statusTypeRNG < maxOfChanceRangeForStatusType) {
                    return damageType;
                }
                minOfChanceRangeForStatusType = maxOfChanceRangeForStatusType;
            }
        }
        for (DamageType damageType : statusPROCChanceMap.keySet()) {
            if (DamageType.isElemental(damageType)) {
                maxOfChanceRangeForStatusType = statusPROCChanceMap.get(damageType) + maxOfChanceRangeForStatusType;
                if (statusTypeRNG >= minOfChanceRangeForStatusType && statusTypeRNG < maxOfChanceRangeForStatusType) {
                    return damageType;
                }
                minOfChanceRangeForStatusType = maxOfChanceRangeForStatusType;
            }
        }
        return null; //TODO: can I get rid of this?!
    }
}


/*

BagsToday at 9:03 AM
if you have 50% status, and 2+ elemental damage types, how does that work? Was reading the wiki and not 100% sure. do each status damageType have a 50% chance, or is it a 50% chance that one ofthem happens, or what?
JaristysToday at 9:06 AM
how ive understood it: 50% status chance means what is says, 50% chance for a status proc to occur. what proc is then applied depends on the ratios of different types of damage.
so say you have a gun with 1 slash 10 corrosive damage, the corrosive is much more likely to be the proc that occurs
SmilingMadToday at 9:07 AM
that is the general gist, but not entirely accurate
BagsToday at 9:07 AM
yeah the wiki really just seems to focus on shotguns and how they do status lol
SmilingMadToday at 9:08 AM
basically, physical damage types are weighed more strongly
3x or 4x iirc
so if you you have 5 corrosive and 5 blast damage, equal odds for each to proc
but if you have 5 slash and 5 corrosive, slash will proc much more
BagsToday at 9:08 AM
okay, but only 1 will proc?
so you cant get 2 status procs per bullet?
SmilingMadToday at 9:08 AM
correct
BagsToday at 9:08 AM
gotcha
SmilingMadToday at 9:09 AM
and yeah shotgun status is fucky
JaristysToday at 9:09 AM
well shotguns are weird because the status is per pellet afaik
BagsToday at 9:09 AM
yeah, the wiki goes into detail on the math there, so it's clear
SmilingMadToday at 9:09 AM
its not really exclusive to shotguns per say
its more specifically something that all guns with innate multishot do
this covers most shotguns
but the twin grakatas for example have the same issue
BagsToday at 9:09 AM
clem grataka :rage:
SmilingMadToday at 9:10 AM
plasmor and astilla, despite being shotguns and using shotgun mods, only fire 1 projectile at base and dont care
and the phage and phantasma, both of which are beam shotguns, do have multiple beams
but beam weapons dont let multishot affect status procs
so they always proc att the same rate regardless of the amount of beams that touch the enemy
i guess the convectrix also belongs in that group but its easy to forget about it
BagsToday at 9:12 AM
thx
for like a shotgun, can some of the pellets proc one status, and the others another? :thinking:
SmilingMadToday at 9:15 AM
yep
BagsToday at 9:15 AM
coo
SmilingMadToday at 9:15 AM
bullets produced by multishot can proc stuff seperately
ChungisBungisToday at 9:15 AM
ech, i hit mr16 and now my nice gold icon is back to bronze
BagsToday at 9:15 AM
guess ill just model shotguns as x pellet count multishot then
ChungisBungisToday at 9:16 AM
oh hey on shotguns and status, whats up with the 100% stat chance thing
feels like people don't care if its even like, 99% chance
SmilingMadToday at 9:16 AM
i forgot how exactly it works, but iirc at 50% status chance there is a 50% for one pellet to proc
ChungisBungisToday at 9:16 AM
but once its 100% its amazing
SmilingMadToday at 9:16 AM
at 100% the game shits itself and just gives every pellet a proc
ChungisBungisToday at 9:17 AM
:thonking~1:
SmilingMadToday at 9:17 AM
yeah
NEW MESSAGES
ChungisBungisToday at 9:17 AM
so before 100% its actually really low
but then 100% becomes actually 100%
 */