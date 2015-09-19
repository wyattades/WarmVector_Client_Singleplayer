package Visual;

import Entities.Player;
import Main.Game;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/24/2015.
 */
public class ScreenMover {

    public float screenVelX, screenVelY, screenPosX, screenPosY;
    private float maxVel;
    private Player player;

    public ScreenMover(Player player) {
        screenVelX = screenVelY = 0;
        screenPosX = screenPosY = 0;
        maxVel = 24;
        this.player = player;
    }

    public void updatePosition() {
        updateVelocity();
        screenPosX = screenVelX;
        screenPosY = screenVelY;
    }

    private void updateVelocity() {

        float posAccel = 0.6f;
        float negAccel = 0.5f;
        if (player.vx > 0) screenVelX = Math.min(screenVelX + posAccel, maxVel);
        else if (player.vx < 0) screenVelX = Math.max(screenVelX - posAccel, -maxVel);
        else {
            if (screenVelX >= 0) screenVelX = Math.max(screenVelX - negAccel, 0);
            else if (screenVelX <= 0) screenVelX = Math.min(screenVelX + negAccel, 0);
        }

        if (player.vy > 0) screenVelY = (int) Math.min(screenVelY + posAccel, maxVel);
        else if (player.vy < 0) screenVelY = (int) Math.max(screenVelY - posAccel, -maxVel);
        else {
            if (screenVelY >= 0) screenVelY = (int) Math.max(screenVelY - negAccel, 0);
            else if (screenVelY <= 0) screenVelY = (int) Math.min(screenVelY + negAccel, 0);
        }
    }

    public void updateRotation(double mouseX, double mouseY) {
        int maxRadius = 100;
        float rotateRadius = (float) (-maxRadius * Math.sqrt((mouseX - Game.WIDTH / 2) * (mouseX - Game.WIDTH / 2) + (mouseY - Game.HEIGHT / 2) * (mouseY - Game.HEIGHT / 2)) / (Game.WIDTH / 2));
        //rotateRadius = -50;
        screenPosX += rotateRadius * Math.cos(player.orient);
        screenPosY += rotateRadius * Math.sin(player.orient);
    }
}
