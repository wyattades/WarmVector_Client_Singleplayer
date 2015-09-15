package Entities;

import Entities.Weapon.Weapon;
import Main.Game;
import Map.GeneratedEnclosure;
import StaticManagers.FileManager;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public class ThisPlayer extends Player {

    public int hitTime;

    public ThisPlayer(int x, int y, float orient, Weapon weapon, GeneratedEnclosure ge) {
        super(x, y, orient, weapon, ge);
        shootSprite = FileManager.images.get("player0g.png");
        defaultSprite = FileManager.images.get("player0.png");
        setSpriteToDefault(weapon == null);
        w = sprite.getWidth();
        h = sprite.getHeight();
        shootTime = Game.currentTimeMillis();
    }

    public void updateAngle(float cursor_x, float cursor_y) {
        orient = (float) Math.atan2(cursor_y - Game.HEIGHT / 2, cursor_x - Game.WIDTH / 2);
    }

    public boolean hit(int amount, float angle) {
        life -= amount;
        hitTime = Game.currentTimeMillis();
        return false;
    }

    public void regenHealth() {
        if (Game.currentTimeMillis() - hitTime > 2000) {
            life = (float) Math.min(maxLife, life + 0.04);
        }
    }

}
