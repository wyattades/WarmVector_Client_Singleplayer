package Entity.Weapon;

import Entity.Entity;
import Main.Game;
import Manager.FileManager;

import java.awt.*;
import java.awt.geom.AffineTransform;

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
        sprite = FileManager.M4;
        this.w = sprite.getWidth() * Game.SCALEFACTOR;
        this.h = sprite.getHeight() * Game.SCALEFACTOR;
    }

    public void draw(Graphics2D g){
        AffineTransform oldTForm = g.getTransform();
        if (orient != 0) g.rotate(orient, dx, dy);
        g.drawImage(sprite, (int)(dx-w/2)+34,(int)(dy-h/2)+2,(int)w,(int)h,null);
        g.setTransform(oldTForm);
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
