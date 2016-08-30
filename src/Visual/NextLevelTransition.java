package Visual;

import Main.Game;
import Util.MyMath;

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
    private int slowestIndex;

    public NextLevelTransition(Color color) {

        this.color = color;

        lengths = speeds = new float[128];

        float maxSpeed = 0.1f;

        float slowest = maxSpeed;
        for (int i = 0; i < speeds.length; i++) {
            speeds[i] = MyMath.random(0, maxSpeed);
            if (speeds[i] < slowest) {
                slowest = speeds[i];
                slowestIndex = i;
            }
        }
        barH = (int) Math.ceil((float) Game.HEIGHT / speeds.length);
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        //g.fillRect(Game.WIDTH - startX, 0, startX, Game.HEIGHT);
        for (int i = 0; i < lengths.length; i++) {
            g.fillRect(Game.WIDTH - (int) lengths[i], barH * i, (int) lengths[i], barH);
        }
    }

    public void transition() {
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] += speeds[i];
        }
        if (lengths[slowestIndex] > Game.WIDTH) {
            //next stage
        }
    }

}
