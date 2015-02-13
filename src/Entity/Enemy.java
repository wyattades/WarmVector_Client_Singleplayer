package Entity;

import Entity.Weapon.Weapon;
import Manager.FileManager;
import Map.TileMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Wyatt on 1/24/2015.
 */
public class Enemy extends Player {

    public boolean shooting;

    public Enemy(double x, double y, double w, double h, double orient, Weapon weapon, TileMap tileMap, ArrayList<Entity> tiles) {
        super(x, y, w, h, orient, weapon, tileMap, tiles);
        hitColor = Color.red;
        sprite = FileManager.PLAYER1;
        shooting = false;
    }
    
    public void normalBehavior(double px, double py) {
        stopMove();
        shooting = false;
        if (lineOfSight(px, py) && distBetween(px, py) < 400) {
            if (distBetween(px, py) > 100) {
                goTowards(px, py, (float) 1);
            }
            if (lookingAt(px, py, (float) 0.05)) {
                shooting = true;
            } else {
                orientTo(px, py, (float) 0.1);
            }
        } else {
            //patrol(1);
            if (!lookingAt((float) (x + vx), (float) (y + vy), (float) 0.06)) {
                orientTo((float) (x + vx), (float) (y + vy), (float) 0.12);
            }
        }

    }

    public void patrol(double speed) {
        if (vx == 0 && vy == 0) {
            double r = new Random().nextDouble();
            updateVelX(speed*Math.cos(r));
            updateVelY(speed*Math.sin(r));
        }
    }

    public void goTowards(double ix, double iy, double speed) {
        double a = angle_Between(ix,iy);
        updateVelX(speed*Math.cos(a));
        updateVelY(speed*Math.sin(a));
    }

    public void orientTo(double ix, double iy, double rate) {
        if (angle_Between(ix,iy) < 0) {
            orient += rate;
        } else {
            orient -= rate;
        }

    }

    public double angle_Between(double ix, double iy) {
        return orient-Math.atan2(ix-x,iy-y)+Math.PI/2;
    }

    public double distBetween(double ix, double iy) {
        ix -= x;
        iy -= y;
        return Math.sqrt(Math.pow(ix,2)+Math.pow(iy,2));
    }

    public boolean lineOfSight(double ix, double iy) {
        for (Entity entity : tiles) {
            Tile t = (Tile)entity;
            if (t.collideBox.intersectsLine(x, y, ix, iy) && t.kind == TileMap.SOLID) {
                return false;
            }
        }
        return true;
    }

    public boolean lookingAt(double ix, double iy, double tolerance) {
        if (Math.abs(angle_Between(ix, iy)) < tolerance) {
            return true;
        }
        return false;
    }

}
