package Entity;

import Main.WarmVector_Client_Singleplayer;
import Manager.FileManager;
import Map.TileMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wyatt on 1/28/2015.
 */
public class Bullet {

    private double ix,iy,fx,fy;
    public double orient;
    private long displayTime;
    public Entity hitObject;
    public boolean state;
    private double gunLength = 18;
    private ArrayList<Animation> animations;
    public ArrayList<Point> collidePoints;

    public Bullet(double x, double y, double orient, double spread, int damage, HashMap<String,ArrayList<Entity>> allEnts, Player shooter) {
        state = true;
        collidePoints = new ArrayList<Point>();
        displayTime = System.currentTimeMillis();
        orient += random(-spread, spread);
        this.orient = orient;
        double checkLine = 10000; //just a big number
        fx = x + checkLine*Math.cos(orient);
        fy = y + checkLine*Math.sin(orient);
        ix = x + gunLength*Math.cos(orient);
        iy = y + gunLength*Math.sin(orient);
        double length = checkLine;
        for(HashMap.Entry<String,ArrayList<Entity>> entry : allEnts.entrySet()) {
            for(Entity e : entry.getValue()) {
                if (e.collideBox.intersectsLine(ix, iy, fx, fy) && !e.equals(shooter) ) {
                    double dist = Math.sqrt((ix - e.x) * (ix - e.x) + (iy - e.y) * (iy - e.y));
                    if (dist < length) {
                        hitObject = e;
                        if (!e.hit(damage)) {
                            length = dist;
                        } else {
                            collidePoints.add(new Point((int)(x + (length+hitObject.w/2+8)*Math.cos(orient)), (int)(y + (length+hitObject.h/2+8)*Math.sin(orient))));
                        }
                    }
                }
            }
        }
        fx = x + (length+hitObject.w/2+8)*Math.cos(orient);
        fy = y + (length+hitObject.h/2+8)*Math.sin(orient);

        hitObject.hit(damage);
        collidePoints.add(new Point((int)(x + (length+hitObject.w/2+8)*Math.cos(orient)), (int)(y + (length+hitObject.h/2+8)*Math.sin(orient))));
    }

    public void draw(Graphics2D g, double px, double py) {
        g.setColor(new Color(240, 240,0, 197));
        int dx = Entity.dispPosX(ix, px);
        int dy = Entity.dispPosY(iy, py);
        g.drawLine(dx,dy,(int)(dx+fx-ix),(int)(dy+fy-iy));
    }

    public void update() {
        updateDisplay();
    }

    public void updateDisplay() {
        if (System.currentTimeMillis() - displayTime > 60) {
            state = false;
        }
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }


}
