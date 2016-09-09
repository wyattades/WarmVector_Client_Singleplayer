package GameState;

import Util.MyInputEvent;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public abstract class GameState {

    protected GameStateManager gsm;

    public GameState(GameStateManager _gsm) {
        gsm = _gsm;
    }

    public abstract void load();

    public abstract void init();

    public abstract void draw(Graphics2D g);

    public abstract void update(double deltaTime);

    public abstract void inputHandle(MyInputEvent event);

    public abstract void unload();

}
