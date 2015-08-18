package GameState;

import Main.Game;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public abstract class GameState {

    protected GameStateManager gsm;

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
        setCursor();
    }

    protected abstract void init();

    public abstract void draw(Graphics2D g);

    public abstract void update();

    public abstract void inputHandle();

    protected abstract void setCursor();

    protected void drawBackground(Graphics2D g, Color color) {
        g.setColor(color);
        g.fillRect(0,0, Game.WIDTH,Game.HEIGHT);
    }

}
