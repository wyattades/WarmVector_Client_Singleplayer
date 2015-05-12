package GameState;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public abstract class GameState {

    GameStateManager gsm;

    GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    public abstract void init();

    public abstract void draw(Graphics2D g);

    public abstract void update();

    public abstract void inputHandle();

}
