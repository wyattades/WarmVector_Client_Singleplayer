package Entity;

import Entity.Weapon.Weapon;
import Map.Tile;
import Map.TileMap;

/**
 * Created by Wyatt on 1/24/2015.
 */
public class Enemy extends Player {

    public Enemy(double x, double y, double w, double h, double orient, Weapon weapon, TileMap tileMap) {
        super(x, y, w, h, orient, weapon, tileMap);
    }

    void patrol(float speed) {
        velocity.setMag(speed);
        if (collideTile(size.x/2, 0, size.x*0.3, 0) || collideTile(-size.x/2, 0, size.x*0.3, 0)) velocity.x *= -1;
        if (collideTile(0, size.y/2, 0, size.y*0.3) || collideTile(0, -size.y/2, 0, size.y*0.3)) velocity.y *= -1;
    }

    void goTowards(PVector pos, float speed) {
        velocity.set(0, 0, 0);
        PVector vel = distBetween(pos).get();
        vel.setMag(speed);
        updateVelX(vel.x);
        updateVelY(vel.y);
    }

    void orientTo(PVector pos, float speed) {
        if (angle_Between(pos) < 0) {
            orientation += speed;
        } else {
            orientation -= speed;
        }
    }

    float angle_Between(PVector pos) {
        PVector o = new PVector(1, 0);
        o.rotate(orientation);
        float a;
        //if (orientation) < 10) a = o.heading() + distBetween(pos).heading() + PI;
        a = orientation-distBetween(pos).heading()+TWO_PI;
        //else a = o.heading() - distBetween(pos).heading();
        return a;
    }

    PVector distBetween(PVector pos) {
        PVector d = pos.get();
        d.sub(position);
        return d;
    }

    boolean lineOfSight(PVector pos) {
        for (Tile t : tileMap.) {
            Tile t = tiles.get(i);
            if (t.collideBox.intersectsLine(x, y, pos.x, pos.y) && t.type == TileMap.SOLID) { //if bulletline intersects with solid tile
                return false;
            }
        }
        return true;
    }

    boolean lookingAt(PVector pos, float tolerance) {
        if (abs(angle_Between(pos)) < tolerance) {
            return true;
        }
        return false;
    }
}
