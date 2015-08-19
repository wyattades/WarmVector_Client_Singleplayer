package Visual;

import StaticManagers.InputManager;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/7/2015.
 */
public class ButtonC {

    private int x;
    public int y;
    private int w;
    private final int h = 43;
    public String text;

    public boolean setWidth;

    public ButtonC(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        setWidth = false;
    }

    public void draw(Graphics2D g) {
        g.setFont(new Font("Dotum Bold", Font.BOLD, 60));

        if (!setWidth) {
            w = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
//            h = (int) g.getFontMetrics().getStringBounds(text, g).getHeight();
            setWidth = true;
        }

        if (!overBox(InputManager.mouse.x,InputManager.mouse.y)) {
            g.setColor(ThemeColors.buttonDefault);
        } else {
            g.setColor(ThemeColors.buttonOver);
        }
        g.drawString(text, x - w, y + h);
    }

    public boolean overBox(int mx, int my) {
//        return mx > x - w / 2 && mx < x + w / 2 &&
//                my > y - h / 2 && my < y + h / 2;
        return  mx > x - w && mx < x &&
                my > y  && my < y + h;
    }

}
