package Entity;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Entity {

    public int x, y, w, h;
    public float orient;
    public Color hitColor;
    public BufferedImage sprite;
    public Rectangle2D collideBox;
    public boolean state;

    protected Entity(int x, int y, int w, int h, float orient) {
        collideBox = new Rectangle2D.Double(x, y, w, h);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.orient = orient;
        state = true;
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

    public abstract boolean hit(int damage, float angle);

}
