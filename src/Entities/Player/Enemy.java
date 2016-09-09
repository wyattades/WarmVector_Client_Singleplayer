package Entities.Player;

import GameState.GameStateManager;
import Main.Game;
import UI.Map;
import Util.MyMath;

import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 1/24/2015.
 */
public class Enemy extends Player {

//    private int shootTime;
    private int idleTime, reactionTime;
    private ThisPlayer thisPlayer;
    private double ax, ay;

    public Enemy(GameStateManager _gsm, double _orient, Map _map, ThisPlayer _thisPlayer) {
        super(_gsm, _orient, _map);
        thisPlayer = _thisPlayer;
        setTopSpeed(1.0);

        life = maxLife = 25.0;

        // TODO: put these in constructor of enemy and thisPlayer
        shootSprite = (BufferedImage)gsm.assetManager.getAsset("player1g.png");
        defaultSprite = (BufferedImage)gsm.assetManager.getAsset("player1.png");
        setSpriteToDefault(weapon == null);
        setBounds(36, 36);
    }

    protected void updateSpecific() {
        normalBehavior(thisPlayer.x, thisPlayer.y, !thisPlayer.state);
    }

    private void normalBehavior(double px, double py, boolean playerDead) {
        shooting = false;
        if (!playerDead && distBetweenSq(px, py) < 160000.0 && lineOfSight(px, py)) {

            double angleBetween = Math.atan2(py - y, px - x);
            double deltaAngle = deltaAngle(angleBetween);

            setVelocity(0);
            if (distBetweenSq(px, py) > 10000.0) {
                goTowards(angleBetween, 2.0);
            }
            if (lookingAt(deltaAngle, 0.1)) {
                shooting = true;
                idleTime = Game.currentTimeMillis();
            } else {
                orientTo(deltaAngle, 0.09);
            }
        } else {
            if (Game.currentTimeMillis() - idleTime > 2000) {
                if (vx == 0) {
                    ax = MyMath.random(-0.2, 0.2);
                }
                if (vy == 0) {
                    ay = MyMath.random(-0.2, 0.2);
                }
            } else {
                if (vx == 0 && vy == 0) {
                    ax = Math.cos(orient);
                    ay = Math.sin(orient);
                } else {
                    ax = vx;
                    ay = vy;
                }
            }
            bounceForward();
        }
    }

    private void bounceForward() {
        moveX(ax);
        moveY(ay);

        double deltaAngle = deltaAngle(Math.atan2(vy, vx));
        if (!lookingAt(deltaAngle, 0.1)){
            orientTo(deltaAngle, 0.07);
        }
    }

    // TODO use dirX and dirY ???
    private void goTowards(double angleBetween, double speed) {
        vx = speed * Math.cos(angleBetween);
        vy = speed * Math.sin(angleBetween);
    }

    private void orientTo(double dAngle, double rate) {
        if (dAngle < 0.0) {
            changeAngle(rate);
        } else {
            changeAngle(-rate);
        }
    }

    private void changeAngle(double amount) {
        orient += amount;
        if (orient > MyMath.PI) orient -= MyMath.TWO_PI;
        else if (orient < -MyMath.PI) orient += MyMath.TWO_PI;
    }

    private double deltaAngle(double angleBetween) {
        double dAngle = orient - angleBetween;
        if (dAngle < -MyMath.PI) dAngle += MyMath.TWO_PI;
        else if (dAngle > MyMath.PI) dAngle -= MyMath.TWO_PI;
        return dAngle;
    }

    private double distBetweenSq(double px, double py) {
        px -= x;
        py -= y;
        return px * px + py * py;
    }

    private boolean lineOfSight(double px, double py) {
        Line2D sightLine = new Line2D.Double(x, y, px, py);
        for (Line2D w : map.walls) {
            if (w.intersectsLine(sightLine)) {
                return false;
            }
        }
        return true;
    }

    private boolean lookingAt(double dAngle, double tolerance) {
        return Math.abs(dAngle) < tolerance;
    }

}
