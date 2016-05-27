package GameState;

import Main.Game;
import Visual.ButtonC;
import Visual.Slider;
import Visual.Theme;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 7/10/2015.
 */
public class NextLevelState extends MenuState {

//    private NextLevelTransition nextLevelTransition;

    public NextLevelState(GameStateManager gsm) {
        super(gsm);
    }

    protected void initButtons() {
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        initDefault();
        addButton("MAIN MENU", ButtonC.ButtonType.MAINMENU);
        if (gsm.level < GameStateManager.MAXLEVEL) addButton("CONTINUE", ButtonC.ButtonType.CONTINUE);
    }

    public void init() {
        startY = Game.HEIGHT - 100;
        initButtons();
    }

    public void unload() {
    }

    public void draw(Graphics2D g) {

       // drawBackground(g, Theme.menuBackground);

        for (ButtonC b : buttons) {
            b.update(gsm.cursor.x, gsm.cursor.y);
            b.draw(g);
        }

        for (Slider s : sliders) {
            s.draw(g);
        }

        if (gsm.level >= GameStateManager.MAXLEVEL) {
            String text = "YOU WIN! (work in progress btw)";
            g.setColor(Theme.buttonOverOld);
            g.drawString(
                    text,
                    Game.WIDTH / 2 - (int) g.getFontMetrics().getStringBounds(text, g).getWidth() / 2,
                    Game.HEIGHT / 2 - 150
            );
        }

        gsm.cursor.draw(g);

    }

    public void update() {

    }

}
