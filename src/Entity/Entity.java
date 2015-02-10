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

    public double x,y,dx,dy,w,h,orient;
    public Color hitColor;
    protected BufferedImage sprite;
    public Rectangle2D collideBox;
    public boolean state;

    public Entity(double x, double y, double w, double h, double orient) {
        collideBox = new Rectangle2D.Double(x, y, w, h);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.orient = orient;
        state = true;
    }

    public Entity() {}

    public void draw(Graphics2D g){
        AffineTransform oldTForm = g.getTransform();
        if (orient != 0) g.rotate(orient, dx, dy);
        g.drawImage(sprite, (int)(dx-w/2),(int)(dy-h/2),(int)w,(int)h,null);
        g.setTransform(oldTForm);
    }

    public void updateDispPos(double px, double py) {
        dx = dispPosX(x,px);
        dy = dispPosY(y,py);
    }

    protected void updateCollideBox() {
        collideBox.setFrame(x, y, w, h);
    }

    public abstract void hit(int damage);

    public void update() {
        updateCollideBox();
    }

    public static int dispPosX(double ix, double px) {
        return (int)(ix - px + Game.WIDTH/2);
    }

    public static int dispPosY(double iy, double py) {
        return (int)(iy - py + Game.HEIGHT/2);
    }
}
