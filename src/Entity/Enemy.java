package Entity;

import Entity.Weapon.Weapon;
import Main.Game;
import Manager.FileManager;
import Map.TileMap;

import java.util.ArrayList;
import java.util.Random;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/24/2015.
 */
public class Enemy extends Player {

    public boolean shooting;

    public Enemy(int x, int y, int w, int h, float orient, Weapon weapon, TileMap tileMap, ArrayList<Entity> tiles) {
        super(x, y, w, h, orient, weapon, tileMap, tiles);
        shooting = false;
        sprite = shootSprite = FileManager.images.get("player1g.png");
        defaultSprite = FileManager.images.get("player1.png");
        this.w = sprite.getWidth();
        this.h = sprite.getHeight();
        life = maxLife = 25.0f;
    }

    public void normalBehavior(float px, float py) {
        //stopMove();
        shooting = false;
        if (lineOfSight(px, py) && distBetween(px, py) < 400) {
            stopMove();
            if (distBetween(px, py) > 100) {
                goTowards(px, py, (float) 1);
            }
            if (lookingAt(px, py, 0.05f)) {
                shooting = true;
            } else {
                orientTo(px, py, 0.1f);
            }
        } else {
//            patrol(2);
//            if (!lookingAt(x + 3*vx, y + 3*vy,  0.12f)) {
//                orientTo(x + 3*vx, y + 3*vy,  0.06f);
//            }
        }
        System.out.println(vx+" , "+vy);


    }

    public void patrol(float speed) {
        if (vx == 0 && vy == 0) {
            float r = Game.random(0,6.29f);
            vx =  speed*(float)Math.cos(r);
            vy =  speed*(float)Math.sin(r);
        }
        if ((vx < 0 && collideTile(-w / 2, 0, 0, h * 0.3f)) || (vx > 0 && collideTile(w / 2, 0, 0, h * 0.3f))) vx *= -1;
        if ((vy > 0 && collideTile(0, h / 2, w * 0.3f, 0)) || (vy < 0 && collideTile(0, -h / 2, w * 0.3f, 0))) vy *= -1;
    }

    void goTowards(float ix, float iy, float speed) {
        double a = angle_Between(ix, iy);
        updateVelX(speed * (float)Math.cos(a));
        updateVelY(speed * (float)Math.sin(a));
    }

    void orientTo(float ix, float iy, float rate) {
        if (angle_Between(ix, iy) < 0) {
            orient += rate;
        } else {
            orient -= rate;
        }

    }

    float angle_Between(float ix, float iy) {
        return orient+(float)Math.atan2(ix - x, iy - y)-(float)Math.PI/2;
    }

    float distBetween(float ix, float iy) {
        ix -= x;
        iy -= y;
        return (float) Math.sqrt(ix*ix + iy*iy);
    }

    boolean lineOfSight(float ix, float iy) {
        for (Entity entity : tiles) {
            Tile t = (Tile) entity;
            if (t.collideBox.intersectsLine(x, y, ix, iy) && t.kind == TileMap.SOLID) {
                return false;
            }
        }
        return true;
    }

    boolean lookingAt(float ix, float iy, float tolerance) {
        return Math.abs(angle_Between(ix, iy)) < tolerance;
    }

}
