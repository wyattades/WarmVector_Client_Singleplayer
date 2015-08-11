package GameState;

import Main.Game;
import Manager.InputManager;
import Visual.ButtonC;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class MenuState extends GameState {

    ArrayList<ButtonC> buttons;

    public MenuState(GameStateManager gsm) {
        super(gsm);
    }

    private void addButton(String name, int x, int y) {
        buttons.add(new ButtonC(name, x, y, 300, 80, Color.red, Color.blue));

    }

    public void init() {
        buttons = new ArrayList<ButtonC>();
        addButton("Begin", Game.WIDTH / 2, Game.HEIGHT / 2 - 150);
        //addButton("Continue", Game.WIDTH / 2, Game.HEIGHT / 2 - 50);
        addButton("Help", Game.WIDTH / 2, Game.HEIGHT / 2 + 50);
        addButton("Quit", Game.WIDTH / 2, Game.HEIGHT / 2 + 150);
    }

    Color background = new Color(100,100,100);

    public void draw(Graphics2D g) {
        g.setColor(background);
        g.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
        for (ButtonC b : buttons) {
            b.draw(g);
        }
    }

    public void update() {
        for (ButtonC b : buttons) {
            if (b.pressed) {
                if (b.text.equals("Begin")) {
                    gsm.setState(GameStateManager.PLAY);
//                } else if (b.text.equals("Continue")) {
//                    gsm.setState(GameStateManager.PLAY);
                } else if (b.text.equals("Help")) {
                    //open help menu
                } else if (b.text.equals("Quit")) {
                    System.out.println("Quiting");
                    System.exit(0);

                }
            }
        }
    }

    public void inputHandle() {
        gsm.cursor.setPosition(InputManager.mouse.x, InputManager.mouse.y);
    }



}
