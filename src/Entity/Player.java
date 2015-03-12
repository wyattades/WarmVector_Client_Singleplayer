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
    public double vx, vy, life;
    public static final int topSpeed = 6;
    public int shootTime;
    ArrayList<Entity> tiles;
    private TileMap tileMap;

    Player(double x, double y, double w, double h, double orient, Weapon weapon, TileMap tileMap, ArrayList<Entity> tiles) {
        super(x, y, w, h, orient);
        this.weapon = weapon;
        this.tiles = tiles;
        this.tileMap = tileMap;
        hitColor = Color.red;
        vx = vy = 0;
        sprite = FileManager.PLAYER1G;
        this.w = sprite.getWidth() * Game.SCALEFACTOR;
        this.h = sprite.getHeight() * Game.SCALEFACTOR;
        life = 100.0;
    }

    public void updateWeapon() {
        weapon.dx = dx;
        weapon.dy = dy;
        weapon.orient = orient;
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
        w.orient += Game.random(-0.5, 0.5);
        return w;
    }

    public void updateVelX(double velX) {
        if ((velX > 0 && !collideTile(w / 2, 0, 0, h * 0.3)) || (velX < 0 && !collideTile(-w / 2, 0, 0, h * 0.3)))
            vx = velX;
    }

    public void updateVelY(double velY) {
        if ((velY > 0 && !collideTile(0, h / 2, w * 0.3, 0)) || (velY < 0 && !collideTile(0, -h / 2, w * 0.3, 0)))
            vy = velY;
    }

    boolean collideTile(double ax, double ay, double dx, double dy) {
        return inTile(x + ax, y + ay) || inTile(x + ax + dx, y + ay + dy) || inTile(x + ax - dx, y + ay - dy);
    }

    boolean inTile(double x, double y) {
        x = x / TileMap.tileSize;
        y = y / TileMap.tileSize;
        int tileType = tileMap.tileArray[(int) x][(int) y];
        return tileType == TileMap.SOLID || tileType == TileMap.WINDOW;
    }


}
