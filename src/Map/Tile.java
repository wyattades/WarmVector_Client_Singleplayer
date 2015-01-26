package Map;

import Entity.Body;

/**
 * Created by Wyatt on 1/22/2015.
 */
public class Tile extends Body {

    double x,y;
    String kind;

    public Tile(double x, double y, double w, double h, double orient, String kind) {
        super(x,y,w,h,orient);
        this.x = x;
        this.y = y;
        this.kind = kind;
    }

}
