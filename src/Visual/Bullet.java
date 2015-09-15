package Visual;

import Entities.Entity;
import Entities.Player;
import Main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public Bullet(HashMap<String, ArrayList<Entity>> allEnts, Player shooter) {
        state = true;
        fill = new Color(255, 255, (int) Game.random(50, 220), 200);
        collidePoints = new ArrayList<CollidePoint>();
        ArrayList<TestPoint> testPoints = new ArrayList<TestPoint>();
        displayTime = Game.currentTimeMillis();
        orient = shooter.orient + Game.random(-shooter.getWeapon().spread, shooter.getWeapon().spread);
        float checkLine = 10000; //just a big number
        fx = (float) (shooter.getWeapon().x + checkLine * Math.cos(orient));
        fy = (float) (shooter.getWeapon().y + checkLine * Math.sin(orient));
        ix = shooter.getWeapon().x;
        iy = shooter.getWeapon().y;

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
            if (!p.e.hit(shooter.getWeapon().damage, orient)) {
                fx = (int) (ix + (p.dist) * Math.cos(orient));
                fy = (int) (iy + (p.dist) * Math.sin(orient));
                break;
            }
        }
        ix = ix + shooter.getWeapon().gunLength * (int) Math.cos(orient);
        iy = iy + shooter.getWeapon().gunLength * (int) Math.sin(orient);
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

}
