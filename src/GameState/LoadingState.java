package GameState;

import Main.Window;
import UI.ButtonUI;
import Util.MyInputEvent;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 9/8/2016.
 * Last edited by Wyatt on 9/8/2016.
 */
public class LoadingState extends GameState {

    private static final int
            OFFSET = (int)(40.0 * Window.SCALE),
            STEP_RATE = 10;

    private static final String[] dots = {"", ".", "..", "..."};

    private int step, index;

    public LoadingState(GameStateManager _gsm) {
        super(_gsm);
    }

    public void load() {}

    public void init() {
        step = 0;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.white);
        g.setFont(ButtonUI.BUTTON_FONT);
        g.drawString("Loading" + dots[index], OFFSET, Window.HEIGHT - OFFSET);
        g.setColor(Color.black);
        g.drawRect(0, 0, Window.WIDTH, Window.HEIGHT);
    }

    public void update(double deltaTime) {
        step ++;
        if (step >= STEP_RATE) {
            step = 0;
            index = (index + 1) % dots.length;
        }
    }

    public void inputHandle(MyInputEvent event) {}

    public void unload() {}
}
