package GameState;


import Main.Game;
import StaticManagers.InputManager;
import Visual.Animation;

import java.awt.*;


/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class IntroState extends GameState {

    private Animation intro;

    public IntroState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        intro = new Animation(Game.WIDTH / 2, Game.HEIGHT / 2, 0, 40, Color.white, "intro_");
        intro.w = Game.WIDTH;
        intro.h = Game.HEIGHT;
    }

    public void unload() {

    }

    public void draw(Graphics2D g) {
        intro.draw(g);
    }

    public void update() {

        intro.update();

        if (!intro.state) {
            nextState();
        }

    }

//    public void setCursor() {
//        gsm.cursor.setSprite(MouseCursor.NONE);
//    }

    public void inputHandle(InputManager inputManager) {
        if (inputManager.isMousePressed("LEFTMOUSE") || inputManager.isMouseClicked("LEFTMOUSE") || inputManager.isKeyPressed("SPACE")) {
            nextState();
        }
    }

    private void nextState() {
        gsm.setState(GameStateManager.MAINMENU);
    }

}
