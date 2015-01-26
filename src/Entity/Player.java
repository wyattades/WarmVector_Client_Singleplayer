package Entity;

import Default.Info;

/**
 * Created by Wyatt on 1/22/2015.
 */
public class Player extends Entity {

    Weapon weapon;
    double vx,vy,life;

    public Player(double x, double y, double w, double h, double orient, Weapon weapon) {
        super(x,y,w,h,orient);
        this.weapon = weapon;
        vx = vy = 0;
        life = 100.0;
    }

    public void updatePosition() {
        x += vx;
        y += vy;
    }

    void updateVelX(double velX) {
        if ((velX > 0 && !collideTile(w/2, 0, 0, h*0.3)) || (velX < 0  && !collideTile(-w/2, 0, 0,h*0.3))) vx = velX;
    }

    void updateVelY(double velY) {
        if ((velY > 0 && !collideTile(0, h/2,h*0.3, 0)) || (velY < 0  && !collideTile(0, -h/2,w*0.3, 0))) vy = velY;
    }

    boolean collideTile(double ax, double ay, double dx, double dy) {
        if (inTile(x+ax, y+ay) || inTile(x+ax+dx, y+ay+dy) || inTile(x+ax-dx, y+ay-dy)) return true;
        return false;
    }

    boolean inTile(double x, double y) {
        x = x/ Info.tileSize;
        y = y/Info.tileSize;
        if (tilesArray[int(x)][int(y)] != Info.TILE_EMPTY) return true;
        return false;
    }
}
