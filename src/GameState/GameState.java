package GameState;

import Main.Game;
import StaticManagers.InputManager;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public abstract class GameState {

    protected GameStateManager gsm;
//    protected MouseCursor cursor;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
//        cursor = new MouseCursor(MouseCursor.CURSOR);
//        setCursor();
    }

    public abstract void init();

    public abstract void unload();

    public abstract void draw(Graphics2D g);

    public abstract void update();

    public abstract void inputHandle(InputManager inputManager);

    protected void drawBackground(Graphics2D g, Color color) {
        g.setColor(color);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
    }

}
