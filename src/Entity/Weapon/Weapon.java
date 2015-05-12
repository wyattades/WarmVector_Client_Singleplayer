package Entity.Weapon;

import Entity.Entity;
import Manager.FileManager;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Weapon extends Entity {

    public float vx, vy;
    public int ammo,type,amount,damage,rate;
    public String name;
    public boolean isHeld;
    public float bVel,spread;

    Weapon(int x, int y, int w, int h, float orient, int ammo) {
        super(x, y, w, h, orient);
        vx = vy = 0;
        sprite = FileManager.M4;
        isHeld = false;
        this.ammo = ammo;

        this.w = sprite.getWidth();
        this.h = sprite.getHeight();
        hitColor = Color.DARK_GRAY;
        setConstants();
    }

    protected abstract void setConstants();

    public void changeAmmo(int amount) {
        if (ammo > 0) {
            ammo += amount;
        }
    }

    public boolean hit(int amount, float angle) {
        vx += Math.cos(angle);
        vy += Math.sin(angle);
        return false;
    }

}
