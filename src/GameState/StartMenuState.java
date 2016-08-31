package GameState;

import Main.Game;
import StaticManagers.AudioManager;
import StaticManagers.InputManager;
import StaticManagers.OutputManager;
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
    private static final Font
            fontLogo = new Font("Dotum Bold", Font.BOLD, (int) (200.0f * Game.SCALE)),
            fontHUD = new Font("Dotum Bold", Font.BOLD, (int) (40.0f * Game.SCALE));

    private BarVisualizer barVisualizer;
    private Color backGround;
    private float backgroundHue;
    private int my;
    private boolean pressed;

    public StartMenuState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        pressed = false;
        my = 0;
        backgroundHue = 0.0f;

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
        addButton("NEW GAME", ButtonC.ButtonType.NEWGAME);
        if (OutputManager.getSetting("level") > 1) addButton("CONTINUE", ButtonC.ButtonType.CONTINUE);

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

            if ((b.value == ButtonC.ButtonType.NEWGAME || b.value == ButtonC.ButtonType.CONTINUE) && b.overBox) {
                beginButtonHover = true;
            }

            b.draw(g);
        }

        float drawHeight = Game.HEIGHT * 0.5f;
        if (currentPage != CurrentPage.MAIN) drawHeight = -500;
        //Draw the "W" & "V" title
        if (beginButtonHover) {
            g.setColor(ButtonC.buttonOver);
            g.setFont(fontHUD);
            g.drawString("ARM", Game.WIDTH - MENU_WIDTH * 0.5f + 70, drawHeight - 200);
            g.drawString("ECTOR", Game.WIDTH - MENU_WIDTH * 0.5f + 28, drawHeight - 40);
        } else {
            g.setColor(ButtonC.buttonDefault);
        }
        g.setFont(fontLogo);
        g.drawString("W", Game.WIDTH - (int) g.getFontMetrics().getStringBounds("W", g).getWidth() * 0.5f - MENU_WIDTH * 0.5f, drawHeight - 200);
        g.drawString("V", Game.WIDTH - (int) g.getFontMetrics().getStringBounds("V", g).getWidth() * 0.5f - MENU_WIDTH * 0.5f, drawHeight - 40);

        drawSpecific(g);

        gsm.cursor.draw(g);

    }

    public void update() {

        backGround = ButtonC.buttonOver = Color.getHSBColor(backgroundHue, 1.0f, 1.0f);
        backgroundHue += 0.001f;
        if (backgroundHue >= 1.0f) backgroundHue = 0;

        barVisualizer.react(pressed, my);

    }

    public void inputHandle(InputManager inputManager) {
        super.inputHandle(inputManager);
        pressed = inputManager.isMousePressed("LEFTMOUSE");
        my = inputManager.mouse.y;
    }

}
