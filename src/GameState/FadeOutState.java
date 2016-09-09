package GameState;

import Main.Window;
import Util.MyInputEvent;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 8/21/2015.
 */
public class FadeOutState extends GameState {

    private static final double RATE = 4.0;

    private double opacity;

    public FadeOutState(GameStateManager _gsm) {
        super(_gsm);
    }

    public void load() {}

    public void init() {
        opacity = 0;
    }

    public void unload() {
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, (int)Math.min(255.0, opacity)));
        g.fillRect(0, 0, Main.Window.WIDTH, Window.HEIGHT);
    }

    public void update(double deltaTime) {
        opacity += RATE;
        if (opacity > 255.0) {
            gsm.setState(GameStateManager.PLAY, GameStateManager.MAIN);
            gsm.setState(GameStateManager.FADEIN, GameStateManager.TOP);
        }
    }

    public void inputHandle(MyInputEvent event) {}

}
