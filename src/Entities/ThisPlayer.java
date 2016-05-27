package Entities;

import Main.Game;
import Map.GeneratedEnclosure;
import StaticManagers.AudioManager;
import StaticManagers.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public class ThisPlayer extends Player {

    public int hitTime;

    public ThisPlayer(float x, float y,  GeneratedEnclosure ge) {super(x, y, 0, ge);}

    public void updateAngle(float cursor_x, float cursor_y) {
        orient = (float) Math.atan2(cursor_y - Game.HEIGHT / 2, cursor_x - Game.WIDTH / 2);
    }

    @Override
    protected void loadSprites() {
        shootSprite = FileManager.getImage("player0g.png");
        defaultSprite = FileManager.getImage("player0.png");
        setSpriteToDefault(weapon == null);
    }

    protected void updateSpecific() {
        //TODO: fix walking sounds
        if ((vx != 0 || vy != 0) && Game.currentTimeMillis() - stepTime > 500) {
            stepTime = Game.currentTimeMillis();
            AudioManager.playSFX(walkSound);
        }

        regenHealth();
    }

    private void regenHealth() {
        if (Game.currentTimeMillis() - hitTime > 2000) {
            life = (float) Math.min(maxLife, life + 0.04);
        }
    }

}
