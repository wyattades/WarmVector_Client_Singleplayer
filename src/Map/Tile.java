package Map;

import Entity.Entity;

import java.awt.*;

/**
 * Created by Wyatt on 1/22/2015.
 */
public class Tile extends Entity {

    double x,y;
    String kind;

    public Tile(double x, double y, double w, double h, double orient, String kind) {
        super(x,y,w,h,orient);
        this.x = x;
        this.y = y;
        this.kind = kind;
        hitColor = Color.BLACK;
    }

    public void draw(Graphics2D g) {

    }

    public void update() {

    }
}
