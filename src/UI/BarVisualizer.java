package UI;

import Main.Window;

import java.awt.*;
import java.util.Arrays;

/**
 * Directory: WarmVector_Client_Singleplayer/UI/
 * Created by Wyatt on 9/2/2016.
 */
public class BarVisualizer {

    private Color color;
    private int[] bars;
    private final int barH, startX;
    private int lastMouseY, mouseY;
    private boolean mousePressed;

    public BarVisualizer(int x, Color color) {

        this.color = color;
        bars = new int[128];
        startX = x;
        barH = (int) Math.ceil((double) Window.HEIGHT / bars.length);
        mousePressed = false;

    }

    public void init() {
        Arrays.fill(bars, Main.Window.WIDTH - startX);
    }

    public void draw(Graphics2D g) {

        g.setColor(color);

        g.fillRect(Window.WIDTH - startX, 0, startX, Window.HEIGHT);

        for (int i = 0; i < bars.length; i++) {
            g.fillRect(Window.WIDTH - startX - bars[i], barH * i, bars[i], barH);
        }

    }

    public void update() {

        if (mousePressed) {
            mousePressed = false;

            for (int i = 0; i < bars.length; i++) {
                int diff = mouseY - i * barH;
                double addition = 100.0 - Math.sin(diff * 0.25) * 80.0;
                if (addition < 0.0) addition = 0.0;
                bars[i] += addition;
            }

        }

        double deltaY = Math.abs(mouseY - lastMouseY);

        for (int i = 0; i < bars.length; i++) {
            double addition = 3.0 * deltaY - Math.abs(mouseY - i * barH);
            if (addition < 0) addition = 0;
            bars[i] += (addition - (double) bars[i] * 0.05);
            if (bars[i] > Window.WIDTH - startX) bars[i] = Window.WIDTH - startX;
            if (bars[i] < 0) bars[i] = 0;
        }

        lastMouseY = mouseY;

    }

    public void reactMove(int y) {
        mouseY = y;
    }

    public void reactClick() {
        mousePressed = true;
    }
}
