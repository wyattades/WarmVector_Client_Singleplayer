package Visual;

import Entities.Entity;
import Entities.Player;
import Main.Game;
import Map.GeneratedEnclosure;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/28/2015.
 */
public class Bullet {

    private float ix, iy, fx, fy;
    public float orient;
    private long displayTime;
    public boolean state;
    private Color fill;
    public ArrayList<CollidePoint> collidePoints;

    public class TestPoint {

        public float dist;
        public Entity e;
        public Color hitColor;

        public TestPoint(float dist, Color hitColor, Entity e) {
            this.dist = dist;
            this.e = e;
            this.hitColor = hitColor;
        }
    }

    public class CollidePoint {

        public Color hitColor;
        public float x, y;

        public CollidePoint(float x, float y, Color hitColor) {
            this.x = x;
            this.y = y;
            this.hitColor = hitColor;
        }
    }

    public Bullet(HashMap<String, ArrayList<Entity>> allEnts, GeneratedEnclosure map, Player shooter) {

        float offset = 14;

        state = true;
        fill = new Color(255, 255, (int) Game.random(50, 220), 200);
        collidePoints = new ArrayList<>();
        ArrayList<TestPoint> testPoints = new ArrayList<>();
        orient = shooter.orient + Game.random(-shooter.weapon.spread, shooter.weapon.spread);
        float checkLine = 10000; //just a big number
        fx = (float) (shooter.x + checkLine * Math.cos(orient));
        fy = (float) (shooter.y + checkLine * Math.sin(orient));
        ix = shooter.x + shooter.weapon.gunLength * (float) Math.cos(shooter.orient);
        iy = shooter.y + shooter.weapon.gunLength * (float) Math.sin(shooter.orient);

        //Create a line for more convenient calculation of intersection points
        Line2D intersector = new Line2D.Float(ix, iy, fx, fy);

        //Add all intersection points of entities to the list testPoints
        for (HashMap.Entry<String, ArrayList<Entity>> entry : allEnts.entrySet()) {
            for (Entity e : entry.getValue()) {
                if (!e.equals(shooter)) {
                    if (e.collideBox.intersectsLine(intersector)) {
                        float dist = (float) (Math.sqrt((ix - e.x) * (ix - e.x) + (iy - e.y) * (iy - e.y)) - offset);
                        testPoints.add(new TestPoint(dist, e.hitColor, e));
                    }
                }
            }
        }

        //Add all intersection points of walls to the list testPoints
        for (Line2D wall : map.walls) {
            if (wall.intersectsLine(intersector)) {
                Point2D intersection = getIntersectionPoint(wall, intersector);
                float dx = (float)(offset * Math.cos(orient));
                float dy = (float)(offset * Math.sin(orient));
                float dist = (float) (Math.sqrt((ix - intersection.getX() - dx) * (ix - intersection.getX() - dx) + (iy - intersection.getY() - dy) * (iy - intersection.getY() - dy)) - offset);
                testPoints.add(new TestPoint(dist, Theme.menuBackground, null));
            }
        }

        //Sort testPoints by distance from the starting point
        Collections.sort(testPoints, (p1, p2) -> Float.compare(p1.dist, p2.dist));

        for (TestPoint p : testPoints) {
            collidePoints.add(new CollidePoint((float) (ix + p.dist * Math.cos(orient)),(float) (iy + p.dist * Math.sin(orient)), p.hitColor));
            if (p.e == null || !p.e.hit(shooter.weapon.damage, orient)) {
                fx = (float) (ix + (p.dist) * Math.cos(orient));
                fy = (float) (iy + (p.dist) * Math.sin(orient));
                break;
            }
        }

        displayTime = Game.currentTimeMillis();

    }

    public void draw(Graphics2D g) {
        g.setColor(fill);
        g.drawLine(Math.round(ix), Math.round(iy), Math.round(fx), Math.round(fy));
    }

    public void update() {
        if (Game.currentTimeMillis() - displayTime > 16) {
            state = false;
        }
    }

    private Point2D getIntersectionPoint(Line2D line1, Line2D line2) {
        double px = line1.getX1(),
                py = line1.getY1(),
                rx = line1.getX2()-px,
                ry = line1.getY2()-py;
        double qx = line2.getX1(),
                qy = line2.getY1(),
                sx = line2.getX2()-qx,
                sy = line2.getY2()-qy;
        double det = sx*ry - sy*rx;
        double z = (sx*(qy-py)+sy*(px-qx))/det;
        return new Point2D.Float(
                (float)(px+z*rx), (float)(py+z*ry));

    }

}
