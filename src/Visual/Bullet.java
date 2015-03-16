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

    private int ix, iy, fx, fy;
    public float orient;
    private long displayTime;
    public boolean state;
    private static final int gunLength = 44;
    private Color fill;
    public ArrayList<CollidePoint> collidePoints;
    private ArrayList<TestPoint> testPoints;

    public class TestPoint {

        public float dist;
        public Entity e;

        public TestPoint(float dist, Entity e) {
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

    public Bullet(int x, int y, float orient, float spread, int damage, HashMap<String, ArrayList<Entity>> allEnts, Player shooter) {
        state = true;
        fill = new Color(255, 255, (int) Game.random(50, 220), 200);
        collidePoints = new ArrayList<CollidePoint>();
        testPoints = new ArrayList<TestPoint>();
        displayTime = System.currentTimeMillis();
        orient += Game.random(-spread, spread);
        this.orient = orient;
        int checkLine = 10000; //just a big number
        fx = (int) (x + checkLine * Math.cos(orient));
        fy = (int) (y + checkLine * Math.sin(orient));
        ix = x;
        iy = y;

        for (HashMap.Entry<String, ArrayList<Entity>> entry : allEnts.entrySet()) {
            for (Entity e : entry.getValue()) {
                if (!e.equals(shooter)) {
                    if (e.collideBox.intersectsLine(ix, iy, fx, fy)) {
                        float dist = (float) (Math.sqrt((ix - e.x) * (ix - e.x) + (iy - e.y) * (iy - e.y)) - 20);
                        testPoints.add(new TestPoint(dist, e));
                    }
                }
            }
        }

        Collections.sort(testPoints, new Comparator<TestPoint>() {
            @Override
            public int compare(TestPoint p1, TestPoint p2) {
                return Float.compare(p1.dist, p2.dist);
            }
        });

        for (TestPoint p : testPoints) {
            collidePoints.add(new CollidePoint((int) (ix + (p.dist) * Math.cos(orient)), (int) (iy + (p.dist) * Math.sin(orient)), p.e.hitColor));
            if (!p.e.hit(damage)) {
                fx = (int) (ix + (p.dist) * Math.cos(orient));
                fy = (int) (iy + (p.dist) * Math.sin(orient));
                break;
            }
        }
        ix =  ix + gunLength * (int)Math.cos(orient);
        iy =  iy + gunLength *(int) Math.sin(orient);
    }

    public void draw(Graphics2D g) {
        g.setColor(fill);
        g.drawLine(ix, iy, fx, fy);
    }

    public void update() {
        if (System.currentTimeMillis() - displayTime > 32) {
            state = false;
        }
    }

}
