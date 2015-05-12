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

    public ThisPlayer(int x, int y, int w, int h, float orient, Weapon weapon, TileMap tileMap, ArrayList<Entity> tiles) {
        super(x, y, w, h, orient, weapon, tileMap, tiles);
        sprite = shootSprite = FileManager.PLAYER0G;
        defaultSprite = FileManager.PLAYER0;
        state = true;
    }

    public void updateAngle(float cursorx, float cursory) {
        orient = (float)Math.atan2(cursory - Game.HEIGHT/2, cursorx - Game.WIDTH/2);
    }

}
