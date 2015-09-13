package Entity.Weapon;

import Entity.Entity;
import Entity.Player;
import StaticManagers.FileManager;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Weapon extends Entity {

    public float vx, vy;
    public int ammo, type, amount, damage, rate, maxAmmo, gunLength;
    public String name;
    public boolean isHeld;
    public float bVel, spread;
    public Player user;
    public Clip hitSound, shootSound;

    Weapon(int x, int y, int w, int h, float orient, Player i_user) {
        super(x, y, w, h, orient);
        vx = vy = 0;
        sprite = FileManager.images.get("m4.png");
        hitSound = FileManager.sounds.get("bulletHit.wav");
        isHeld = false;
        user = i_user;
        this.w = sprite.getWidth();
        this.h = sprite.getHeight();
        hitColor = Color.DARK_GRAY;
        setConstants();
        ammo = maxAmmo;
    }

    public void unloadUser() {
        user = null;
    }

    public void draw(Graphics2D g) {
        if (user != null) {
            AffineTransform oldTForm = g.getTransform();
            g.rotate(orient, x, y);
            g.drawImage(sprite, x - w / 2 + 24, y - h / 2 + 2, null);
            g.setTransform(oldTForm);
        } else {
            AffineTransform oldTForm = g.getTransform();
            g.rotate(orient, x, y);
            g.drawImage(sprite, x - w / 2, y - h / 2, null);
            g.setTransform(oldTForm);
        }
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
        return true;
    }

    public void updatePos() {
        if (user != null) {
            orient = user.orient;
            x = user.x;
            y = user.y;
        } else {
            System.out.println("Unauthorized update of weapon position");
            System.exit(1);
        }
    }
}
