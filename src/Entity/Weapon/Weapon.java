package Entity.Weapon;

import Entity.Entity;

/**
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Weapon extends Entity{

    public int ammo,type,amount,damage,rate;
    public String name;
    public boolean isHeld;
    public double bVel,spread;

    public Weapon(double x, double y, double w, double h, double orient, int ammo) {
        super(x,y,w,h,orient);
        isHeld = false;
        this.ammo = ammo;
    }

    public Weapon(int ammo) {
        isHeld = false;
        this.ammo = ammo;
    }

    protected abstract void setConstants();

    public void changeAmmo(int amount) {
        if (ammo > 0) {
            ammo += amount;
        }
    }

    public boolean hit(int amount) {

        return false;
    }

}
