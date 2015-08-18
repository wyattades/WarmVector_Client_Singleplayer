package Visual;

import Main.Game;
import StaticManagers.FileManager;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/7/2015.
 */
public class MouseCursor {

    private final static float x_sensitivity = 1.5f, y_sensitivity = 1.5f;
    public static int NONE = 0, CURSOR = 1, CROSSHAIR = 2;
    public int x, y;
    private int w, h;
    private BufferedImage sprite, cursor, crosshair, transparent;

    public MouseCursor() {
        x = Game.WIDTH / 2 + 70;
        y = Game.HEIGHT / 2;
        cursor = FileManager.images.get("cursor.png");
        crosshair = FileManager.images.get("crosshair.png");
        transparent = new BufferedImage(2,2,BufferedImage.TYPE_3BYTE_BGR);
        sprite = transparent;
    }

    public void setSprite(int type) {
        if (type == CURSOR) sprite = cursor;
        else if (type == CROSSHAIR) sprite = crosshair;
        else if (type == NONE) sprite = transparent;
        w = sprite.getWidth();
        h = sprite.getHeight();
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite, x - (w / 2), y - (h / 2), null);
    }

    public void updatePosition(int deltaX, int deltaY) {
        x = constrain((int) (x + x_sensitivity * deltaX), 0, Game.WIDTH);
        y = constrain((int) (y + y_sensitivity * deltaY), 0, Game.HEIGHT);
    }

    public void setPosition(int new_x, int new_y) {
        x = constrain(new_x, 0, Game.WIDTH);
        y = constrain(new_y, 0, Game.HEIGHT);
    }

    private int constrain(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
}
