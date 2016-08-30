package GameState;

import Main.Game;
import StaticManagers.AudioManager;
import StaticManagers.InputManager;
import Visual.BarVisualizer;
import Visual.ButtonC;
import Visual.Slider;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class StartMenuState extends MenuState {

    // Private constants
    public static final Color BACKGROUND_COLOR = new Color(22, 20, 22);
    private static final int MENU_WIDTH = 480;
    private static final Font
            fontLogo = new Font("Dotum Bold", Font.BOLD, (int) (200.0f * Game.SCALE)),
            fontHUD = new Font("Dotum Bold", Font.BOLD, (int) (40.0f * Game.SCALE));

    private BarVisualizer barVisualizer;
    private Color backGround;
    private float backgroundHue = 0.0f;

    public StartMenuState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        startY = Game.HEIGHT - ButtonC.BUTTON_HEIGHT * 2;
        initButtons();
        barVisualizer = new BarVisualizer(MENU_WIDTH, BACKGROUND_COLOR);
        gsm.cursor.setMouse((int)(Game.WIDTH * 0.5f), (int)(Game.HEIGHT * 0.5f));

        AudioManager.playMusic("start_menu.wav");

    }

    public void unload() {
        ButtonC.buttonOver = ButtonC.buttonOverOld;
    }

    protected void initButtons() {

        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        initDefault();
        addButton("BEGIN", ButtonC.ButtonType.BEGIN);

    }

    public void draw(Graphics2D g) {

        drawBackground(g, backGround);

        barVisualizer.draw(g);

        for (Slider s : sliders) {
            s.draw(g);
        }

        boolean beginButtonHover = false;

        // Draw the buttons
        for (ButtonC b : buttons) {

            b.update(gsm.cursor.x, gsm.cursor.y);

            if (b.value == ButtonC.ButtonType.BEGIN && b.overBox) {
                beginButtonHover = true;
            }

            b.draw(g);
        }

        //Draw the "W" & "V" title
        if (beginButtonHover) {
            g.setColor(ButtonC.buttonOver);
            g.setFont(fontHUD);
            g.drawString("ARM", Game.WIDTH - MENU_WIDTH * 0.5f + 70, Game.HEIGHT * 0.5f - 200);
            g.drawString("ECTOR", Game.WIDTH - MENU_WIDTH * 0.5f + 28, Game.HEIGHT * 0.5f - 40);
        } else {
            g.setColor(ButtonC.buttonDefault);
        }
        g.setFont(fontLogo);
        g.drawString("W", Game.WIDTH - (int) g.getFontMetrics().getStringBounds("W", g).getWidth() * 0.5f - MENU_WIDTH * 0.5f, Game.HEIGHT * 0.5f - 200);
        g.drawString("V", Game.WIDTH - (int) g.getFontMetrics().getStringBounds("V", g).getWidth() * 0.5f - MENU_WIDTH * 0.5f, Game.HEIGHT * 0.5f - 40);


        gsm.cursor.draw(g);

    }

    int my;
    boolean pressed = false;

    public void update() {

        backGround = ButtonC.buttonOver = Color.getHSBColor(backgroundHue, 1, 1);
        backgroundHue += 0.001f;
        if (backgroundHue >= 1.0f) backgroundHue = 0;

        barVisualizer.react(pressed, my);

    }

    public void inputHandle(InputManager inputManager) {
        pressed = inputManager.isMousePressed("LEFTMOUSE");
        my = inputManager.mouse.y;

        defaultInputHandle(inputManager);
    }

}
