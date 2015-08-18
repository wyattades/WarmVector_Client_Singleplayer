package GameState;

import Main.Game;
import Visual.ButtonC;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 7/10/2015.
 */
public class NextLevelState extends MenuState {

    public NextLevelState(GameStateManager gsm) {
        super(gsm);
    }

    protected void initButtons() {
        buttons = new ArrayList<ButtonC>();
        if (gsm.level < GameStateManager.MAXLEVEL) addButton("CONTINUE");
        initDefault();
    }

    public void init() {
        startY = Game.HEIGHT/2 + 50;
        initButtons();
    }

    public void draw(Graphics2D g) {

        //drawBackground(g,ThemeColors.background);

        for (ButtonC b : buttons) {
            b.draw(g);
        }

        if (gsm.level >= GameStateManager.MAXLEVEL) {
            String text = "YOU WIN!";
            g.drawString(
                    text,
                    Game.WIDTH / 2 - (int) g.getFontMetrics().getStringBounds(text, g).getWidth() / 2,
                    Game.HEIGHT / 2 - 150
            );
        }

    }

    public void update() {}

}
