package Entity;

import Entity.Weapon.Weapon;
import Main.Game;
import Manager.FileManager;
import Map.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Player extends Entity {

    public Weapon weapon;
    public float vx;
    public float vy;
    public float life;
    public float maxLife;
    public static final int topSpeed = 5;
    public int shootTime;
    ArrayList<Entity> tiles;
    private TileMap tileMap;
    protected BufferedImage shootSprite,defaultSprite;

    Player(int x, int y, int w, int h, float orient, Weapon weapon, TileMap tileMap, ArrayList<Entity> tiles) {
        super(x, y, w, h, orient);
        this.weapon = weapon;
        this.tiles = tiles;
        this.tileMap = tileMap;
        hitColor = Color.red;
        vx = vy = 0;
        life = maxLife = 100.0f;
    }

    public void setSpriteToDefault(boolean bool) {
        if (bool) sprite = defaultSprite;
        else sprite = shootSprite;
    }

//    public void updateWeapon() {
//        int length = 24;
//        weapon.x = (int)(x+length*Math.cos(orient));
//        weapon.y = (int)(y+length*Math.sin(orient));
//        weapon.orient = orient;
//    }

    public void stopMove() {
        vx = vy = 0;
    }

    void updatePosition() {
        x += vx;
        y += vy;
    }

    public boolean hit(int amount, float angle) {
        life -= amount;
        return false;
    }


    void updateLife() {
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
        w.orient = Game.random(0,6.28f);
        return w;
    }

    public void updateVelX(float velX) {
        if ((velX > 0 && !collideTile(w / 2, 0, 0, h * 0.3f)) || (velX < 0 && !collideTile(-w / 2, 0, 0, h * 0.3f)))
            vx = velX;
    }

    public void updateVelY(float velY) {
        if ((velY > 0 && !collideTile(0, h / 2, w * 0.3f, 0)) || (velY < 0 && !collideTile(0, -h / 2, w * 0.3f, 0)))
            vy = velY;
    }

    boolean collideTile(float ax, float ay, float dx, float dy) {
        return inTile(x + ax, y + ay) || inTile(x + ax + dx, y + ay + dy) || inTile(x + ax - dx, y + ay - dy);
    }

    boolean inTile(float x, float y) {
        x = x / TileMap.tileSize;
        y = y / TileMap.tileSize;
        int tileType = tileMap.tileArray[(int) x][(int) y];
        return tileType == TileMap.SOLID || tileType == TileMap.WINDOW;
    }


}
