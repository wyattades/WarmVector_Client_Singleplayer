package Entities.Projectiles;

import Entities.Entity;
import Entities.Player;
import Entities.Weapons.Weapon;
import StaticManagers.FileManager;
import Util.MyMath;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 9/12/2015.
 */
public class Projectile extends Entity {

    private float vx;
    private float vy;
    private float accel;
    public float explodeRadius;
    public float damage;

    public Player shooter;

    public Projectile(Player _shooter) {

        super(_shooter.x, _shooter.y, _shooter.orient + MyMath.random(-_shooter.weapon.spread * 0.5f, _shooter.weapon.spread * 0.5f), Color.MAGENTA);

        shooter = _shooter;

        sprite = FileManager.getImage("bullet.png");
        setBounds(sprite.getWidth() * 4, sprite.getHeight() * 2);

        float cosAngle = (float)Math.cos(orient);
        float sinAngle = (float)Math.sin(orient);

        this.x += Weapon.ORIGIN_RADIUS * cosAngle;
        this.y += Weapon.ORIGIN_RADIUS * sinAngle;

        vx = _shooter.weapon.i_speed * cosAngle;
        vy = _shooter.weapon.i_speed * sinAngle;

        accel = _shooter.weapon.accel;
        explodeRadius = _shooter.weapon.explodeRadius;
        damage = _shooter.weapon.damage;

    }

    public void move() {
        if (accel != 0) {
            vx += (float) Math.sin(orient) * accel;
            vy += (float) Math.cos(orient) * accel;
        }
        x += vx;
        y += vy;

        updateCollideBox();
    }

    public void handleHit(float damage, float angle) {
        // NA at the moment
    }

}
