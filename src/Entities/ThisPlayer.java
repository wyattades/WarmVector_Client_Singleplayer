package Entities;

import Main.Game;
import Map.GeneratedEnclosure;
import StaticManagers.AudioManager;
import StaticManagers.FileManager;
import Util.MyMath;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 1/22/2015.
 */
public class ThisPlayer extends Player {

    public int hitTime;

    public ThisPlayer(float _x, float _y, GeneratedEnclosure _ge) {
        super(_x, _y, 0, _ge);
        setTopSpeed(5.0f);

        shootSprite = FileManager.getImage("player0g.png");
        defaultSprite = FileManager.getImage("player0.png");
        setSpriteToDefault(weapon == null);
        setBounds(36, 36);

    }

    public void updateAngle(float cursor_x, float cursor_y) {
        orient = (float) Math.atan2(cursor_y - Game.HEIGHT * 0.5f, cursor_x - Game.WIDTH * 0.5f);
    }

    protected void updateSpecific() {
        //TODO: fix walking sounds
        if (moving && Game.currentTimeMillis() - stepTime > 300) {
            stepTime = Game.currentTimeMillis();
            AudioManager.playSFX("concrete" + (int)MyMath.random(1.0f, 7.0f) + ".wav");
        }

        regenHealth();
    }

    private void regenHealth() {
        if (Game.currentTimeMillis() - hitTime > 2000) {
            life = Math.min(maxLife, life + 0.1f);
        }
    }

}
