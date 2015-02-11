package Entity;

import Entity.Weapon.Weapon;
import Map.TileMap;

import java.util.ArrayList;

/**
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Player extends Entity {

    public Weapon weapon;
    public double vx,vy,life;
    public static final int topSpeed = 1;
    public int shootTime;
    ArrayList<Entity> tiles;
    TileMap tileMap;

    public Player(double x, double y, double w, double h, double orient, Weapon weapon, TileMap tileMap, ArrayList<Entity> tiles) {
        super(x,y,w,h,orient);
        this.weapon = weapon;
        this.tiles = tiles;
        this.tileMap = tileMap;
        vx = vy = 0;
        life = 100.0;
    }

    public void stopMove() {
        vx = vy = 0;
    }

    protected void updatePosition() {
        x += vx;
        y += vy;
    }

    public void hit(int amount) {
        life -= amount;
    }


    protected void updateLife() {
        if (life < 0) life = 0;
    }

    public void update() {
        updatePosition();
        updateLife();
        updateCollideBox();
    }

    public void updateVelX(double velX) {
        if ((velX > 0 && !collideTile(w/2, 0, 0, h*0.3)) || (velX < 0  && !collideTile(-w/2, 0, 0,h*0.3))) vx = velX;
    }

    public void updateVelY(double velY) {
        if ((velY > 0 && !collideTile(0, h/2,w*0.3, 0)) || (velY < 0  && !collideTile(0, -h/2,w*0.3, 0))) vy = velY;
    }

    protected boolean collideTile(double ax, double ay, double dx, double dy) {
        if (inTile(x+ax, y+ay) || inTile(x+ax+dx, y+ay+dy) || inTile(x+ax-dx, y+ay-dy)) return true;
        return false;
    }

    protected boolean inTile(double x, double y) {
        x = x/TileMap.tileSize;
        y = y/TileMap.tileSize;
        int tileType = 0;
        try{tileType= tileMap.tileArray[(int) x][(int) y];}
        catch(Exception e){}
        if (tileType == TileMap.SOLID || tileType == TileMap.WINDOW ) return true;
        return false;
    }
}
