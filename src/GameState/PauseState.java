package GameState;

import Main.Game;
import Visual.ButtonC;
import Visual.Slider;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class PauseState extends MenuState {

    public PauseState(GameStateManager gsm) {
        super(gsm);
    }

    protected void initButtons() {
        buttons = new ArrayList<ButtonC>();
        sliders = new ArrayList<Slider>();
        addButton("RESUME");
        initDefault();
    }

    public void init() {
        startY = Game.HEIGHT/2 + 50;
        initButtons();
    }

    public void draw(Graphics2D g) {

        for (Slider s : sliders) {
            s.draw(g);
        }

        for (ButtonC b : buttons) {
            b.draw(g);
        }
    }

    public void update() {}

    public void setCursor() {}

}
