package Entities;

import Main.Game;
import Map.GeneratedEnclosure;
import StaticManagers.FileManager;

import java.awt.geom.Line2D;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/24/2015.
 */
public class Enemy extends Player {

    public boolean shooting;
    private int shootTime;

    public Enemy(float x, float y, float orient, GeneratedEnclosure ge) {
        super(x, y, orient, ge);
        shooting = false;

        life = maxLife = 25.0f;
    }

    @Override
    protected void loadSprites() {
        shootSprite = FileManager.getImage("player1g.png");
        defaultSprite = FileManager.getImage("player1.png");
        setSpriteToDefault(weapon == null);
    }

    public void normalBehavior(float px, float py, boolean playerDead) {
        //stopMove();
        shooting = false;
        if (!playerDead && lineOfSight(px, py) && distBetween(px, py) < 400) {
            resetVelocity();
            if (distBetween(px, py) > 100) {
                goTowards(px, py, (float) 1);
            }
            if (lookingAt(px, py, 0.05f)) {
                if (Game.currentTimeMillis() - shootTime > 400) {
                    shootTime = Game.currentTimeMillis();
                    shooting = true;
                } else {
                    shooting = false;

                }
            } else {
                orientTo(px, py, 0.1f);
            }
        } else {
//            patrol(2);
//            if (!lookingAt(x + 3*vx, y + 3*vy,  0.12f)) {
//                orientTo(x + 3*vx, y + 3*vy,  0.06f);
//            }
        }
    }

//    public void patrol(float speed) {
//        if (vx == 0 && vy == 0) {
//            float r = Game.random(0, 6.29f);
//            vx = speed * (float) Math.cos(r);
//            vy = speed * (float) Math.sin(r);
//        }
//        if ((vx < 0 && collideTile(-w / 2, 0, 0, h * 0.3f)) || (vx > 0 && collideTile(w / 2, 0, 0, h * 0.3f))) vx *= -1;
//        if ((vy > 0 && collideTile(0, h / 2, w * 0.3f, 0)) || (vy < 0 && collideTile(0, -h / 2, w * 0.3f, 0))) vy *= -1;
//    }

    void goTowards(float ix, float iy, float speed) {
        double a = angle_Between(ix, iy);
//        updateVelX(speed * (float) Math.cos(a));
//        updateVelY(speed * (float) Math.sin(a));
        vx = (speed * (float) Math.cos(a));
        vy = (speed * (float) Math.sin(a));
    }

    void orientTo(float ix, float iy, float rate) {
        if (angle_Between(ix, iy) < 0) {
            orient += rate;
        } else {
            orient -= rate;
        }

    }

    float angle_Between(float ix, float iy) {
        return orient + (float) Math.atan2(ix - x, iy - y) - (float) Math.PI / 2;
    }

    float distBetween(float ix, float iy) {
        ix -= x;
        iy -= y;
        return (float) Math.sqrt(ix * ix + iy * iy);
    }

    boolean lineOfSight(float ix, float iy) {
        for (Line2D w : map.walls) {
            if (w.intersectsLine(x, y, ix, iy)) {
                return false;
            }
        }
        return true;
    }

    boolean lookingAt(float ix, float iy, float tolerance) {
        return Math.abs(angle_Between(ix, iy)) < tolerance;
    }

}
