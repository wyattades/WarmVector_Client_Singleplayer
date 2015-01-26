package Entity;

import Default.Entity;

import java.awt.*;

/**
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Body extends Entity {

    double w,h,orient;
    Color hitColor;

    public Body(double x, double y, double w, double h, double orient) {
        super(x, y);
        this.w = w;
        this.h = h;
        this.orient = orient;
    }
}
