package GameState;

import Main.Game;
import Manager.InputManager;
import Visual.ButtonC;
import Visual.MouseCursor;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class PauseState extends GameState {

    ArrayList<ButtonC> buttons;

    public PauseState(GameStateManager gsm) {
        super(gsm);

    }

    private void addButton(String name, int x, int y) {
        buttons.add(new ButtonC(name, x, y, 300, 80));

    }

    public void init() {
        buttons = new ArrayList<ButtonC>();
        addButton("Resume", Game.WIDTH / 2, Game.HEIGHT / 2 - 150);
        addButton("Restart", Game.WIDTH / 2, Game.HEIGHT / 2 - 50);
        addButton("Help", Game.WIDTH / 2, Game.HEIGHT / 2 + 50);
        addButton("Quit", Game.WIDTH / 2, Game.HEIGHT / 2 + 150);
    }

    public void draw(Graphics2D g) {
        for (ButtonC b : buttons) {
            b.draw(g);
        }
    }

    public void update() {
        for (ButtonC b : buttons) {
            if (b.pressed) {
                if (b.text.equals("Resume")) {
                    gsm.setPaused(false);
                } else if (b.text.equals("Restart")) {
                    gsm.setState(GameStateManager.PLAY);
                } else if (b.text.equals("Help")) {
                    //open help menu
                } else if (b.text.equals("Quit")) {
                    System.out.println("Quiting");
                    System.exit(0);

                }
            }
        }
    }

    public void setCursor() {}

    public void inputHandle() {
        if (Game.currentTimeMillis() - InputManager.getKeyTime("ESC") > 400 && InputManager.isKeyPressed("ESC")) {
            InputManager.setKeyTime("ESC", Game.currentTimeMillis());
            gsm.setPaused(false);
        }
        gsm.cursor.setPosition(InputManager.mouse.x, InputManager.mouse.y);
    }

}
