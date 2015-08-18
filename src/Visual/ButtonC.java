package Visual;

import Main.Game;
import Manager.InputManager;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/7/2015.
 */
public class ButtonC {

    private int x;
    public int y;
    private int w;
    private int h;
    public String text;
    public boolean pressed;

    public ButtonC(String text, int x, int y, int w, int h) {
        pressed = false;
        this.text = text;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void draw(Graphics2D g) {
        if (!overBox(InputManager.mouse.x, InputManager.mouse.y)) {
            g.setColor(ThemeColors.buttonDefault);
            g.fillRect(x - (w / 2), y - (h / 2), w, h);
            g.setColor(ThemeColors.textDefault);
            g.drawString(text, x - (int) g.getFontMetrics().getStringBounds(text, g).getWidth() / 2, y+10);
        } else {
            g.setColor(ThemeColors.buttonOver);
            if (InputManager.isMousePressed("LEFTMOUSE") && Game.currentTimeMillis() - InputManager.getMouseTime("LEFTMOUSE") > 400) {
                InputManager.setMouseTime("LEFTMOUSE",Game.currentTimeMillis());
                pressed = true;
                g.setColor(ThemeColors.buttonSelected);
            }
            g.fillRect(x - (w / 2), y - (h / 2), w, h);
            g.setColor(ThemeColors.textOver);
            g.drawString(text, x - (int) g.getFontMetrics().getStringBounds(text, g).getWidth() / 2, y+10);

        }

        g.setColor(ThemeColors.outline);
        g.setStroke(new BasicStroke(2));
        g.drawRect(x - (w / 2), y - (h / 2), w, h);
        g.setStroke(new BasicStroke(1));
    }

    private boolean overBox(int mx, int my) {
        return mx > x - w / 2 && mx < x + w / 2 &&
                my > y - h / 2 && my < y + h / 2;
    }

}
