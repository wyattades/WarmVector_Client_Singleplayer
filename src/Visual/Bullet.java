package Visual;

import Entity.Entity;
import Entity.Player;
import Main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Wyatt on 1/28/2015.
 */
public class Bullet {

    private double ix, iy, fx, fy;
    public double orient;
    private long displayTime;
    public boolean state;
    private static final double gunLength = 50;
    private Color fill;
    public ArrayList<CollidePoint> collidePoints;
    private ArrayList<TestPoint> testPoints;

    public class TestPoint {

        public double dist;
        public Entity e;

        public TestPoint(double dist, Entity e) {
            this.dist = dist;
            this.e = e;
        }
    }

    public class CollidePoint {

        public Color hitColor;
        public int x, y;

        public CollidePoint(int x, int y, Color c) {
            this.x = x;
            this.y = y;
            hitColor = c;
        }
    }

    public Bullet(double x, double y, double orient, double spread, int damage, HashMap<String, ArrayList<Entity>> allEnts) {
        state = true;
        fill = new Color(255, 255, (int) Game.random(50, 220), 200);
        collidePoints = new ArrayList<CollidePoint>();
        testPoints = new ArrayList<TestPoint>();
        displayTime = System.currentTimeMillis();
        orient += Game.random(-spread, spread);
        this.orient = orient;
        double checkLine = 10000; //just a big number
        fx = x + checkLine * Math.cos(orient);
        fy = y + checkLine * Math.sin(orient);
        ix = x + gunLength * Math.cos(orient);
        iy = y + gunLength * Math.sin(orient);

        for (HashMap.Entry<String, ArrayList<Entity>> entry : allEnts.entrySet()) {
            for (Entity e : entry.getValue()) {
                if (e.collideBox.intersectsLine(ix, iy, fx, fy)) {
                    double dist = Math.sqrt((ix - e.x) * (ix - e.x) + (iy - e.y) * (iy - e.y)) - 20;
                    testPoints.add(new TestPoint(dist, e));
                }
            }
        }

        Collections.sort(testPoints, new Comparator<TestPoint>() {
            @Override
            public int compare(TestPoint p1, TestPoint p2) {
                return Double.compare(p1.dist, p2.dist);
            }
        });

        for (TestPoint p : testPoints) {
            collidePoints.add(new CollidePoint((int) (ix + (p.dist) * Math.cos(orient)), (int) (iy + (p.dist) * Math.sin(orient)), p.e.hitColor));
            if (!p.e.hit(damage)) {
                fx = ix + (p.dist) * Math.cos(orient);
                fy = iy + (p.dist) * Math.sin(orient);
                break;
            }
        }
    }

    public void draw(Graphics2D g, double px, double py) {
        int dx = Entity.dispPosX(ix, px);
        int dy = Entity.dispPosY(iy, py);
        g.setColor(fill);
        g.drawLine(dx, dy, (int) (dx + fx - ix), (int) (dy + fy - iy));
    }

    public void update() {
        updateDisplay();
    }

    void updateDisplay() {
        if (System.currentTimeMillis() - displayTime > 32) {
            state = false;
        }
    }

}
