package Entity;

import Main.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Entity {

    public int x,y,w,h;
    public float orient;
    public Color hitColor;
    public BufferedImage sprite;
    public Rectangle2D collideBox;
    public boolean state,transparent;

    protected Entity(int x, int y, int w, int h, float orient) {
        collideBox = new Rectangle2D.Double(x, y, w, h);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.orient = orient;
        state = true;
        transparent = false;
    }

    public void draw(Graphics2D g) {
        AffineTransform oldTForm = g.getTransform();
        if (orient != 0) g.rotate(orient, x, y);
        g.drawImage(sprite, x - w / 2, y - h / 2, null);
        g.setTransform(oldTForm);
    }

    public void updateCollideBox() {
        collideBox.setFrame(x - w / 2, y - h / 2, w, h);
    }

    public abstract boolean hit(int damage);
//
//    public static int dispPosX(double ix, double px) {
//        return (int) (ix - px + Game.WIDTH / 2);
//    }
//
//    public static int dispPosY(double iy, double py) {
//        return (int) (iy - py + Game.HEIGHT / 2);
//    }
}
