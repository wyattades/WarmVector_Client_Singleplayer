package GameState;

import Main.Game;
import StaticManagers.InputManager;
import StaticManagers.OutputManager;
import Visual.ButtonC;
import Visual.Slider;

import java.awt.*;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/17/2015.
 */
public abstract class MenuState extends GameState {

    protected static final int MENU_WIDTH = 480;
    private static final Font textFont = new Font("Dotum Bold", Font.BOLD, (int) (30.0f * Game.SCALE));

    protected ArrayList<ButtonC> buttons;
    protected ArrayList<Slider> sliders;

    protected enum CurrentPage {
        MAIN,
        HELP,
        CREDITS,
        OPTIONS
    }
    protected CurrentPage currentPage;

    protected int startY;

    private static final int buttonDist = (int)(ButtonC.BUTTON_HEIGHT * 1.4);

    public MenuState(GameStateManager gsm) {
        super(gsm);
    }

    protected void addButton(String name, ButtonC.ButtonType value) {
        int y = startY;
        for (ButtonC b : buttons) {
            if (y >= b.y) y = b.y - buttonDist;
        }
        if (sliders != null) {
            for (Slider s : sliders) {
                if (y >= s.y) y = s.y - buttonDist;
            }
        }
        buttons.add(new ButtonC(name, value, Game.WIDTH - 50, y));
    }

    protected void addSlider(String text, String name, String[] options) {
        int y = startY;
        for (ButtonC b : buttons) {
            if (y >= b.y) y = b.y - buttonDist;
        }
        for (Slider s : sliders) {
            if (y >= s.y) y = s.y - buttonDist;
        }
        sliders.add(new Slider(Game.WIDTH - 50, y, text, name, options, OutputManager.getSetting(name)));
    }

    protected abstract void initButtons();

    protected void initDefault() {
        currentPage = CurrentPage.MAIN;
        addButton("QUIT", ButtonC.ButtonType.QUIT);
        addButton("HELP", ButtonC.ButtonType.HELP);
        addButton("CREDITS", ButtonC.ButtonType.CREDITS);
        addButton("OPTIONS", ButtonC.ButtonType.OPTIONS);
    }

    private void initSettings() {
        addSlider("Fullscreen", "fullscreen", new String[]{"Off", "On"});
        addSlider("Cave Mode", "cave_mode", new String[]{"Off", "On"});
        addSlider("Quality", "quality", new String[]{"Good", "Great"});
        addSlider("Music Level", "music_volume", new String[]{"0", "25", "50", "75", "100"});
        addSlider("SFX Level", "sfx_volume", new String[]{"0", "25", "50", "75", "100"});
    }

    private void pageDown() {
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        addButton("BACK", ButtonC.ButtonType.BACK);
    }

    protected void drawSpecific(Graphics2D g) {
        if (currentPage == CurrentPage.HELP) {
            g.setColor(ButtonC.buttonDefault);
            g.setFont(ButtonC.BUTTON_FONT);
            int x = Game.WIDTH - MENU_WIDTH + 20,
                    y = 400;
            g.drawString("CONTROLS:", x, y);
            g.setFont(textFont);
            g.drawString("W-A-S-D: move", x, y + ButtonC.BUTTON_HEIGHT);
            g.drawString("LEFT CLICK: shoot", x, y + 2 * ButtonC.BUTTON_HEIGHT);
            g.drawString("RIGHT CLICK: change weapon", x, y + 3 * ButtonC.BUTTON_HEIGHT);
            g.drawString("ESC: pause", x, y + 4 * ButtonC.BUTTON_HEIGHT);
            g.drawString("Clear the map of", x, y + 6 * ButtonC.BUTTON_HEIGHT);
            g.drawString("enemies to progress!", x, y + 7 * ButtonC.BUTTON_HEIGHT);

        } else if (currentPage == CurrentPage.CREDITS) {
            g.setColor(ButtonC.buttonDefault);
            g.setFont(ButtonC.BUTTON_FONT);
            int x = Game.WIDTH - MENU_WIDTH + 20,
                    y = 400;
            g.drawString("CREDITS:", x, y);
            g.setFont(textFont);
            g.drawString("CODING", x + 50, y + 2 * ButtonC.BUTTON_HEIGHT);
            g.drawString("Wyatt Ades", x + 50, y + 3 * ButtonC.BUTTON_HEIGHT);
            g.drawString("ART", x + 50, y + 5 * ButtonC.BUTTON_HEIGHT);
            g.drawString("Wyatt Ades", x + 50, y + 6 * ButtonC.BUTTON_HEIGHT);
            g.drawString("MUSIC", x + 50, y + 8 * ButtonC.BUTTON_HEIGHT);
            g.drawString("Illegal sources", x + 50, y + 9 * ButtonC.BUTTON_HEIGHT);
        }
    }

    protected void buttonOutcome(ButtonC.ButtonType value) {
        switch (value) {
            case OPTIONS:
                currentPage = CurrentPage.OPTIONS;
                pageDown();
                initSettings();
                break;
            case HELP:
                currentPage = CurrentPage.HELP;
                pageDown();
                break;
            case NEXTLEVEL:
                gsm.unloadState(GameStateManager.NEXTLEVEL);
                gsm.setState(GameStateManager.PLAY);
                break;
            case CONTINUE:
                gsm.setTopState(GameStateManager.FADEOUT);
                break;
            case QUIT:
                Game.running = false;
                break;
            case RESUME:
                gsm.unloadState(GameStateManager.PAUSE);
                break;
            case NEWGAME:
                OutputManager.setSetting("level", 1);
                gsm.setTopState(GameStateManager.FADEOUT);
                break;
            case RESTART:
                gsm.setState(GameStateManager.PLAY);
                break;
            case BACK:
                currentPage = CurrentPage.MAIN;
                initButtons();
                break;
            case CREDITS:
                currentPage = CurrentPage.CREDITS;
                pageDown();
                break;
            case MAINMENU:
                gsm.unloadState(gsm.topState);
                gsm.setState(GameStateManager.MAINMENU);
                break;
            default:
                System.out.println("Invalid button value: " + value);
                break;
        }
    }

    private Slider currentSlider = null;

    public void inputHandle(InputManager inputManager) {
        gsm.cursor.setPosition(inputManager.mouse.x, inputManager.mouse.y);

        boolean mousePressed = inputManager.isMousePressed("LEFTMOUSE");

        if (mousePressed || inputManager.isMouseClicked("LEFTMOUSE")) {

            for (ButtonC b : buttons) {
                if (b.overBox &&
                        Game.currentTimeMillis() - inputManager.getMouseTime("LEFTMOUSE") > 400) {
                    inputManager.setMouseTime("LEFTMOUSE", Game.currentTimeMillis());
                    buttonOutcome(b.value);
                    break;
                }
            }

        }

        if (mousePressed) {
            if (currentSlider == null) {
                for (Slider s : sliders) {
                    if (s.overBox(gsm.cursor.x, gsm.cursor.y)) {
                        s.pressed = true;
                        s.setDragPos(gsm.cursor.x);
                        currentSlider = s;
                    }
                }
            }

        } else {

            if (currentSlider != null) {
                currentSlider.snapSlider();
                currentSlider.pressed = false;
                OutputManager.setSetting(currentSlider.name, currentSlider.current_option);
                currentSlider = null;
            }

        }

        if (currentSlider != null) {
            currentSlider.slide(gsm.cursor.x - currentSlider.dragPos);
        }

    }

}
