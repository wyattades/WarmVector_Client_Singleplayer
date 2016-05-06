package Visual;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/7/2015.
 */
public class ButtonC {

    private int x;
    public int y;
    private int w;
    private String text;

    private boolean setWidth;
    public boolean overBox;
    public int value;

    public static final int
            BEGIN = 0,
            CONTINUE = 1,
            OPTIONS = 2,
            HELP = 3,
            QUIT = 4,
            RESTART = 5,
            RESUME = 6,
            BACK = 7,
            CREDITS = 8,
            MAINMENU = 9;


    public ButtonC(String text, int value, int x, int y) {
        this.text = text;
        this.value = value;
        this.x = x;
        this.y = y;
        setWidth = false;
        overBox = false;
    }

    public void draw(Graphics2D g) {
        g.setFont(Theme.fontButton);

        if (!setWidth) {
            w = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
            setWidth = true;
        }

        if (!overBox) {
            g.setColor(Theme.buttonDefault);
        } else {
            g.setColor(Theme.buttonOver);
        }
        g.drawString(text, x - w, y + Theme.buttonHeight);
    }

    public void update(int x, int y) {
        overBox = overBox(x, y);
    }

    private boolean overBox(int mx, int my) {
        return mx > x - w && mx < x &&
                my > y && my < y + Theme.buttonHeight;
    }

}
