package HUD;

import Manager.InputManager;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/7/2015.
 */
public class ButtonC {

    private int x, y, w, h;
    private Color c1, c2;
    public String text;
    public boolean pressed;

    public ButtonC(String text, int x, int y, int w, int h, Color c1, Color c2) {
        pressed = false;
        this.text = text;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.c1 = c1;
        this.c2 = c2;
    }

    public void draw(Graphics2D g) {
        if (!overBox(InputManager.mouse.x,InputManager.mouse.y)) {
            g.setColor(c1);
            //g.setStroke(c2);
        } else {
            g.setColor(c2);
            pressed = InputManager.isMousePressed("LEFTMOUSE");
            //g.setStroke(c1);
        }
        g.fillRect(x - (w / 2), y - (h / 2), w, h);
        g.setColor(Color.black);
        g.drawString(text,x,y);

    }

    private boolean overBox(int mx, int my) {
        return mx > x - w / 2 && mx < x + w / 2 &&
                my > y - h / 2 && my < y + h / 2;
    }

}
