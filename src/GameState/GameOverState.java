package GameState;

import Main.Game;
import Visual.ButtonC;
import Visual.ThemeColors;

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
        startY = Game.HEIGHT/2 + 50;
    }

    protected void initButtons() {
        buttons = new ArrayList<ButtonC>();
        if (gsm.level < GameStateManager.MAXLEVEL) addButton("CONTINUE");
        initDefault();
    }

    public void draw(Graphics2D g) {

        drawBackground(g,ThemeColors.background);

        for (ButtonC b : buttons) {
            b.draw(g);
        }

        g.setColor(ThemeColors.textTitle);
        if (gsm.level >= GameStateManager.MAXLEVEL) {
            String text = "YOU DIED";
            g.drawString(
                    text,
                    Game.WIDTH / 2 - (int) g.getFontMetrics().getStringBounds(text, g).getWidth() / 2,
                    Game.HEIGHT / 2 - 150
            );
        }

    }

    public void update() {}

}
