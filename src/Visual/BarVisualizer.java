package Visual;

import Main.Game;

import java.awt.*;

import com.sun.media.sound.FFT;

/**
 * Directory: WarmVector_Client_Singleplayer/Visual/
 * Created by Wyatt on 8/18/2015.
 */
public class BarVisualizer {

    private Color color;
    private int[] bars;
    private int barH,startX;
    private int lastMousePos;

    public BarVisualizer(int x, Color color) {
        this.color = color;
        bars = new int[128];
        startX = x;
        barH = (int) Math.ceil((float)Game.HEIGHT/128f);
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillRect(Game.WIDTH - startX, 0, startX, Game.HEIGHT);
        for (int i = 0; i < bars.length; i++) {
            g.fillRect(Game.WIDTH-startX-bars[i], barH * i, bars[i], barH);
        }
    }

    public void react(int mouseY) {
        int deltaY = Math.abs(mouseY - lastMousePos);
        for (int i = 0; i < bars.length; i++) {
//            float multiplier = -0.1f * bars[i];
//            if (deltaY > 3) {
//                multiplier += deltaY/4;
                int diff = Math.abs(mouseY - i * barH);
//                if (diff < 300) multiplier += 100/diff/deltaY;
//            }
//            bars[i] += multiplier;
//            if (diff > 300) diff = 0;
//            if (deltaY < 3) deltaY = 0;
            float addition = 3.0f * (float)deltaY - diff;
            if (addition < 0) addition = 0;
            bars[i] += (addition - (float)bars[i]/20.0f);
            if (bars[i] > Game.WIDTH-startX) bars[i] = Game.WIDTH-startX;
            if (bars[i] < 0) bars[i] = 0;
        }
        lastMousePos = mouseY;
    }


}
