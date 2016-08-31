package Visual;

import Main.Game;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/7/2015.
 */
public class ButtonC {

    public static Color buttonSelected = new Color(220, 220, 220),
            buttonDefault = new Color(180, 180, 180, 220),
            buttonOver = new Color(249, 249, 249),
            buttonOverOld = buttonOver;
    public static final Font BUTTON_FONT = new Font("Dotum Bold", Font.BOLD, (int) (60.0f * Game.SCALE));

    public static final int BUTTON_HEIGHT = (int) (50.0f * Game.SCALE);

    private int x;
    public int y;
    private int w;
    private String text;

    private boolean setWidth;
    public boolean overBox;
    public ButtonType value;

    public enum ButtonType {
        NEWGAME,
        NEXTLEVEL,
        CONTINUE,
        OPTIONS,
        HELP,
        QUIT,
        RESTART,
        RESUME,
        BACK,
        CREDITS,
        MAINMENU
    }


    public ButtonC(String text, ButtonType value, int x, int y) {
        this.text = text;
        this.value = value;
        this.x = x;
        this.y = y;
        setWidth = false;
        overBox = false;
    }

    public void draw(Graphics2D g) {
        g.setFont(BUTTON_FONT);

        if (!setWidth) {
            w = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
            setWidth = true;
        }

        if (!overBox) {
            g.setColor(buttonDefault);
        } else {
            g.setColor(buttonOver);
        }
        g.drawString(text, x - w, y + BUTTON_HEIGHT);
    }

    public void update(int x, int y) {
        overBox = overBox(x, y);
    }

    private boolean overBox(int mx, int my) {
        return mx > x - w && mx < x &&
                my > y && my < y + BUTTON_HEIGHT;
    }

}
