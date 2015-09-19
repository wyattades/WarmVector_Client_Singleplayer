package Entities.Weapon;

import Entities.Entity;
import Entities.Player;
import StaticManagers.FileManager;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Weapon extends Entity {

    public float vx, vy;
    public int ammo, amount, damage, clipSize, clipAmount, rate;
    public String name;
    public float bVel, spread, gunLength;
    public Player user;
    public Clip hitSound, shootSound;

    Weapon(float x, float y,  float orient, Player i_user,
           String name, int clipSize, int clipAmount, int amount, int rate,
           float gunLength, int damage, float spread, float bVel, Clip shootSound) {

        super(x, y,  orient);

        vx = vy = 0;
        hitSound = FileManager.sounds.get("bulletHit.wav");
        user = i_user;
        hitColor = Color.DARK_GRAY;

        this.name = name;
        this.ammo = this.clipSize = clipSize;
        this.clipAmount = clipAmount;
        this.amount = amount;
        this.rate = rate;
        this.gunLength = gunLength;
        this.damage = damage;
        this.spread = spread;
        this.bVel = bVel;
        this.shootSound = shootSound;

    }

    public void draw(Graphics2D g) {
        if (user != null) {
            AffineTransform oldTForm = g.getTransform();
            g.rotate(user.orient, user.x, user.y);
            g.drawImage(sprite, Math.round(user.x - w / 2 + 24), Math.round(user.y - h / 2 + 2), null);
            g.setTransform(oldTForm);
        } else {
            AffineTransform oldTForm = g.getTransform();
            g.rotate(orient, x, y);
            g.drawImage(sprite, Math.round(x - w / 2), Math.round(y - h / 2), null);
            g.setTransform(oldTForm);
        }
    }

    public void changeAmmo(int amount) {
        if (ammo > 0) {
            ammo += amount;
        }
    }

    public boolean hit(int amount, float angle) {
//        vx += Math.cos(angle);
//        vy += Math.sin(angle);
        return true;
    }

    public void unloadUser() {
        user = null;
    }
}
