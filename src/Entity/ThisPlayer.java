package Entity;

import Entity.Weapon.Weapon;
import Map.TileMap;

/**
 * Created by Wyatt on 1/22/2015.
 */
public class ThisPlayer extends Player {

    long initTime;

    public ThisPlayer(double x, double y, double w, double h, double orient, Weapon weapon, TileMap tileMap) {
        super(x, y, w, h, orient, weapon, tileMap);
    }

    public void update() {
        updateLife();
        updatePosition();
    }

    private void updateAngle(double cursorx, double cursory) {
        orient = Math.atan2(cursory-dy, cursorx-dx);
    }
}
