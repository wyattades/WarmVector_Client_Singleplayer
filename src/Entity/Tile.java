package Entity;

import Map.TileMap;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public class Tile extends Entity {

    public int kind;

    public Tile(int x, int y, int kind) {
        super(x, y, TileMap.tileSize, TileMap.tileSize, 0);
        this.x = x;
        this.y = y;
        this.kind = kind;
        if (kind == TileMap.SOLID) hitColor = Color.black;
        else if (kind == TileMap.WINDOW) hitColor = new Color(90, 148, 255);
    }

    public boolean hit(int amount, float angle) {
        return kind == TileMap.WINDOW;
    }
}
