package GameState;

import Main.Game;
import Manager.InputManager;
import HUD.ButtonC;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class PauseState extends GameState {

    ArrayList<ButtonC> buttons;

    public PauseState(GameStateManager gsm) {
        super(gsm);
        buttons = new ArrayList<ButtonC>();
        addButton("Resume",Game.WIDTH/2,Game.HEIGHT/2-200);
        addButton("Restart",Game.WIDTH/2,Game.HEIGHT/2);
        addButton("Help",Game.WIDTH/2,Game.HEIGHT/2+200);
        addButton("Quit",Game.WIDTH/2,Game.HEIGHT/2+400);
    }

    private void addButton(String name, int x, int y) {
        buttons.add(new ButtonC(name,x,y,300,80,Color.red,Color.blue));

    }

    public void init() {

    }

    public void draw(Graphics2D g) {
        for (ButtonC b : buttons) {
            b.draw(g);
            if (b.pressed) {
                if (b.text.equals("Resume")) {
                    gsm.setPaused(false);
                } else if (b.text.equals("Restart")) {
                    gsm.unloadState(gsm.currentState);
                    gsm.setState(gsm.PLAY);
                    gsm.setPaused(false);
                } else if (b.text.equals("Help")) {

                } else if (b.text.equals("Quit")) {
                    System.exit(0);
                }
            }
        }
    }

    public void update() {

    }

    public void inputHandle() {
        if (InputManager.isKeyPressed("ALT") && InputManager.getKeyTime("ALT")-Game.currentTimeMillis() > 100) {
            InputManager.setKeyTime("ALT", Game.currentTimeMillis());
            gsm.setPaused(false);
        }
//        if (InputManager.isKeyPressed("ALT") || InputManager.isKeyPressed("ESC")) gsm.setPaused(false);
    }

}
