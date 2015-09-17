package GameState;

import Main.Game;
import StaticManagers.InputManager;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 8/20/2015.
 */
public class FadeInState extends GameState {

    private int opacity;

    public boolean state;

    public FadeInState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        opacity = 255;
        state = true;
        gsm.cursor.setMouse(Game.WIDTH / 2 + 70, Game.HEIGHT / 2);
        gsm.cursor.setPosition(Game.WIDTH / 2 + 70, Game.HEIGHT / 2);

        gsm.gameStates[gsm.currentState].update();
    }

    @Override
    public void unload() {
        gsm.cursor.setMouse(Game.WIDTH / 2 + 70, Game.HEIGHT / 2);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, opacity));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
    }

    @Override
    public void update() {
        int rate = 3;
        opacity -= rate;
        if (opacity <= 2) {
            gsm.unloadState(GameStateManager.FADEIN);
        }

    }

    @Override
    public void inputHandle(InputManager inputManager) {

    }

}
