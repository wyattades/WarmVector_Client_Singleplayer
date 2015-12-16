package GameState;

import Main.Game;
import StaticManagers.InputManager;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 8/21/2015.
 */
public class FadeOutState extends GameState {

    private int opacity;

    public boolean state;

    public FadeOutState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        opacity = 0;
        state = true;
        gsm.gameStates[gsm.currentState].update();
    }

    @Override
    public void unload() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, opacity));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

    }

    @Override
    public void update() {
        int rate = 3;
        opacity += rate;
        if (opacity >= 255) {
            gsm.unloadState(GameStateManager.FADEOUT);
            gsm.setState(GameStateManager.PLAY);
            gsm.setTopState(GameStateManager.FADEIN);
        }
    }

    @Override
    public void inputHandle(InputManager inputManager) {

    }

//    @Override
//    protected void setCursor() {
//    }
}
