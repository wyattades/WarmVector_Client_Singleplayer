package Entity;

import Main.WarmVector_Client_Singleplayer;

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

    public Bullet(double x, double y, double orient, double spread, int damage, HashMap<String,ArrayList<Entity>> allEnts) {
        state = true;
        displayTime = System.currentTimeMillis();
        orient += WarmVector_Client_Singleplayer.random(-spread,spread);
        double checkLine = 10000; //just a big number
        fx = checkLine*Math.cos(orient);
        fy = checkLine*Math.sin(orient);
        ix = x + 64*fx/checkLine;
        iy = y + 64*fy/checkLine;
        double length = checkLine;
        for(HashMap.Entry<String,ArrayList<Entity>> entry : allEnts.entrySet()) {
            for(Entity e : entry.getValue()) {
                if (e.collideBox.intersectsLine(ix, iy, fx, fy)) {
                    double dist = Math.sqrt((ix - fx) * (ix - fx) + (iy - fy) * (iy - fy));
                    if (dist < length) {
                        length = dist;
                        hitObject = e;
                    }
                }
            }
        }
        fx = x + length*Math.cos(orient);
        fy = y + length*Math.sin(orient);

        String type = hitObject.getClass().getName();
        if (type.equals("Player")) {
            hitObject.hit(damage);
        }
    }

    public void draw(Graphics2D g, double px, double py) {
        g.setColor(Color.yellow);
        g.drawLine(Entity.dispPosX(ix,px),Entity.dispPosY(iy,py),Entity.dispPosX(fx,px),Entity.dispPosY(fy,py));
    }

    public void update() {
        updateDisplay();
    }

    public void updateDisplay() {
        if (System.currentTimeMillis() - displayTime > 100) {
            state = false;
        }
    }


}
