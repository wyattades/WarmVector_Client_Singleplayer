package Entity;

import java.awt.*;

/**
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Entity {

    public double x,y,dx,dy,w,h,orient;
    public Color hitColor;

    public Entity(double x, double y, double w, double h, double orient) {
        this.x = x;
        this.y = y;

    }

    public Entity() {
    }

    public abstract void draw(Graphics2D g);

    public abstract void update();
}
