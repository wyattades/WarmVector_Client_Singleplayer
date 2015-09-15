package Visual;

import java.awt.*;

/**
 * Created by wyatt on 8/18/2015.
 */
public class Slider {

    private int x;
    public int y;
    private int w;
    public int slider_x;
    public int current_option;
    public String[] options;
    public String text;
    private static final int sub_w = 60, size = 30;
    public boolean pressed;
    public int dragPos;
    public String name;

    public Slider(int x, int y, String text, String name, String[] options, int current_option) {
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

    public void draw(Graphics2D g) {

        //Slider line
        g.setColor(ThemeColors.textTitle);
        g.setStroke(new BasicStroke(4));
        g.drawLine(x - w, y, x, y);

        //Actual slider
        if (pressed) g.setColor(ThemeColors.buttonDefault);
        else g.setColor(ThemeColors.buttonSelected);
        g.fillRect(slider_x - size / 2, y - size / 2, size, size);

        //Text
        g.setFont(new Font("Dotum Bold", Font.BOLD, 20));
        g.setColor(ThemeColors.textOver);
        g.drawString(text, (int) (x - w - size - g.getFontMetrics().getStringBounds(text, g).getWidth()), y + 6);

        //Options
        g.setFont(new Font("Dotum Bold", Font.BOLD, 16));
        for (int i = 0; i < options.length; i++) {
            g.drawString(options[i], (int) (x - w + sub_w * i - g.getFontMetrics().getStringBounds(options[i], g).getWidth() / 2), y + size);
        }

    }

    public void snapSlider() {
        current_option = Math.round(((float) (slider_x - (x - w)) / (float) sub_w));

        slider_x = x - w + sub_w * current_option;
    }

    public void slide(int mx) {
        slider_x = mx;
        if (slider_x < x - w) slider_x = x - w;
        if (slider_x > x) slider_x = x;
    }

    public boolean overBox(int mx, int my) {
        return mx > slider_x - size / 2 && mx < slider_x + size / 2 &&
                my > y - size / 2 && my < y + size / 2;
    }

    public void setDragPos(int i_pos) {
        dragPos = i_pos - slider_x;
    }
}
