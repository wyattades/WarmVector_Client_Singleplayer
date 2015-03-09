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

    public ThisPlayer(double x, double y, double w, double h, double orient, Weapon weapon, TileMap tileMap, ArrayList<Entity> tiles) {
        super(x, y, w, h, orient, weapon, tileMap, tiles);
        sprite = FileManager.PLAYER0G;
        dx = Game.WIDTH/2;
        dy = Game.HEIGHT/2;
        state = true;
    }

    public void updateAngle(double cursorx, double cursory) {
        orient = Math.atan2(cursory-dy, cursorx-dx);
    }

    public void updateDispPos(double px, double py) {
        dx = px;
        dy = py;
    }


}
