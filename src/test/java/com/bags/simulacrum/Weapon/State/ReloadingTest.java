package com.bags.simulacrum.Weapon.State;

import com.bags.simulacrum.Weapon.FireStateProperties;
import com.bags.simulacrum.Weapon.WeaponInformationEnums.TriggerType;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ReloadingTest {

    private Reloading subject;

    @Before
    public void setup() {
        subject = new Reloading(new FireStateProperties.FireStatePropertiesBuilder(TriggerType.HELD, 0.01, 0, 0).build());
    }

    @Test
    public void itChangesToOutOfAmmoWhenAmmoIsOut() {
        FiringState actualState = subject.progressTime(0.01);
        assertTrue(actualState instanceof OutOfAmmo);
    }

    @Test
    public void itChangesStateAfterReloading() {
        FireStateProperties fakeFireStateProperties = new FireStateProperties.FireStatePropertiesBuilder(TriggerType.HELD, 0.01, 1, 1).build();

        subject = new Reloading(fakeFireStateProperties);
        FiringState actualState = subject.progressTime(0.01);
        assertFalse(actualState instanceof Ready);
    }

}