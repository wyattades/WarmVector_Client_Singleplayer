package GameState;

import Main.Game;
import Manager.InputManager;
import Visual.ButtonC;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by wyatt on 8/17/2015.
 */
public abstract class MenuState extends GameState{

    protected ArrayList<ButtonC> buttons;

    private final int buttonDist = 90;

    protected int startY = Game.HEIGHT/2 - 250;

    public MenuState(GameStateManager gsm) {super(gsm);}

    protected void addButton(String name, boolean above) {
        if (!above) {
            int y = startY;
            for (ButtonC b : buttons) {
                if (y <= b.y) y = b.y + buttonDist;
            }
            buttons.add(new ButtonC(name, Game.WIDTH / 2, y, 300, 70));
        } else {
            for (ButtonC b : buttons) {
               b.y += buttonDist;
            }
            buttons.add(new ButtonC(name, Game.WIDTH / 2, startY, 300, 70));
        }
    }

    protected void initDefault() {
        buttons = new ArrayList<ButtonC>();
        addButton("Settings",false);
        addButton("Help",false);
        addButton("Quit",false);
    }

    private void initSettings() {
        buttons = new ArrayList<ButtonC>();
        addButton("AA On",false);
        addButton("Back",false);
    }

    public void buttonOutcomes() {
        for (ButtonC b : buttons) {
            if (b.pressed) {
                if (b.text.equals("Settings")) {
                    initSettings();
                } else if (b.text.equals("Help")) {
                    //Open help menu
                } else if (b.text.equals("Quit")) {
                    System.exit(0);
                } else if (b.text.equals("Resume")) {
                    gsm.setPaused(false);
                } else if (b.text.equals("Begin")) {
                    gsm.setState(GameStateManager.PLAY);
                } else if (b.text.equals("Restart")) {
                    gsm.setState(GameStateManager.PLAY);
                } else if (b.text.equals("Back")) {
                    init();
                } else if (b.text.equals("AA Off")) {
                    b.text = "AA On";
                    //set antialiasing to off
                } else if (b.text.equals("AA On")) {
                    b.text = "AA Off";
                    //set antialiasing to on
                }
                b.pressed = false;
            }
        }
    }

    public void inputHandle() {
        gsm.cursor.setPosition(InputManager.mouse.x, InputManager.mouse.y);
    }

}
