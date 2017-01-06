package Entities;

import GameState.GameStateManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/Entities/
 * Created by Wyatt on 1/22/2015.
 */
public abstract class Entity {

    private int id;

    protected double w, h, dirX, dirY;

    public BufferedImage sprite;
    public double x, y, sprite_w2, sprite_h2, orient, vx, vy;
    public Rectangle2D collideBox;
    public boolean state;

    // TODO: static id generation is bad
    public static int genId = 0;

    protected GameStateManager gsm;

    protected Entity(GameStateManager _gsm, double _x, double _y, double _orient) {
        gsm = _gsm;
        x = _x;
        y = _y;
        setVelocity(0);
        orient = _orient;
        state = true;
        id = genId++;
    }

    protected void setBounds(double _w, double _h) {
        w = _w;
        h = _h;
        collideBox = new Rectangle2D.Double(x, y, w, h);
        sprite_w2 = sprite.getWidth() * 0.5;
        sprite_h2 = sprite.getHeight() * 0.5;
    }

    public void draw(Graphics2D g) {
        if (orient != 0) {
            AffineTransform oldTForm = g.getTransform();
            g.rotate(orient, x, y);
            g.drawImage(sprite, (int) (x - sprite_w2), (int) (y - sprite_h2), null);
            g.setTransform(oldTForm);
        } else {
            g.drawImage(sprite, (int) (x - sprite_w2), (int) (y - sprite_h2), null);
        }
    }

    public void setVelocity(double speed) {
        dirX = Math.cos(orient);
        dirY = Math.sin(orient);

        vx = dirX * speed;
        vy = dirY * speed;
    }

    public void updateCollideBox() {
        collideBox.setFrame(x - w * 0.5, y - h * 0.5, w, h);
    }

    public int getId() {
        return id;
    }

}
