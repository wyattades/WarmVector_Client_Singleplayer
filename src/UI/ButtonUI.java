package UI;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/7/2015.
 */
public class ButtonUI extends TextUI {

    public static final Color
            COLOR_DEFAULT = new Color(180, 180, 180, 220),
            COLOR_OVER = new Color(249, 249, 249);
    public static final int BUTTON_HEIGHT = (int) (50.0 * Main.Window.SCALE);
    public static final Font BUTTON_FONT = new Font("Dotum Bold", Font.BOLD, (int)(BUTTON_HEIGHT * 1.3));

    public boolean mouseOver;

    public enum ButtonType {
        NEWGAME,
        NEXTLEVEL,
        QUIT_CONFIRM,
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
    public final ButtonType type;

    public ButtonUI(String _text, double _x, double _y, Font _font, ButtonType _type) {
        super(_text, _x, _y, _font);

        type = _type;
        mouseOver = false;
    }

    public void setColor(Color newColor) {
        color = newColor;
    }

    public boolean mouseOver(int mx, int my) {
        mouseOver = mx > x - w && mx < x &&
                my > y - BUTTON_HEIGHT && my < y;

        return mouseOver;
    }

}
