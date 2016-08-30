package Entities;

import Entities.Weapons.Weapon;
import Main.Game;
import Map.GeneratedEnclosure;
import Util.MyMath;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Player extends Entity {

    public Weapon weapon;
    protected float vx, vy;
    private boolean slowX, slowY;
    public float life;
    public float maxLife;
    protected float topSpeed, diagSpeed;
    public static final float acceleration = 1.0f;
    public int shootTime;
    public boolean shooting, moving;
    protected BufferedImage shootSprite, defaultSprite, deadSprite;
    protected GeneratedEnclosure map;

    Player(float _x, float _y, float _orient, GeneratedEnclosure _map) {
        super(_x, _y, _orient, Color.RED);
        map = _map;
        slowX = slowY = true;
        shooting = moving = false;
        life = maxLife = 100.0f;
        shootTime = Game.currentTimeMillis();
        resetVelocity();
    }

    protected void setTopSpeed(float value) {
        topSpeed = value;
        diagSpeed = topSpeed / (float) Math.sqrt(2);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);

        if (weapon != null) {
            AffineTransform oldTForm = g.getTransform();
            g.rotate(orient, x, y);
            g.drawImage(weapon.sprite, Math.round(x - weapon.sprite_w2 + 26), Math.round(y - weapon.sprite_h2 + 2), null);
            g.setTransform(oldTForm);
        }
    }

    public void reloadWeapon() {
        if (weapon != null) {
            int addedAmmo = Math.min(weapon.clipSize - weapon.ammo, weapon.reserveAmmo);
            weapon.ammo += addedAmmo;
            weapon.reserveAmmo -= addedAmmo;
        }
    }

    public void setWeapon(Weapon w) {
        weapon = w;
        if (w == null) {
            setSpriteToDefault(true);
        } else {
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

    public void moveX(float deltaV) {
        slowX = false;
        vx += deltaV;
        if (vx > topSpeed) vx = topSpeed;
        else if (vx < -topSpeed) vx = -topSpeed;
    }

    public void moveY(float deltaV) {
        slowY = false;
        vy += deltaV;
        if (vy > topSpeed) vy = topSpeed;
        else if (vy < -topSpeed) vy = -topSpeed;
    }

    public void updatePosition() {
        float a = 0.6f;

        moving = false;

        // Limit total speed to diagSpeed
        if (Math.abs(vx) > diagSpeed && Math.abs(vy) > diagSpeed) {
            if (vx > 0) vx = diagSpeed;
            else vx = -diagSpeed;
            if (vy > 0) vy = diagSpeed;
            else vy = -diagSpeed;
        }

        // If there is no obstacle horizontally
        if (!obstacleX(vx)) {
            // Apply friction on x velocity if not accelerating
            if (slowX) {
                if (vx > 0) {
                    vx = Math.max(vx - a, 0);
                } else if (vx < 0) {
                    vx = Math.min(vx + a, 0);
                }
            }
            slowX = true;
            if (vx != 0) moving = true;
            x += vx;
        } else {
            // Restrict horizontal movement
            vx = 0;
        }

        // If there is no obstacle vertically
        if (!obstacleY(vy)) {
            // Apply friction on y velocity if not accelerating
            if (slowY) {
                if (vy > 0) {
                    vy = Math.max(vy - a, 0);
                } else if (vy < 0) {
                    vy = Math.min(vy + a, 0);
                }
            }
            slowY = true;
            if (vy != 0) moving = true;
            y += vy;
        } else {
            // Restrict vertical movement
            vy = 0;
        }
    }

    public void handleHit(float damage, float angle) {
        life -= damage;
        if (life < 0) {
            life = 0;
            state = false;
        }
    }

    int stepTime = Game.currentTimeMillis();

    public void update() {
        updateSpecific();
        updatePosition();
        updateCollideBox();
    }

    abstract protected void updateSpecific();

    public Weapon getWeaponForDrop() {
        float spawnDist = MyMath.random(10, 24);
        Weapon w = weapon.clone();
        w.x = x + spawnDist * (float)Math.cos(MyMath.random(0, 6.29f));
        w.y = y + spawnDist * (float)Math.sin(MyMath.random(0, 6.29f));
        w.orient = MyMath.random(0,6.29f);
        return w;
    }

    private boolean obstacleX(float vx) {
        Rectangle2D futureX = new Rectangle2D.Float(x - w * 0.5f + vx, y - h * 0.5f, w, h);
        return map.inverseRegion.intersects(futureX);
    }

    private boolean obstacleY(float vy) {
        Rectangle2D futureY = new Rectangle2D.Float(x - w * 0.5f, y - h * 0.5f + vy, w, h);
        return map.inverseRegion.intersects(futureY);
    }

    public void deathSequence() {
        weapon = null;
        resetVelocity();
        sprite = deadSprite;
        //animation???
    }

}
