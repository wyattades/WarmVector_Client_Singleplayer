package GameState;

import Main.Game;
import StaticManagers.InputManager;
import Visual.BarVisualizer;
import Visual.ButtonC;
import Visual.Slider;
import Visual.Theme;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class StartMenuState extends MenuState {

    private BarVisualizer barVisualizer;

    private Color backGround;

    private float backgroundHue = 0.0f;

    private final int menuWidth = 480;

    public StartMenuState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        startY = Game.HEIGHT - 100;
        initButtons();
        barVisualizer = new BarVisualizer(menuWidth, Theme.menuBackground);
        gsm.cursor.setMouse(Game.WIDTH/2, Game.HEIGHT/2);

    }

    public void unload() {
        Theme.buttonOver = Theme.buttonOverOld;
    }

    protected void initButtons() {

        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        initDefault();
        addButton("BEGIN", ButtonC.BEGIN);

    }

    public void draw(Graphics2D g) {

        drawBackground(g, backGround);

        barVisualizer.draw(g);

        for (ButtonC b : buttons) {
            b.update(gsm.cursor.x, gsm.cursor.y);
            b.draw(g);
        }

        for (Slider s : sliders) {
            s.draw(g);
        }

        //Draw the "W" & "V" title
        for (ButtonC b : buttons) {
            if (b.overBox && b.value == ButtonC.BEGIN) {
                g.setColor(Theme.buttonOver);
                g.setFont(Theme.fontHUD);
                g.drawString("ARM", Game.WIDTH - menuWidth / 2 + 70, Game.HEIGHT / 2 - 200);
                g.drawString("ECTOR", Game.WIDTH - menuWidth / 2 + 28, Game.HEIGHT / 2 - 40);
            } else {
                g.setColor(Theme.buttonDefault);
            }
        }
        //Cheaty hacky method, TODO: Change this
        if (buttons.size() == 5) {
            g.setFont(Theme.fontLogo);
            g.drawString("W", Game.WIDTH - (int) g.getFontMetrics().getStringBounds("W", g).getWidth() / 2 - menuWidth / 2, Game.HEIGHT / 2 - 200);
            g.drawString("V", Game.WIDTH - (int) g.getFontMetrics().getStringBounds("V", g).getWidth() / 2 - menuWidth / 2, Game.HEIGHT / 2 - 40);
        }
        gsm.cursor.draw(g);

    }

    int my;
    boolean pressed = false;

    public void update() {

        backGround = Theme.buttonOver = Color.getHSBColor(backgroundHue, 1, 1);
        backgroundHue += 0.001f;
        if (backgroundHue >= 1) backgroundHue = 0;

        barVisualizer.react(pressed, my);

    }

    public void inputHandle(InputManager inputManager) {
        pressed = inputManager.isMousePressed("LEFTMOUSE");
        my = inputManager.mouse.y;

        defaultInputHandle(inputManager);
    }

}
