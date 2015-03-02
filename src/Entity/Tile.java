package Entity;

import Map.TileMap;

import java.awt.*;

/**
 * Created by Wyatt on 1/22/2015.
 */
public class Tile extends Entity {

    public int kind;

    public Tile(double x, double y, double w, double h, double orient, int kind) {
        super(x,y,w,h,orient);
        this.x = x;
        this.y = y;
        this.kind = kind;
        if (kind == TileMap.SOLID) hitColor = Color.black;
        if (kind == TileMap.WINDOW) hitColor = Color.blue;
    }

    public void update() {}

    public void draw(Graphics2D g) {}

    public boolean hit(int amount) {
        if (kind == TileMap.WINDOW) {
            return true;
        }
        return false;
    }
}
