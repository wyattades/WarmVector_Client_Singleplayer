package GameState;

import Main.Game;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 8/21/2015.
 */
public class FadeOutState extends GameState {

    private int opacity;

    private final int rate = 2;

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
        g.setColor(new Color(0,0,0,opacity));
        g.fillRect(0, 0, Game.WIDTH,Game.HEIGHT);
    }

    @Override
    public void update() {
        opacity += rate;
        if (opacity >= 255) {
            gsm.unloadState(GameStateManager.FADEOUT);
            gsm.setState(GameStateManager.PLAY);
            gsm.setTopState(GameStateManager.FADEIN);
        }
    }

    @Override
    public void inputHandle() {

    }

    @Override
    protected void setCursor() {
    }
}
