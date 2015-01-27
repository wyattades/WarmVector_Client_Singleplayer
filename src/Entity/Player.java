package Entity;

import Entity.Weapon.Weapon;
import Map.TileMap;

import java.awt.*;

/**
 * Created by Wyatt on 1/22/2015.
 */
public class Player extends Entity {

    public Weapon weapon;
    double vx,vy,life;
    public static final int topSpeed = 4;
    TileMap tileMap;

    public Player(double x, double y, double w, double h, double orient, Weapon weapon, TileMap tileMap) {
        super(x,y,w,h,orient);
        this.weapon = weapon;
        this.tileMap = tileMap;
        vx = vy = 0;
        life = 100.0;
    }

    public void updatePosition() {
        x += vx;
        y += vy;
    }

    public void updateLife() {
        if (life < 0) life = 0;
    }

    public void update() {

    }

    public void draw(Graphics2D g) {}

    public void updateVelX(double velX) {
        if ((velX > 0 && !collideTile(w/2, 0, 0, h*0.3)) || (velX < 0  && !collideTile(-w/2, 0, 0,h*0.3))) vx = velX;
    }

    public void updateVelY(double velY) {
        if ((velY > 0 && !collideTile(0, h/2,h*0.3, 0)) || (velY < 0  && !collideTile(0, -h/2,w*0.3, 0))) vy = velY;
    }

    boolean collideTile(double ax, double ay, double dx, double dy) {
        if (inTile(x+ax, y+ay) || inTile(x+ax+dx, y+ay+dy) || inTile(x+ax-dx, y+ay-dy)) return true;
        return false;
    }

    boolean inTile(double x, double y) {
        x = x/ tileMap.tileSize;
        y = y/tileMap.tileSize;
        if (tileMap.mapArray[(int)x][(int)y] != TileMap.EMPTY) return true;
        return false;
    }
}
