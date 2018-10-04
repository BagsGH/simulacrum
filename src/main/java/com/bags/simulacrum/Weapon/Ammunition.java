package com.bags.simulacrum.Weapon;

import lombok.Data;

@Data
public class Ammunition {

    private int currentMagazineSize;
    private int magazineSize;
    private int currentAmmo;
    private int maxAmmo;

    public Ammunition(int maxAmmo, int currentAmmo, int magazineSize, int currentMagazineSize) {
        this.maxAmmo = maxAmmo;
        this.currentAmmo = currentAmmo;
        this.magazineSize = magazineSize;
        this.currentMagazineSize = currentMagazineSize;
    }

    public Ammunition copy() {
        return new Ammunition(this.maxAmmo, this.currentAmmo, this.magazineSize, this.currentMagazineSize);
    }

    public void subtractAmmo() {
        this.currentMagazineSize--;
    }

    public void loadMagazine() {
        if (this.currentAmmo > 0) {
            int freshMagazineSize = this.magazineSize < this.currentAmmo ? this.magazineSize : this.currentAmmo;
            this.currentMagazineSize = freshMagazineSize;
            this.currentAmmo -= freshMagazineSize;
        }
    }

}
