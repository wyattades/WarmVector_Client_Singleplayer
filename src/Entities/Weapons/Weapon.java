package Entities.Weapons;

import Entities.Entity;
import StaticManagers.FileManager;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Weapon extends Entity implements Cloneable {

    public static final float ORIGIN_RADIUS = 20.0f;

    public int ammo, reserveAmmo;

    public final int clipSize, amountPerShot, damage, rate;
    public final String name, shootSound;
    public final float i_speed, spread, accel, explodeRadius;

    public Weapon(String _name, int _clipSize,
           int _reserveAmmo, int _amountPerShot, int _rate, int _damage,
           float _spread, float _i_speed, float _accel, float _explodeRadius) {

        super(0, 0, 0, Color.DARK_GRAY);

        name = _name;
        ammo = clipSize = _clipSize;
        reserveAmmo = _reserveAmmo;
        amountPerShot = _amountPerShot;
        rate = _rate;
        damage = _damage;
        spread = _spread;
        i_speed = _i_speed;
        accel = _accel;
        explodeRadius = _explodeRadius;

        shootSound = "m4_shoot.wav";
        sprite = FileManager.getImage("gun_" + name + ".png");
        setBounds(sprite.getWidth(), sprite.getWidth());

    }

    @Override
    public Weapon clone() {
        try {
            final Weapon result = (Weapon) super.clone();
            result.state = true;
            return result;
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    public void changeAmmo(int amount) {
        ammo += amount;
        if (ammo < 0) ammo = 0;
    }

    public void handleHit(float damage, float angle) {
//        vx += Math.cos(angle);
//        vy += Math.sin(angle);
    }

}
