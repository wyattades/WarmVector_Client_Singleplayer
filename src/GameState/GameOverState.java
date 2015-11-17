package GameState;

import Main.Game;
import Visual.ButtonC;
import Visual.Slider;
import Visual.Theme;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class GameOverState extends MenuState {

    public GameOverState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        startY = Game.HEIGHT - 100;
    }

    public void unload() {

    }

    protected void initButtons() {
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        if (gsm.level < GameStateManager.MAXLEVEL) addButton("CONTINUE", ButtonC.CONTINUE);
        initDefault();
    }

    public void draw(Graphics2D g) {

        drawBackground(g, Theme.menuBackground);

        for (Slider s : sliders) {
            s.draw(g);
        }

        for (ButtonC b : buttons) {
            b.update(gsm.cursor.x, gsm.cursor.y);
            b.draw(g);
        }

        if (gsm.level >= GameStateManager.MAXLEVEL) {
            String text = "YOU DIED";
            g.setColor(Theme.buttonDefault);
            g.drawString(
                    text,
                    Game.WIDTH / 2 - (int) g.getFontMetrics().getStringBounds(text, g).getWidth() / 2,
                    Game.HEIGHT / 2 - 150
            );
        }

    }

    public void update() {
    }

}
