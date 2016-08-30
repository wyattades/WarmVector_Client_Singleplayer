package Entities;

import Main.Game;
import Map.GeneratedEnclosure;
import StaticManagers.FileManager;
import Util.MyMath;

import java.awt.geom.Line2D;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 1/24/2015.
 */
public class Enemy extends Player {

//    private int shootTime;
    private int idleTime;
    private ThisPlayer thisPlayer;
    private float ax, ay;

    public Enemy(float _x, float _y, float _orient, GeneratedEnclosure _ge, ThisPlayer _thisPlayer) {
        super(_x, _y, _orient, _ge);
        thisPlayer = _thisPlayer;
        setTopSpeed(1.0f);

        life = maxLife = 25.0f;

        shootSprite = FileManager.getImage("player1g.png");
        defaultSprite = FileManager.getImage("player1.png");
        setSpriteToDefault(weapon == null);
        setBounds(36, 36);
    }

    protected void updateSpecific() {
        normalBehavior(thisPlayer.x, thisPlayer.y, !thisPlayer.state);
    }

    private void normalBehavior(float px, float py, boolean playerDead) {
        shooting = false;
        if (!playerDead && lineOfSight(px, py) && distBetweenSq(px, py) < 160000.0f) {

            float angleBetween = (float) Math.atan2(py - y, px - x);
            float deltaAngle = deltaAngle(angleBetween);

            resetVelocity();
            if (distBetweenSq(px, py) > 10000.0f) {
                goTowards(angleBetween, 2.0f);
            }
            if (lookingAt(deltaAngle, 0.1f)) {
//                if (Game.currentTimeMillis() - shootTime > 400) {
//                    shootTime = Game.currentTimeMillis();
                    shooting = true;
                idleTime = Game.currentTimeMillis();
//                }
            } else {
                orientTo(deltaAngle, 0.09f);
            }
        } else if (Game.currentTimeMillis() - idleTime > 1500) {
            patrol();
            float deltaAngle = deltaAngle((float) Math.atan2(vy, vx));
            if (!lookingAt(deltaAngle, 0.1f)){
                orientTo(deltaAngle, 0.07f);
            }
        }
    }

    public void patrol() {
//        if (vx == 0 && vy == 0) {
//            patrolAngle = MyMath.random(0, 6.29f);
//            ax = speed * (float) Math.cos(patrolAngle);
//            ay = speed * (float) Math.sin(patrolAngle);
//        }
        if (vx == 0) {
            ax = MyMath.random(-0.2f, 0.2f);
        }
        if (vy == 0) {
            ay = MyMath.random(-0.2f, 0.2f);
        }
        moveX(ax);
        moveY(ay);
    }

    private void goTowards(float angleBetween, float speed) {
        vx = (speed * (float) Math.cos(angleBetween));
        vy = (speed * (float) Math.sin(angleBetween));
    }

    private void orientTo(float dAngle, float rate) {
        if (dAngle < 0.0f) {
            changeAngle(rate);
        } else {
            changeAngle(-rate);
        }
    }

    private void changeAngle(float amount) {
        orient += amount;
        if (orient > MyMath.PI) orient -= MyMath.TWO_PI;
        else if (orient < -MyMath.PI) orient += MyMath.TWO_PI;
    }

    private float deltaAngle(float angleBetween) {
        float dAngle = orient - angleBetween;
        if (dAngle < -MyMath.PI) dAngle += MyMath.TWO_PI;
        else if (dAngle > MyMath.PI) dAngle -= MyMath.TWO_PI;
        return dAngle;
    }

    private float distBetweenSq(float px, float py) {
        px -= x;
        py -= y;
        return px * px + py * py;
    }

    private boolean lineOfSight(float px, float py) {
        Line2D sightLine = new Line2D.Float(x, y, px, py);
        for (Line2D w : map.walls) {
            if (w.intersectsLine(sightLine)) {
                return false;
            }
        }
        return true;
    }

    private boolean lookingAt(float dAngle, float tolerance) {
        return Math.abs(dAngle) < tolerance;
    }

}
