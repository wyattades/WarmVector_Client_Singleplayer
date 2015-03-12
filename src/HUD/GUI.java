package HUD;

import Entity.ThisPlayer;
import Main.Game;

/**
 * Created by Wyatt on 1/24/2015.
 */
public class GUI {

    public double screenVelX;
    public double screenVelY;
    private double maxVel;
    public double screenPosX, screenPosY;
    private ThisPlayer player;
    private static final double posAccel = 1, negAccel = 1;

    public GUI(ThisPlayer player) {
        screenVelX = screenVelY = screenPosY = screenVelY = 0;
        maxVel = 24;
        this.player = player;
    }

    public void updatePosition() {
        updateVelocity();
        screenPosX = screenVelX;
        screenPosY = screenVelY;
    }

    private void updateVelocity() {

        if (player.vx > 0) screenVelX = Math.min(screenVelX + posAccel, maxVel);
        else if (player.vx < 0) screenVelX = Math.max(screenVelX - posAccel, -maxVel);
        else {
            if (screenVelX > 0) screenVelX = Math.max(screenVelX - negAccel, 0);
            else if (screenVelX < 0) screenVelX = Math.min(screenVelX + negAccel, 0);
        }

        if (player.vy > 0) screenVelY = Math.min(screenVelY + posAccel, maxVel);
        else if (player.vy < 0) screenVelY = Math.max(screenVelY - posAccel, -maxVel);
        else {
            if (screenVelY > 0) screenVelY = Math.max(screenVelY - negAccel, 0);
            else if (screenVelY < 0) screenVelY = Math.min(screenVelY + negAccel, 0);
        }
    }

    public void updateRotation(double mouseX, double mouseY) {
        double rotateRadius = -70 * Math.sqrt((mouseX - player.dx) * (mouseX - player.dx) + (mouseY - player.dy) * (mouseY - player.dy)) / (Game.WIDTH / 2);
        screenPosX += rotateRadius * Math.cos(player.orient);
        screenPosY += rotateRadius * Math.sin(player.orient);
    }
}
