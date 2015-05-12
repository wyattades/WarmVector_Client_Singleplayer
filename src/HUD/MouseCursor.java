package HUD;

import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/7/2015.
 */
public class MouseCursor {

    public int x;
    public int y;
    private int w;
    private int h;
    private BufferedImage sprite, cursor, crosshair;

    public MouseCursor(BufferedImage cursor, BufferedImage crosshair) {
        x = Game.WIDTH / 2 + 70;
        y = Game.HEIGHT / 2;
        this.cursor = cursor;
        this.crosshair = crosshair;
        setSpriteCursor(true);
    }

    public void setSpriteCursor(boolean bool) {
        if (bool) sprite = cursor;
        else sprite = crosshair;
        w = sprite.getWidth();
        h = sprite.getHeight();
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite, x - (w / 2), y - (h / 2), null);
    }


    public void updatePosition(int deltaX, int deltaY) {
        x = constrain(x+deltaX,0,Game.WIDTH);
        y = constrain(y+deltaY,0,Game.HEIGHT);
    }

    public void setPosition(int newx, int newy) {
        x = constrain(newx,0,Game.WIDTH);
        y = constrain(newy,0,Game.HEIGHT);
    }

    private int constrain(int value, int min, int max) { return Math.min(Math.max(value, min), max); }
}
