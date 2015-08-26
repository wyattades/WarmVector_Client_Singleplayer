package Visual;

import Main.Game;
import StaticManagers.FileManager;
import StaticManagers.InputManager;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/7/2015.
 */
public class MouseCursor {

    private final static float x_sensitivity = 1.0f, y_sensitivity = 1.0f;
    public static int CURSOR = 1, CROSSHAIR = 2; //,NONE = 0;
    public int x, y;
    private int oldPosX, oldPosY;
    private int w, h;
    private BufferedImage sprite, cursor, crosshair;
    private Robot robot;

    public MouseCursor() {
        x = Game.WIDTH / 2 + 70;
        y = Game.HEIGHT / 2;

        cursor = FileManager.images.get("cursor.png");
        crosshair = FileManager.images.get("crosshair.png");
//        transparent = new BufferedImage(2,2,BufferedImage.TYPE_3BYTE_BGR);

        cursor = FileManager.images.get("cursor.png");;
        crosshair = FileManager.images.get("crosshair.png");

        sprite = cursor;

        w = sprite.getWidth();
        h = sprite.getHeight();

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void setSprite(int type) {
        if (type == CURSOR) sprite = cursor;
        else if (type == CROSSHAIR) sprite = crosshair;
//        else if (type == NONE) sprite = transparent;
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

    public void setMouseToCenter() {
        robot.mouseMove(Game.WIDTH / 2, Game.HEIGHT / 2);
    }

    private int constrain(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public void updateOldPos() {
        oldPosX = x;
        oldPosY = y;
    }

    public void setToOldPos() {
        x = oldPosX - InputManager.mouse.x + Game.WIDTH / 2;
        y = oldPosY - InputManager.mouse.y + Game.HEIGHT / 2;
    }
}
