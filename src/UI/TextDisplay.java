package UI;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/UI/
 * Created by Wyatt on 9/2/2016.
 */
public class TextDisplay {

    protected final String text;
    private final Font font;

    private boolean widthSet;

    protected int x, y, w;
    protected Color color;

    public TextDisplay(String _text, double _x, double _y, Font _font) {
        text = _text;
        x = (int) _x;
        y = (int) _y;
        font = _font;
        color = ButtonC.COLOR_DEFAULT;
        widthSet = false;
    }

    public void draw(Graphics2D g) {
        g.setFont(font);

        if (!widthSet) {
            widthSet = true;
            w = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
        }

        g.setColor(color);
        g.drawString(text, x - w, y);
    }

}
