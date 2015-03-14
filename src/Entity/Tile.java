package Entity;

import Map.TileMap;

import java.awt.*;

/**
 * Created by Wyatt on 1/22/2015.
 */
public class Tile extends Entity {

    public int kind;

    public Tile(double x, double y, int kind) {
        super(x, y, (double) TileMap.tileSize, (double) TileMap.tileSize, (double) 0);
        this.x = x;
        this.y = y;
        this.kind = kind;
        if (kind == TileMap.SOLID) hitColor = Color.black;
        if (kind == TileMap.WINDOW) {
            hitColor = Color.blue;
            transparent = true;
        }
    }

    public void draw(Graphics2D g) {
    }

    public boolean hit(int amount) {
        return kind == TileMap.WINDOW;
    }
}
