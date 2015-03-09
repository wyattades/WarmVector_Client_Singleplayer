package Visual;

import Entity.Entity;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wyatt on 3/8/2015.
 */
class Shadow {

    private HashMap<String, ArrayList<Entity>> allEnts;
    private double x, y;
    private ArrayList<Line2D> lines;
    private Color fill;
    private Polygon shape;

    public Shadow(double x, double y, HashMap<String, ArrayList<Entity>> allEnts) {
        this.allEnts = allEnts;
        this.x = x;
        this.y = y;
        fill = Color.black;
        shape = new Polygon();
    }

    public void draw(Graphics2D g) {
        g.setColor(fill);
        g.draw(shape);
    }

    public void updatePosition(double nx, double ny) {
        x = nx;
        y = ny;
    }

    public void updateShadow() {
        shape = new Polygon();
    }
}
