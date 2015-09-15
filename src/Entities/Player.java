package Entities;

import Entities.Weapon.Weapon;
import Main.Game;
import Map.GeneratedEnclosure;
import Map.TileMap;

import java.awt.*;
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
    private int topSpeed = 5;
    public int shootTime;
    protected BufferedImage shootSprite, defaultSprite, deadSprite;
    protected GeneratedEnclosure ge;

    Player(int x, int y, float orient, Weapon weapon, GeneratedEnclosure ge) {
        super(x, y,  orient);
        this.ge = ge;
        this.weapon = weapon;
        hitColor = Color.red;
        vx = vy = 0;
        life = maxLife = 100.0f;
    }

    public void setSpriteToDefault(boolean bool) {
        if (bool) sprite = defaultSprite;
        else sprite = shootSprite;
    }

    public void stopMove() {
        vx = vy = 0;
    }

    public void updatePosition() {
        x += vx;
        y += vy;
        vx -= 0;
    }

    public boolean hit(int amount, float angle) {
        life -= amount;
        return false;
    }


    public void updateLife() {
        if (life < 0) {
            life = 0;
            state = false;
        }
    }

    public void update() {
        if (Math.abs(vx) > 0 && Math.abs(vy) > 0) {
            vx /= Math.sqrt(2);
            vy /= Math.sqrt(2);
        }
        updatePosition();
        updateLife();
        updateCollideBox();
    }

    public Weapon getWeapon() {
        Weapon w = weapon;
        w.x = x;
        w.y = y;
        w.orient = Game.random(0, 6.28f);
        return w;
    }

    public void updateVelX(float velX) {
        velX *= topSpeed;
        if ((velX > 0 && !collideTile(w / 2, 0, 0, h * 0.3f)) || (velX < 0 && !collideTile(-w / 2, 0, 0, h * 0.3f)))
            vx = velX;
    }

    public void updateVelY(float velY) {
        velY *= topSpeed;
        if ((velY > 0 && !collideTile(0, h / 2, w * 0.3f, 0)) || (velY < 0 && !collideTile(0, -h / 2, w * 0.3f, 0)))
            vy = velY;
    }

    protected boolean collideTile(float ax, float ay, float dx, float dy) {
        return inTile(x + ax, y + ay) || inTile(x + ax + dx, y + ay + dy) || inTile(x + ax - dx, y + ay - dy);
    }

    private boolean inTile(float x, float y) {
//        x = x / TileMap.tileSize;
//        y = y / TileMap.tileSize;

        //TEMP
        return false;
//        int tileType = tileMap.tileArray[Math.round(x)][Math.round(y)]; //NOTE: if Math.round() doesn't work, cast it int instead
//        return tileType == TileMap.SOLID || tileType == TileMap.WINDOW;
    }


    public void deathSequence() {
        weapon = null;
        sprite = deadSprite;
        //animation???
    }
}
