package Visual;

import Main.Game;

import java.awt.*;
import java.util.Arrays;

/**
 * Directory: WarmVector_Client_Singleplayer/Visual/
 * Created by Wyatt on 8/18/2015.
 */
public class BarVisualizer {

    private Color color;
    private int[] bars;
    private int barH, startX;
    private int lastMousePos;
    private int pressedTime;

    public BarVisualizer(int x, Color color) {

        this.color = color;
        bars = new int[128];
        startX = x;
        Arrays.fill(bars, Game.WIDTH - startX);
        barH = (int) Math.ceil((float) Game.HEIGHT / bars.length);

    }

    public void draw(Graphics2D g) {

        g.setColor(color);

        g.fillRect(Game.WIDTH - startX, 0, startX, Game.HEIGHT);

        for (int i = 0; i < bars.length; i++) {
            g.fillRect(Game.WIDTH - startX - bars[i], barH * i, bars[i], barH);
        }

    }

    public void react(boolean mousePressed, int mouseY) {

        if (mousePressed) {

            int timeDiff = Game.currentTimeMillis() - pressedTime;
            for (int i = 0; i < bars.length; i++) {
                int diff = mouseY - i * barH;
                float addition = 100.0f - (float) Math.sin(diff * 0.25f) * 80.0f - timeDiff;
                if (addition < 0.0f) addition = 0.0f;
                bars[i] += addition;
            }

        } else {

            pressedTime = Game.currentTimeMillis();

        }

        float deltaY = (float)Math.abs(mouseY - lastMousePos);

        for (int i = 0; i < bars.length; i++) {
            float addition = 3.0f * deltaY - Math.abs(mouseY - i * barH);
            if (addition < 0) addition = 0;
            bars[i] += (addition - (float)bars[i] * 0.05f);
            if (bars[i] > Game.WIDTH-startX) bars[i] = Game.WIDTH-startX;
            if (bars[i] < 0) bars[i] = 0;
        }

        lastMousePos = mouseY;

    }

}
