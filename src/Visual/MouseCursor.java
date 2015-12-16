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

    private final static float x_sensitivity = 1.0f, y_sensitivity = 1.0f;
    public static final int CURSOR = 1, CROSSHAIR = 2; //,NONE = 0;
    public int x, y;
    private int oldPosX, oldPosY;
    private int w, h;
    private BufferedImage sprite, cursor, crosshair;
    private Robot robot;

    public MouseCursor() {

        cursor = FileManager.getImage("cursor.png");
        crosshair = FileManager.getImage("crosshair.png");

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
        switch (type) {
            case CURSOR:
                sprite = cursor;
                break;
            case CROSSHAIR:
                sprite = crosshair;
                break;
            default:
                System.out.println("Crosshair name DNE");
                break;
        }
        w = sprite.getWidth();
        h = sprite.getHeight();
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite, x - (w / 2), y - (h / 2), null);
    }

//    public void updatePosition(int deltaX, int deltaY) {
//        x = constrain((int) (x + x_sensitivity * deltaX), 0, Game.WIDTH);
//        y = constrain((int) (y + y_sensitivity * deltaY), 0, Game.HEIGHT);
////        x = constrain(x + InputManager.mouse.dx, 0, Game.WIDTH);
////        y = constrain(y + InputManager.mouse.dy, 0, Game.HEIGHT);
//
//    }

    public void setPosition(int new_x, int new_y) {
        x = constrain(new_x, 0, Game.WIDTH);
        y = constrain(new_y, 0, Game.HEIGHT);
    }

    public void setMouse(int x, int y) {
        robot.mouseMove(x, y);
    }

    private int constrain(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public void updateOldPos() {
        oldPosX = x;
        oldPosY = y;
    }

    public void setToOldPos() {
        robot.mouseMove(oldPosX, oldPosY);
        x = oldPosX;
        y = oldPosY;
    }
}
