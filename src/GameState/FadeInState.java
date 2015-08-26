package GameState;

import Main.Game;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 8/20/2015.
 */
public class FadeInState extends GameState {

    private int opacity;

    private final int rate = 2;

    public boolean state;

    public FadeInState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        opacity = 255;
        state = true;
        gsm.gameStates[gsm.currentState].update();
    }

    @Override
    public void unload() {
        gsm.cursor.setMouseToCenter();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(0,0,0,opacity));
        g.fillRect(0, 0, Game.WIDTH,Game.HEIGHT);
    }

    @Override
    public void update() {
        opacity -= rate;
        if (opacity <= 0) {
            gsm.unloadState(GameStateManager.FADEIN);
        }

    }

    @Override
    public void inputHandle() {

    }

//    @Override
//    protected void setCursor() {
//    }
}
