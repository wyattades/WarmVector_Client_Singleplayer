package Entity;

import Main.WarmVector_Client_Singleplayer;
import Map.TileMap;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wyatt on 1/28/2015.
 */
public class Bullet {

    private double ix,iy,fx,fy;
    private long displayTime;
    private Entity hitObject;
    public boolean state;
    private double gunLength = 20;

    public Bullet(double x, double y, double orient, double spread, int damage, HashMap<String,ArrayList<Entity>> allEnts, Player shooter) {
        state = true;
        displayTime = System.currentTimeMillis();
        orient += random(-spread, spread);
        double checkLine = 10000; //just a big number
        fx = x + checkLine*Math.cos(orient);
        fy = y + checkLine*Math.sin(orient);
        ix = x + gunLength*Math.cos(orient);
        iy = y + gunLength*Math.sin(orient);
        double length = checkLine;
        for(HashMap.Entry<String,ArrayList<Entity>> entry : allEnts.entrySet()) {
            for(Entity e : entry.getValue()) {
                if (e.collideBox.intersectsLine(ix, iy, fx, fy) && !e.equals(shooter)) {
                    double dist = Math.sqrt((ix - e.x)*(ix - e.x) + (iy - e.y)*(iy - e.y));
                    if (dist < length) {
                        length = dist;
                        hitObject = e;
                    }
                }
            }
        }
        fx = x + (length+hitObject.w/2)*Math.cos(orient);
        fy = y + (length+hitObject.h/2)*Math.sin(orient);

        hitObject.hit(damage);
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
