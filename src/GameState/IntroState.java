package GameState;


import Main.Game;
import Manager.InputManager;
import Visual.Animation;
import java.awt.*;


/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class IntroState extends GameState {

    Animation intro;

    public IntroState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        intro = new Animation(Game.WIDTH/2, Game.HEIGHT/2, 0, 40, Color.white, "intro_");
    }

    public void draw(Graphics2D g) {
        intro.draw(g);
    }

    public void update() {
        intro.update();
        if (!intro.state) gsm.setState(GameStateManager.MENU);
    }

    public void inputHandle() {
        if (InputManager.isMousePressed("LEFTMOUSE")) {
            gsm.setState(GameStateManager.MENU);
        }
    }

}
