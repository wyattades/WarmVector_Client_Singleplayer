package Entity;

import Entity.Weapon.Weapon;
import Main.Game;
import Manager.FileManager;
import Map.TileMap;

import java.util.ArrayList;

/**
 * Created by Wyatt on 1/22/2015.
 */
public class ThisPlayer extends Player {

    public ThisPlayer(int x, int y, int w, int h, float orient, Weapon weapon, TileMap tileMap, ArrayList<Entity> tiles) {
        super(x, y, w, h, orient, weapon, tileMap, tiles);
        sprite = FileManager.PLAYER0G;
        state = true;
    }

    public void updateAngle(float cursorx, float cursory) {
        //orient = Math.atan2(cursory - dy, cursorx - dx);
    }


}
