package GameState;

import Main.Game;
import StaticManagers.OutputManager;
import Visual.ButtonC;
import Visual.MouseCursor;
import Visual.Slider;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 7/10/2015.
 */
public class NextLevelState extends MenuState {

    private boolean win;

    public NextLevelState(GameStateManager gsm) {
        super(gsm);
    }

    protected void initButtons() {
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        initDefault();
        addButton("MAIN MENU", ButtonC.ButtonType.MAINMENU);

        int level = OutputManager.getSetting("level");
        if (level < GameStateManager.MAXLEVEL) {
            addButton("NEXT LEVEL", ButtonC.ButtonType.NEXTLEVEL);
            OutputManager.setSetting("level", level + 1);
        } else {
            win = true;
        }
    }

    public void init() {
        win = false;
        startY = Game.HEIGHT - 100;
        gsm.cursor.setSprite(MouseCursor.CURSOR);
        initButtons();
    }

    public void unload() {}

    public void draw(Graphics2D g) {

       // drawBackground(g, Theme.menuBackground);

        for (ButtonC b : buttons) {
            b.update(gsm.cursor.x, gsm.cursor.y);
            b.draw(g);
        }

        for (Slider s : sliders) {
            s.draw(g);
        }

        if (win) {
            String text = "YOU WIN! (work in progress btw)";
            g.setColor(ButtonC.buttonOverOld);
            g.drawString(
                    text,
                    Game.WIDTH * 0.5f - (int) g.getFontMetrics().getStringBounds(text, g).getWidth() * 0.5f,
                    Game.HEIGHT * 0.5f - 150
            );
        }

        gsm.cursor.draw(g);

    }

    public void update() {

    }

}
