package GameState;

import Manager.GameStateManager;

import java.awt.*;

/**
 * Created by Wyatt on 1/25/2015.
 */
public abstract class GameState {

    public GameStateManager gsm;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    public abstract void init();
    public abstract void draw(Graphics2D g);
    public abstract void update();
    public abstract void inputHandle();

}
