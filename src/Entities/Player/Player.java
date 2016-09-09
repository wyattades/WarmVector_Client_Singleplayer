package Entities.Player;

import Entities.Entity;
import Entities.Hittable;
import Entities.Projectile;
import Entities.Weapon;
import GameState.GameStateManager;
import Main.Game;
import UI.Map;
import Util.ImageFilter;
import Util.MyMath;
import javafx.scene.media.AudioClip;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Player extends Entity implements Hittable {

    public Weapon weapon;
    private boolean slowX, slowY;
    public double life;
    public double maxLife;
    protected double topSpeed, diagSpeed;
    public static final double acceleration = 1.0;
    public int shootTime;
    public boolean shooting, moving;
    protected BufferedImage shootSprite, defaultSprite, deadSprite;
    protected Map map;

    private AudioClip hitSound;
    private final BufferedImage[] HIT_ANIMATION;

    protected Player(GameStateManager _gsm, double _orient, Map _map) {
        super(_gsm, 0, 0, _orient);

        hitSound = (AudioClip)gsm.assetManager.getAsset("ric0.wav");

        // TODO: recolor this somewhere else (so it only runs once in entire program)
        HIT_ANIMATION = ImageFilter.recolorAnimation((BufferedImage[])gsm.assetManager.getAsset("hit_"), Color.RED);
        map = _map;
        slowX = slowY = true;
        shooting = moving = false;
        life = maxLife = 100.0;
        shootTime = Game.currentTimeMillis();
        setVelocity(0);
    }

    protected void setTopSpeed(double value) {
        topSpeed = value;
        diagSpeed = topSpeed / Math.sqrt(2.0);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);

        if (weapon != null) {
            AffineTransform oldTForm = g.getTransform();
            g.rotate(orient, x, y);
            g.drawImage(weapon.sprite, (int) (x - weapon.sprite_w2 + 26), (int) (y - weapon.sprite_h2 + 2), null);
            g.setTransform(oldTForm);
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

    public void moveX(double deltaV) {
        slowX = false;
        vx += deltaV;
        if (vx > topSpeed) vx = topSpeed;
        else if (vx < -topSpeed) vx = -topSpeed;
    }

    public void moveY(double deltaV) {
        slowY = false;
        vy += deltaV;
        if (vy > topSpeed) vy = topSpeed;
        else if (vy < -topSpeed) vy = -topSpeed;
    }

    public void updatePosition() {
        double a = 0.6;

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

    public void handleDamage(double damage) {
        life -= damage;
        if (life < 0.0) {
            life = 0.0;
            state = false;
        }
    }

    public boolean handleIndirectHit(Projectile p) {
        double distSq = (y - p.y) * (y - p.y) + (x - p.x) * (x - p.x);
        double radiusSq = p.explodeRadius * p.explodeRadius;
        if (distSq < radiusSq) {
            handleDamage(p.damage * distSq / radiusSq);
            return true;
        }
        return false;
    }

    public boolean handleDirectHit(Projectile p) {
        if (collideBox.intersects(p.collideBox)) {
            handleDamage(p.damage);
            gsm.audioManager.playSFX(hitSound);
            return true;
        } else {
            return false;
        }
    }

    public BufferedImage[] getHitAnimation() {
        return HIT_ANIMATION;
    }

    public int stepTime = Game.currentTimeMillis();

    public void update() {
        updateSpecific();
        updatePosition();
        updateCollideBox();
    }

    abstract protected void updateSpecific();

    public Weapon getWeaponForDrop() {
        double spawnDist = MyMath.random(10.0, 24.0);
        Weapon w = weapon.clone();
        w.x = x + spawnDist * Math.cos(MyMath.random(0.0, MyMath.TWO_PI));
        w.y = y + spawnDist * Math.sin(MyMath.random(0.0, MyMath.TWO_PI));
        w.orient = MyMath.random(0.0, MyMath.TWO_PI);
        return w;
    }

    private boolean obstacleX(double vx) {
        Rectangle2D futureX = new Rectangle2D.Double(x - w * 0.5 + vx, y - h * 0.5, w, h);
        return map.inverseRegion.intersects(futureX);
    }

    private boolean obstacleY(double vy) {
        Rectangle2D futureY = new Rectangle2D.Double(x - w * 0.5, y - h * 0.5 + vy, w, h);
        return map.inverseRegion.intersects(futureY);
    }

    public void deathSequence() {
        weapon = null;
        setVelocity(0);
        sprite = deadSprite;
        //animation???
    }

}
