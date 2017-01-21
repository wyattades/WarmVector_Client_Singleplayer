package UI;

import Main.Window;
import Util.MyMath;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/18/2015.
 */
public class SliderUI {

    private int x, y, w, slider_x;
    public int current_option;
    public String[] options;
    public String text;
    private static final Color
            COLOR_SELECTED = new Color(220, 220, 220);
    private static final int sub_w = (int)(60.0 * Main.Window.SCALE), size = (int)(30.0 * Window.SCALE);
    private static final Font textFont = new Font("Dotum Bold", Font.BOLD, (int)(20.0 * Window.SCALE)),
                                optionsFont = new Font("Dotum Bold", Font.BOLD, (int)(16.0 * Window.SCALE));

    public boolean pressed;
    public int dragPos;
    public String name;

    public SliderUI(int x, int y, String text, String name, String[] options, int current_option) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.name = name;
        w = sub_w * (options.length - 1);
        this.options = options;
        pressed = false;
        this.current_option = current_option;
        slider_x = x - w + sub_w * current_option;
    }

    public void set(int option) {
        slider_x = x - w + option * sub_w;
    }

    public void draw(Graphics2D g) {

        //Slider line
        g.setColor(ButtonUI.COLOR_DEFAULT);
        g.setStroke(new BasicStroke(4));
        g.drawLine(x - w, y, x, y);

        //Actual slider
        if (pressed) g.setColor(ButtonUI.COLOR_OVER);
        else g.setColor(COLOR_SELECTED);
        g.fillRect(slider_x - (int)(size * 0.5), y - (int)(size * 0.5), size, size);

        //Text
        g.setFont(textFont);
        g.setColor(ButtonUI.COLOR_DEFAULT);
        g.drawString(text, (int) (x - w - size - g.getFontMetrics().getStringBounds(text, g).getWidth()), y + 6);

        //Options
        g.setFont(optionsFont);
        for (int i = 0; i < options.length; i++) {
            g.drawString(options[i], (int) (x - w + sub_w * i - g.getFontMetrics().getStringBounds(options[i], g).getWidth() * 0.5), y + size);
        }

    }

    public void snapSlider() {
        current_option = MyMath.round(((double) (slider_x - (x - w)) / (double) sub_w));

        slider_x = x - w + sub_w * current_option;
    }

    public void slide(int mx) {
        slider_x = mx;
        if (slider_x < x - w) slider_x = x - w;
        if (slider_x > x) slider_x = x;
    }

    public boolean overBox(int mx, int my) {
        return mx > slider_x - size * 0.5 && mx < slider_x + size * 0.5 &&
                my > y - size * 0.5 && my < y + size * 0.5;
    }

    public void setDragPos(int i_pos) {
        dragPos = i_pos - slider_x;
    }
}
