package Visual;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/18/2015.
 */
public class Slider {

    public int x;
    public int y;
    public int w;
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
        g.setColor(ButtonC.buttonDefault);
        g.setStroke(new BasicStroke(4));
        g.drawLine(x - w, y, x, y);

        //Actual slider
        if (pressed) g.setColor(ButtonC.buttonOver);
        else g.setColor(ButtonC.buttonSelected);
        g.fillRect(slider_x - (int)(size * 0.5f), y - (int)(size * 0.5f), size, size);

        //Text
        g.setFont(new Font("Dotum Bold", Font.BOLD, 20));
        g.setColor(ButtonC.buttonDefault);
        g.drawString(text, (int) (x - w - size - g.getFontMetrics().getStringBounds(text, g).getWidth()), y + 6);

        //Options
        g.setFont(new Font("Dotum Bold", Font.BOLD, 16));
        for (int i = 0; i < options.length; i++) {
            g.drawString(options[i], (int) (x - w + sub_w * i - g.getFontMetrics().getStringBounds(options[i], g).getWidth() * 0.5f), y + size);
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
        return mx > slider_x - size * 0.5f && mx < slider_x + size * 0.5f &&
                my > y - size * 0.5f && my < y + size * 0.5f;
    }

    public void setDragPos(int i_pos) {
        dragPos = i_pos - slider_x;
    }
}
