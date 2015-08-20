package GameState;

import Main.Game;
import StaticManagers.InputManager;
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
        initDefault();
        addButton("MAIN MENU", ButtonC.RETURN);
        addButton("RESUME", ButtonC.RESUME);
    }

    public void init() {
        startY = Game.HEIGHT - 200;
        initButtons();
    }

    public void unload() {

    }

    public void draw(Graphics2D g) {

        for (Slider s : sliders) {
            s.draw(g);
        }

        for (ButtonC b : buttons) {
            b.update(InputManager.mouse.x,InputManager.mouse.y);
            b.draw(g);
        }
    }

    public void update() {}

    public void setCursor() {}

    public void inputHandle() {
        defaultInputHandle();
        if (InputManager.isKeyPressed("ESC") && Game.currentTimeMillis()-InputManager.getKeyTime("ESC") > 400) {
            gsm.setPaused(false);
            InputManager.setKeyTime("ESC",Game.currentTimeMillis());
        }
    }

}
