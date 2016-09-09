package Entities;

import Entities.Player.Player;
import GameState.GameStateManager;
import Util.MyMath;
import javafx.scene.media.AudioClip;

import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 9/12/2015.
 */
public class Projectile extends Entity {

    private double accel;
    public double explodeRadius, damage;
    public AudioClip hitSound;

    public Player shooter;

    protected Projectile(GameStateManager _gsm, Player _shooter) {

        super(_gsm, _shooter.x, _shooter.y, _shooter.orient + MyMath.random(-_shooter.weapon.spread * 0.5, _shooter.weapon.spread * 0.5));

        shooter = _shooter;

        sprite = (BufferedImage)gsm.assetManager.getAsset(_shooter.weapon.bulletImage);
        setBounds(sprite.getWidth() * 4, sprite.getHeight() * 2);

        setVelocity(_shooter.weapon.i_speed);

        x += Weapon.ORIGIN_RADIUS * dirX;
        y += Weapon.ORIGIN_RADIUS * dirY;

        hitSound = _shooter.weapon.hitSound;
        accel = _shooter.weapon.accel;
        explodeRadius = _shooter.weapon.explodeRadius;
        damage = _shooter.weapon.damage;

    }

    public void move() {
        if (accel != 0) {
            vx += dirX * accel;
            vy += dirY * accel;
        }
        x += vx;
        y += vy;

        updateCollideBox();
    }

}
