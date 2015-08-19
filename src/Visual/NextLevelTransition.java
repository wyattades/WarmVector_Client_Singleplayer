package Visual;

import Main.Game;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/Visual/
 * Created by Wyatt on 8/18/2015.
 */
public class NextLevelTransition {

    private Color color;
    private float[] lengths;
    private float[] speeds;
    private int barH;

    public NextLevelTransition(Color color) {
        this.color = color;
        lengths = speeds = new float[128];
        for (int i = 0; i < speeds.length; i++) {
            speeds[i] = Game.random(0,.1f);
        }
        barH = (int) Math.ceil((float) Game.HEIGHT/128f);
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        //g.fillRect(Game.WIDTH - startX, 0, startX, Game.HEIGHT);
        for (int i = 0; i < lengths.length; i++) {
            g.fillRect(Game.WIDTH-(int)lengths[i], barH * i, (int)lengths[i], barH);
        }
    }

    public void transition() {
        for (int i = 0; i < lengths.length; i++) {
            if (lengths[i] < Game.WIDTH) lengths[i] += speeds[i];
        }
    }

}
