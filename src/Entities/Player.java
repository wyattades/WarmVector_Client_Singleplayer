package Entities;

import Entities.Weapons.Weapon;
import Helper.MyMath;
import Main.Game;
import Map.GeneratedEnclosure;
import StaticManagers.AudioManager;
import StaticManagers.FileManager;
import StaticManagers.OutputManager;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Player extends Entity {

    public Weapon weapon;
    public float vx, vy;
    public float life;
    public float maxLife;
    public static int topSpeed = 5;
    public int shootTime;
    protected BufferedImage shootSprite, defaultSprite, deadSprite;
    protected GeneratedEnclosure map;
    private boolean walking;
    private Clip walkSound;

    Player(float x, float y, float orient, GeneratedEnclosure map) {
        super(x, y,  orient);
        this.map = map;
        hitColor = Color.red;
        resetVelocity();
        life = maxLife = 100.0f;
        shootTime = Game.currentTimeMillis();
        w = h = 36;
        walkSound = FileManager.getSound("concrete1.wav");
    }

    public void setWeapon(Weapon w) {
        weapon = w;
        if (w == null) {
            setSpriteToDefault(true);
        } else {
            weapon.user = this;
            setSpriteToDefault(false);
        }
    }

    protected void setSpriteToDefault(boolean bool) {
        if (bool) sprite = defaultSprite;
        else sprite = shootSprite;
    }

    public void resetVelocity() {
        vx = 0;
        vy = 0;
    }

    private void slowPlayer() {

        float minSpeed = 0.3f;
        float reductionFactor = 0.4f;

//        if (Math.abs(vx) > minSpeed) {
//            vx *= reductionFactor;
//        } else {
//            vx = 0;
//        }
//
//        if (Math.abs(vy) > minSpeed) {
//            vy *= reductionFactor;
//        } else {
//            vy = 0;
//        }

        if (vx > minSpeed) {
            vx -= reductionFactor;
        } else if (vx < -minSpeed) {
            vx += reductionFactor;
        } else {
            vx = 0;
        }

        if (vy > minSpeed) {
            vy -= reductionFactor;
        } else if (vy < -minSpeed) {
            vy += reductionFactor;
        } else {
            vy = 0;
        }

    }

    public void updatePosition() {
        walking = false;
        if (!obstacleX(vx)) {
            x += vx;
            walking = true;
        }
        if (!obstacleY(vy)) {
            y += vy;
            walking = true;
        }
    }

    @Override
    public boolean hit(int amount, float angle) {
        life -= amount;
        if (life < 0) {
            life = 0;
            state = false;
        }
        return true;
    }

    int stepTime = 0;

    public void update() {

        updatePosition();
        slowPlayer();
        updateCollideBox();

        if (walking && Game.currentTimeMillis() - stepTime > 400) {
            stepTime = Game.currentTimeMillis();
            AudioManager.playSFX(walkSound);
        }

    }

    public Weapon getWeaponForDrop() {
        float spawnDist = MyMath.random(10, 24);
        Weapon w = weapon;
        w.x = x + spawnDist * (float)Math.cos(MyMath.random(0, 6.29f));
        w.y = y + spawnDist * (float)Math.sin(MyMath.random(0, 6.29f));
        w.orient = MyMath.random(0,6.29f);
        w.user = null;
        return w;
    }

    private boolean obstacleX(float vx) {

        Rectangle2D futureX = new Rectangle2D.Float(x - w/2 + vx, y - h/2, w, h);

//        boolean collidesX = false;
//
//        for (Line2D line : ge.walls) {
//            if (futureX.intersectsLine(line)) {
//                collidesX = true;
//            }
//        }
//
//        return collidesX;

        return map.inverseRegion.intersects(futureX);
    }

    private boolean obstacleY(float vy) {

        Rectangle2D futureY = new Rectangle2D.Float(x - w/2, y - h/2 + vy, w, h);

//        boolean collidesY = false;
//
//        for (Line2D line : ge.walls) {
//            if (futureY.intersectsLine(line)) {
//                collidesY = true;
//            }
//        }
//
//        return collidesY;

        return map.inverseRegion.intersects(futureY);

    }

    public void deathSequence() {
        weapon = null;
        resetVelocity();
        sprite = deadSprite;
        //animation???
    }

}
