package Entities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Entity {

    public float x, y, w, h;
    public float orient;
    public Color hitColor;
    public BufferedImage sprite;
    public Rectangle2D collideBox;
    public boolean state;

    protected Entity(float x, float y, float orient) {
        collideBox = new Rectangle2D.Double(x, y, w, h);
        this.x = x;
        this.y = y;
        this.orient = orient;
        state = true;

        loadSprites();
        w = sprite.getWidth();
        h = sprite.getHeight();

        //Set color as pink for easy debugging (entity's hitColor should never be pink)
        hitColor = new Color(255, 0, 169);
    }

    protected abstract void loadSprites();

    public void draw(Graphics2D g) {
        AffineTransform oldTForm = g.getTransform();
        if (orient != 0) g.rotate(orient, x, y);
        g.drawImage(sprite, Math.round(x - w / 2), Math.round(y - h / 2), null);
        g.setTransform(oldTForm);
    }

    public void updateCollideBox() {
        collideBox.setFrame(x - w / 2, y - h / 2, w, h);
    }

    public abstract boolean hit(int damage, float angle);

}
