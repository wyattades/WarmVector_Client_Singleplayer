package GameState;

import Main.Game;
import Manager.InputManager;
import Visual.ButtonC;
import Visual.MouseCursor;
import Visual.ThemeColors;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 7/10/2015.
 */
public class NextLevelState extends GameState {

    ArrayList<ButtonC> buttons;

    public NextLevelState(GameStateManager gsm) {
        super(gsm);
    }

    private void addButton(String name, int x, int y) {
        buttons.add(new ButtonC(name, x, y, 300, 80));
    }

    public void init() {
        buttons = new ArrayList<ButtonC>();
        if (gsm.level < GameStateManager.MAXLEVEL) addButton("Next Level", Game.WIDTH / 2, Game.HEIGHT / 2 - 150);
        addButton("Restart", Game.WIDTH / 2, Game.HEIGHT / 2 - 50);
        addButton("Help", Game.WIDTH / 2, Game.HEIGHT / 2 + 50);
        addButton("Quit", Game.WIDTH / 2, Game.HEIGHT / 2 + 150);
    }

    public void setCursor() {
        gsm.cursor.setSprite(MouseCursor.CURSOR);
    }

    public void draw(Graphics2D g) {
        g.setColor(ThemeColors.background);
        g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
        g.setColor(ThemeColors.textTitle);
        if (gsm.level >= GameStateManager.MAXLEVEL) {
            String text = "YOU WIN!";
            g.drawString(
                    text,
                    Game.WIDTH / 2 - (int) g.getFontMetrics().getStringBounds(text, g).getWidth() / 2,
                    Game.HEIGHT / 2 - 150
            );
        }
        for (ButtonC b : buttons) {
            if (b.pressed) {
                if (b.text.equals("Next Level")) {
                    gsm.level++;
                    gsm.setState(GameStateManager.PLAY);
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
