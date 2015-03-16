package GameState;

import Manager.InputManager;

import java.awt.*;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class PauseState extends GameState {
    public PauseState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

    }

    public void draw(Graphics2D g) {

    }

    public void update() {

    }

    public void inputHandle() {
        if (InputManager.isKeyPressed("ALT")) gsm.setPaused(false);
    }
}
