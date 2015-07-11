package Entity;

import Entity.Weapon.Weapon;
import Main.Game;
import Manager.FileManager;
import Map.TileMap;

import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public class ThisPlayer extends Player {

    public int hitTime;

    public ThisPlayer(int x, int y, int w, int h, float orient, Weapon weapon, TileMap tileMap, ArrayList<Entity> tiles) {
        super(x, y, w, h, orient, weapon, tileMap, tiles);
        sprite = shootSprite = FileManager.images.get("player0g.png");
        defaultSprite = FileManager.images.get("player0.png");
        this.w = sprite.getWidth();
        this.h = sprite.getHeight();
    }

    public void updateAngle(float cursorx, float cursory) {
        orient = (float) Math.atan2(cursory - Game.HEIGHT / 2, cursorx - Game.WIDTH / 2);
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
