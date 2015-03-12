package Visual;

import Entity.Entity;
import Main.Game;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Wyatt on 3/8/2015.
 */
public class Shadow {

    private HashMap<String, ArrayList<Entity>> allEnts;
    private double x, y;
    private ArrayList<Point> boundaryPoints;
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

    //light source: (nx,ny)
    public void updatePosition(double nx, double ny) {
        x = nx;
        y = ny;
    }

    public void updateShadow() {
        shape = new Polygon();
        shape.addPoint((int) x, (int) y);
        shape.addPoint(0,0);
        shape.addPoint(0, Game.HEIGHT);
        shape.addPoint(Game.WIDTH,Game.HEIGHT);
        shape.addPoint(Game.WIDTH,0);
        for (int i = 0; i < 2 * Math.PI; i += 2 * Math.PI / 60) {
            Line2D ray = new Line2D.Double(x, y, 10000 * Math.cos(i), 10000 * Math.sin(i));
            ArrayList<Point2D> hitPoints = new ArrayList<Point2D>();
            for (HashMap.Entry<String, ArrayList<Entity>> entry : allEnts.entrySet()) {
                if (!entry.getKey().equals("thisPlayer")) {
                     for (Entity e : entry.getValue()) {
                        if (ray.intersects(e.collideBox)) {
                            ArrayList<Point2D> points = getIntersectionPoint(ray,e.collideBox);
                            for (Point2D p : points) {
                                if (p != null) {
                                    hitPoints.add(p);
                                }
                            }
                        }
                    }
                }
            }
            Collections.sort(hitPoints, new Comparator<Point2D>() {
                @Override
                public int compare(Point2D p1, Point2D p2) {
                    return Double.compare(Math.sqrt((x-p1.getX())*(x-p1.getX())+(y-p1.getY())*(y-p1.getY())),
                            Math.sqrt((x-p2.getX())*(x-p2.getX())+(y-p2.getY())*(y-p2.getY())) );
                }
            });
            Point2D vertex = hitPoints.get(0);
            shape.addPoint((int)vertex.getX(),(int)vertex.getY());
        }
        shape.addPoint((int)x,(int)y);//idk if this is necessary
    }

    public ArrayList<Point2D> getIntersectionPoint(Line2D line, Rectangle2D rectangle) {

        ArrayList<Point2D> p = new ArrayList<Point2D>();

        // Top line
        p.add(getIntersectionPoint(line,
                new Line2D.Double(
                        rectangle.getX() - rectangle.getWidth()/2,
                        rectangle.getY() - rectangle.getHeight()/2,
                        rectangle.getX() + rectangle.getWidth()/2,
                        rectangle.getY() + rectangle.getHeight()/2)));
        // Bottom line
        p.add(getIntersectionPoint(line,
                new Line2D.Double(
                        rectangle.getX() - rectangle.getWidth()/2,
                        rectangle.getY() + rectangle.getHeight()/2,
                        rectangle.getX() + rectangle.getWidth()/2,
                        rectangle.getY() - rectangle.getHeight()/2)));
        // Left side...
        p.add(getIntersectionPoint(line,
                new Line2D.Double(
                        rectangle.getX() + rectangle.getWidth()/2,
                        rectangle.getY() - rectangle.getHeight()/2,
                        rectangle.getX() - rectangle.getWidth()/2,
                        rectangle.getY() + rectangle.getHeight()/2)));
        // Right side
        p.add(getIntersectionPoint(line,
                new Line2D.Double(
                        rectangle.getX() + rectangle.getWidth()/2,
                        rectangle.getY() + rectangle.getHeight()/2,
                        rectangle.getX() - rectangle.getWidth()/2,
                        rectangle.getY() - rectangle.getHeight()/2)));

        return p;

    }

    public Point2D getIntersectionPoint(Line2D lineA, Line2D lineB) {

        double x1 = lineA.getX1();
        double y1 = lineA.getY1();
        double x2 = lineA.getX2();
        double y2 = lineA.getY2();

        double x3 = lineB.getX1();
        double y3 = lineB.getY1();
        double x4 = lineB.getX2();
        double y4 = lineB.getY2();

        Point2D p = null;

        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d != 0) {
            double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
            double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

            p = new Point2D.Double(xi, yi);

        }
        return p;
    }
}
