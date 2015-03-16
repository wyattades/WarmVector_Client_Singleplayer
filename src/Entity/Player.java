package Entity;

import Entity.Weapon.Weapon;
import Main.Game;
import Manager.FileManager;
import Map.TileMap;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Player extends Entity {

    public Weapon weapon;
    public float vx, vy, life;
    public static final int topSpeed = 6;
    public int shootTime;
    ArrayList<Entity> tiles;
    private TileMap tileMap;

    Player(int x, int y, int w, int h, float orient, Weapon weapon, TileMap tileMap, ArrayList<Entity> tiles) {
        super(x, y, w, h, orient);
        this.weapon = weapon;
        this.tiles = tiles;
        this.tileMap = tileMap;
        hitColor = Color.red;
        vx = vy = 0;
        sprite = FileManager.PLAYER1G;
        this.w = sprite.getWidth();
        this.h = sprite.getHeight();
        life = 50.0f;
    }

    public void updateWeapon() {
        weapon.x = x;
        weapon.y = y;
        weapon.orient = orient;
        //dont need this right?
    }

    public void stopMove() {
        vx = vy = 0;
    }

    void updatePosition() {
        x += vx;
        y += vy;
    }

    public boolean hit(int amount) {
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
