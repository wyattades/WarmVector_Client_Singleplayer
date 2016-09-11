package UI;

import Main.Window;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/UI/
 * Created by Wyatt on 9/2/2016.
 */
public class InteractiveWave {

    private Color color;
    private int[] barsX, barsY;
    private double[] barsV;
    private final int barH, equilibrium;
    private int lastMouseY;
    private boolean mousePressed;

    // "water" physics parameters
    private static final int
            AMOUNT = 32;
    private static final double
            tension = 0.007,
            damp = 0.015,
            spread = 0.1,
            splash = 200.0;


    public InteractiveWave(int _rightOffset, Color _color) {

        equilibrium = Window.WIDTH - _rightOffset;
        color = _color;
        mousePressed = false;

        barH = Window.HEIGHT / (AMOUNT - 2);

        barsX = new int[AMOUNT + 2];
        barsX[AMOUNT] = Window.WIDTH;
        barsX[AMOUNT + 1] = Window.WIDTH;

        barsY = new int[AMOUNT + 2];
        barsY[AMOUNT] = Window.HEIGHT;
        barsY[AMOUNT + 1] = 0;
        for (int i = 0; i < AMOUNT; i++) {
            barsY[i] = i * barH;
        }

        barsV = new double[AMOUNT];

    }

    public void init() {
        for (int i = 0; i < AMOUNT; i++) {
            barsX[i] = equilibrium;
            barsV[i] = 0;
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillPolygon(barsX, barsY, AMOUNT + 2);
    }

    public void update(int mouseY) {

        if (mousePressed) {
            mousePressed = false;

            barsV[mouseY / barH] = splash;
        }

        double mouseRate = Math.abs(mouseY - lastMouseY);
        barsV[mouseY / barH] -= mouseRate * 0.2;

        for (int i = 0; i < AMOUNT; i++) {
            barsV[i] += tension * (equilibrium - barsX[i]) - damp * barsV[i];
            barsX[i] += barsV[i];
        }

        double[] leftDeltas = new double[AMOUNT];
        double[] rightDeltas = new double[AMOUNT];

        for (int i = 0; i < AMOUNT; i++) {
            if (i > 0) {
                leftDeltas[i] = spread * (barsX[i] - barsX[i - 1]);
                barsV[i - 1] += leftDeltas[i];
            }
            if (i < AMOUNT - 1) {
                rightDeltas[i] = spread * (barsX[i] - barsX[i + 1]);
                barsV[i + 1] += rightDeltas[i];
            }
        }

        for (int i = 0; i < AMOUNT; i++) {
            if (i > 0) barsX[i - 1] += leftDeltas[i];
            if (i < AMOUNT - 1) barsX[i + 1] += rightDeltas[i];
        }

        lastMouseY = mouseY;//TODO don't need atm

    }

    public void reactClick() {
        mousePressed = true;
    }
}
