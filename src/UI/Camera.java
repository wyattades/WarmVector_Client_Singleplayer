package UI;

import Entities.Player.Player;
import GameState.PlayState;
import Main.Window;
import Util.Vector;

/**
 * Directory: WarmVector_Client_Singleplayer/Util/
 * Created by Wyatt on 9/5/2016.
 */
public class Camera {

    private static final double maxVel = 24.0,
                                scaleDivide = 1.0 / PlayState.SCALEFACTOR;
    private Player follower;
//    private double screenVelX, screenVelY;
    private double rotateOffsetX, rotateOffsetY;
    private Vector screenVel;

    public double displayX, displayY;

    public Camera(Player _follower) {
        follower = _follower;

        rotateOffsetX = rotateOffsetY = 0.0;
//        screenPosX = screenPosY = 0.0;
        screenVel = new Vector();
    }

    public void update(double mouseX, double mouseY) {

//        updateVelocity();

        updateRotation(mouseX, mouseY);

        displayX = screenVel.x + rotateOffsetX + Window.WIDTH * 0.5 * scaleDivide - follower.x;
        displayY = screenVel.y + rotateOffsetY + Window.HEIGHT * 0.5 * scaleDivide - follower.y;

    }

    private void updateVelocity() {


        final double maxSpeed = 40.0,
                accelRate = 1.0;

        if (follower.vx == 0 && follower.vy == 0) {
            double magSq = screenVel.magSq();
            if (magSq >= 0.2) {
                Vector accel = new Vector(screenVel);
                accel.setMag(accelRate);
                screenVel.sub(accel);
            } else if (magSq != 0.0){
                screenVel.set(0, 0);
            }
        } else {
            Vector accel = new Vector(follower.vx, follower.vy);
            accel.setMag(accelRate);
            screenVel.add(accel);
            screenVel.limit(maxSpeed);
        }


//        final double
//                posAccel = 0.6,
//                negAccel = 0.5;
//
//        if (follower.vx > 0.0) screenPosX = Math.min(screenPosX + posAccel, maxVel);
//        else if (follower.vx < 0.0) screenPosX = Math.max(screenPosX - posAccel, -maxVel);
//        else {
//            if (screenPosX >= 0.0) screenPosX = Math.max(screenPosX - negAccel, 0.0);
//            else if (screenPosX <= 0.0) screenPosX = Math.min(screenPosX + negAccel, 0.0);
//        }
//
//        if (follower.vy > 0.0) screenPosY = Math.min(screenPosY + posAccel, maxVel);
//        else if (follower.vy < 0.0) screenPosY = Math.max(screenPosY - posAccel, -maxVel);
//        else {
//            if (screenPosY >= 0.0) screenPosY = Math.max(screenPosY - negAccel, 0.0);
//            else if (screenPosY <= 0.0) screenPosY = Math.min(screenPosY + negAccel, 0.0);
//        }
    }

    private void updateRotation(double mouseX, double mouseY) {

        double dy = mouseY - Window.HEIGHT * 0.5 - screenVel.y,
                dx = mouseX - Window.WIDTH * 0.5 - screenVel.x;

        follower.orient = Math.atan2(dy, dx);

        final double maxRadius = 100.0;

        // TODO: get rid of this sqrt somehow?
//        double rotateRadius = -maxRadius * Math.sqrt(dx * dx + dy * dy) / (Game.WIDTH * 0.5);
//        double rotateRadius = -50.0;
        double rotateRadius = 0.0;

        rotateOffsetX = rotateRadius * Math.cos(follower.orient);
        rotateOffsetY = rotateRadius * Math.sin(follower.orient);
    }
}
