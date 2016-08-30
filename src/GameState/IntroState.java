package GameState;


import Main.Game;
import StaticManagers.AudioManager;
import StaticManagers.FileManager;
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
        intro = new Animation(Game.WIDTH * 0.5f, Game.HEIGHT * 0.5f, 0, 40, FileManager.getAnimation("intro_"));
        intro.w = Game.WIDTH;
        intro.h = Game.HEIGHT;

        AudioManager.playMusic("start_menu.wav");
    }

    public void unload() {}

    public void draw(Graphics2D g) {
        intro.draw(g);
    }

    public void update() {

        intro.update();

        if (!intro.state) {
            gsm.setState(GameStateManager.MAINMENU);
        }

    }

    public void inputHandle(InputManager inputManager) {
        if (inputManager.isMousePressed("LEFTMOUSE") || inputManager.isMouseClicked("LEFTMOUSE") || inputManager.isKeyPressed("SPACE")) {
            gsm.setState(GameStateManager.MAINMENU);
        }
    }

}
