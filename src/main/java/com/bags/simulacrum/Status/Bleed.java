package com.bags.simulacrum.Status;

import com.bags.simulacrum.Damage.Damage;
import com.bags.simulacrum.Damage.DamageSource;
import com.bags.simulacrum.Damage.DamageType;
import com.bags.simulacrum.Entity.Target;
import lombok.Data;

import java.util.Collections;

import static com.bags.simulacrum.Damage.DamageSourceType.DOT;
import static com.bags.simulacrum.Damage.DamageType.TRUE;

@Data
public class Bleed extends StatusDot { //TODO: find a way to make this hit directly to healths

    private Bleed(DamageType damageType, double duration, int damageTicks) {
        this.damageType = damageType;
        this.duration = duration;
        this.numberOfDamageTicks = damageTicks;
    }

    public Bleed() {

    }

    @Override
    public DamageSource apply(Target target) {
        this.progressToNextTick = 0.0;
        this.tickProgress++;
        Damage dealsTrueDamage = new Damage(TRUE, this.damagePerTick);
        return new DamageSource(DOT, Collections.singletonList(dealsTrueDamage));
    }



    /*
    Am I missing something with the slash proc?
    Slash damage's status effect is Bleed, lacerating the enemy with a damage over time effect that deals 35% of the source's innate damage
    .  Innate says without elemental or IPS mods, so does that mean it includes things such as HC, serration, etc?
SmilingMadToday at 12:54 PM
    that wording is bad
    it uses base damage modified by base damage modifiers
    okay that wording is bad too
    serration and HC work
    elemental mods dont
    physical damage mods dont
    of course multishot means you get more slash procs because multiple bullets, so in the end tyou get more slash from that as well
BagsToday at 12:55 PM
    okay, that makes sense
    I was just thinking "how the hell is this good if it ignores all mods" lol
    but if it counts hc, serr, etc ok
    makes more sense
SkullCollectorToday at 12:57 PM
    Is that just the innate slash damage or total physical damage?
SmilingMadToday at 12:58 PM
    total base damage
    this means whatever damage types it has at base, modified by base damage mods
    this is why hunter munitions works on weapons like the amprex, despite that weapon having pure electricity damage at base
    slash procs counterintuitively do not care about slash damage specifically
    so adding slash damage through a slash mod doesnt actually add any damage to the proc
SmilingMadToday at 1:00 PM
    all it actually does is increase the amount of non-proc slash damage, which in turn increases the damage damageType's status bias
    so you will proc slash more often
    of course, this is meaningless on a weapon that doesnt have slash in the first place, because in that case the mod does nothing
    to give a practical example of why you should  use such mods, the 120% slash damage mod is often added to the tigris prime to increase the amount of slash procs it gets
    so youll get more procs over any elemental mods that have been added
    if you run viral (which synergizes greatly with slash) you dont really benefit from followup viral procs, so having more slash than viral procs is fantastic

    arguments can be made for adding, say, puncture damage for their damage multipliers against armoured targets
    though this is usually only really desireable on weapons with an already high puncture stat
    and i have no idea if this actually helps the weapon all that much
    if the weapon usually relies on status procs then puncture will just dilute the status pool with less useful puncture procs
    i think i said way too much here oops
     */
}
