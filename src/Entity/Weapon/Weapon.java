package Entity.Weapon;

import Entity.Entity;
import Manager.FileManager;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Weapon extends Entity {

    public int ammo,type,amount,damage,rate;
    public String name;
    public boolean isHeld;
    public float bVel,spread;

    Weapon(int x, int y, int w, int h, float orient, int ammo) {
        super(x, y, w, h, orient);
        sprite = FileManager.M4;
        isHeld = false;
        this.ammo = ammo;

        this.w = sprite.getWidth();
        this.h = sprite.getHeight();
        hitColor = Color.DARK_GRAY;
        transparent = true;
    }

//    public void draw(Graphics2D g) {
//        AffineTransform oldTForm = g.getTransform();
//        if (orient != 0) g.rotate(orient, x, y);
//        g.drawImage(sprite, x - w / 2 + 24, y - h / 2 + 2, null);
//        g.setTransform(oldTForm);
//    }

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
