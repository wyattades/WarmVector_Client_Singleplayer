package HUD;

import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Wyatt on 3/7/2015.
 */
public class MouseCursor {

    public int x;
    public int y;
    private int px;
    private int py;
    private int w;
    private int h;
    private BufferedImage sprite;

    public MouseCursor(BufferedImage sprite) {
        this.sprite = sprite;
        x = px = Game.WIDTH / 2 + 70;
        y = py = Game.HEIGHT / 2;
        w = 32;
        h = 32;
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite, x - (w / 2), y - (h / 2), w, h, null);
    }

    //theres got to be a simpler way to do this \/
    public void updatePosition(int deltaX, int deltaY, int dx, int dy) {
        px += deltaX;
        py += deltaY;
        if (dx > Game.WIDTH) dx = Game.WIDTH;
        else if (dx < 0) dx = 0;
        if (dy > Game.HEIGHT) dy = Game.HEIGHT;
        else if (dy < 0) dy = 0;
        x = px + dx;
        y = py + dy;
        if (x > Game.WIDTH) x = Game.WIDTH;
        else if (x < 0) x = 0;
        if (y > Game.HEIGHT) y = Game.HEIGHT;
        else if (y < 0) y = 0;
    }
}
