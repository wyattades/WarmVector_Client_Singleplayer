package Entities;

import StaticManagers.FileManager;
import Util.ImageUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Entity {

    protected BufferedImage sprite;
    protected float w, h, sprite_w2, sprite_h2;

    public float x, y;
    public float orient;
    public final BufferedImage[] hitAnimation;
    public Rectangle2D collideBox;
    public boolean state;

    protected Entity(float _x, float _y, float _orient, Color _hitColor) {
        x = _x;
        y = _y;
        orient = _orient;
        hitAnimation = ImageUtil.recolorAnimation(FileManager.getAnimation("hit_"), _hitColor);
        state = true;
    }

    protected void setBounds(float _w, float _h) {
        w = _w;
        h = _h;
        collideBox = new Rectangle2D.Float(x, y, w, h);
        sprite_w2 = sprite.getWidth() * 0.5f;
        sprite_h2 = sprite.getHeight() * 0.5f;
    }

    public void draw(Graphics2D g) {
        if (orient != 0) {
            AffineTransform oldTForm = g.getTransform();
            g.rotate(orient, x, y);
            g.drawImage(sprite, Math.round(x - sprite_w2), Math.round(y - sprite_h2), null);
            g.setTransform(oldTForm);
        } else {
            g.drawImage(sprite, Math.round(x - sprite_w2), Math.round(y - sprite_h2), null);
        }
    }

    public void updateCollideBox() {
        collideBox.setFrame(x - w * 0.5f, y - h * 0.5f, w, h);
    }

    abstract public void handleHit(float damage, float angle);
}
