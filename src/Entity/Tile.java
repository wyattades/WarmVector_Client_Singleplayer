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


    public void update() {

    }

    public void draw(Graphics2D g) {
        g.setColor(hitColor);
        g.fillRect((int) (dx - w / 2), (int) (dy - h / 2), (int) w, (int) h);
        g.setColor(new Color(200,200,200));
        g.drawRect((int)(dx-w/2),(int)(dy-h/2),(int)w,(int)h);
    }

    public void hit(int amount) {


    }
}
