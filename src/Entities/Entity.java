package Entities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Entity {

    public float x, y, w, h, sprite_w, sprite_h;
    public float orient;
    public Color hitColor;
    public BufferedImage sprite;
    public Rectangle2D collideBox;
    public boolean state;

    protected Entity(float x, float y, float orient) {
        collideBox = new Rectangle2D.Float(x, y, w, h);
        this.x = x;
        this.y = y;
        this.orient = orient;
        state = true;

        loadSprites();
        sprite_w = sprite.getWidth();
        sprite_h = sprite.getHeight();

        //Set base color as pink for easy debugging
        //because an entity's hitColor should never be pink
        hitColor = new Color(255, 0, 169);
    }

    protected abstract void loadSprites();

    public void draw(Graphics2D g) {
        if (orient != 0) {
            AffineTransform oldTForm = g.getTransform();
            g.rotate(orient, x, y);
            g.drawImage(sprite, Math.round(x - sprite_w / 2.0f), Math.round(y - sprite_h / 2.0f), null);
            g.setTransform(oldTForm);
        } else {
            g.drawImage(sprite, Math.round(x - sprite_w / 2.0f), Math.round(y - sprite_h / 2.0f), null);
        }
    }

    public void updateCollideBox() {
        collideBox.setFrame(x - w / 2, y - h / 2, w, h);
    }

    public abstract boolean hit(int damage, float angle);

}
