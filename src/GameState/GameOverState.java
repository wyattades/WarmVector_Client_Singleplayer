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
public class GameOverState extends GameState {


    // EXCLUDE


    ArrayList<ButtonC> buttons;

    public GameOverState(GameStateManager gsm) {
        super(gsm);
    }

    private void addButton(String name, int x, int y) {
        buttons.add(new ButtonC(name, x, y, 300, 80, Color.red, Color.blue));
    }

    public void init() {
        buttons = new ArrayList<ButtonC>();
        addButton("Restart", Game.WIDTH / 2, Game.HEIGHT / 2 - 50);
        addButton("Help", Game.WIDTH / 2, Game.HEIGHT / 2 + 50);
        addButton("Quit", Game.WIDTH / 2, Game.HEIGHT / 2 + 150);
    }

    public void draw(Graphics2D g) {
        g.setColor(new Color(200,200,200));
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        g.setColor(Color.black);
        if (gsm.level >= GameStateManager.MAXLEVEL) {
            String text = "YOU DIED";
            g.drawString(
                    text,
                    Game.WIDTH / 2 - (int) g.getFontMetrics().getStringBounds(text, g).getWidth() / 2,
                    Game.HEIGHT / 2 - 150
            );
        }
        for (ButtonC b : buttons) {
            if (b.pressed) {
               if (b.text.equals("Restart")) {
                    gsm.setState(GameStateManager.PLAY);
                } else if (b.text.equals("Help")) {
                    //open help menu
                } else if (b.text.equals("Quit")) {
                    System.out.println("Quiting");
                    System.exit(0);

                }
            }
        }
        for (ButtonC b : buttons) {
            b.draw(g);
        }
    }

    public void update() {
    }

    public void inputHandle() {
        gsm.cursor.setPosition(InputManager.mouse.x, InputManager.mouse.y);
    }
}
