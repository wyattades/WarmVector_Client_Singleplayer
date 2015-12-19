package GameState;

import Main.Game;
import StaticManagers.InputManager;
import StaticManagers.OutputManager;
import Visual.ButtonC;
import Visual.Slider;

import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/17/2015.
 */
public abstract class MenuState extends GameState {

    protected ArrayList<ButtonC> buttons;
    protected ArrayList<Slider> sliders;

    protected int startY;

    static final int buttonDist = 70;

    public MenuState(GameStateManager gsm) {
        super(gsm);
    }

    protected void addButton(String name, int value) {
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

    protected void addSlider(String text, String name, String[] options, int current_pos) {
        int y = startY;
        for (ButtonC b : buttons) {
            if (y >= b.y) y = b.y - buttonDist;
        }
        for (Slider s : sliders) {
            if (y >= s.y) y = s.y - buttonDist;
        }
        sliders.add(new Slider(Game.WIDTH - 50, y, text, name, options, current_pos));
    }

    protected abstract void initButtons();

    protected void initDefault() {
        addButton("QUIT", ButtonC.QUIT);
        addButton("HELP", ButtonC.HELP);
        addButton("CREDITS", ButtonC.CREDITS);
        addButton("OPTIONS", ButtonC.OPTIONS);

    }

    private void initSettings() {
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        addButton("BACK", ButtonC.BACK);
       // OutputManager.reloadSettings();
        addSlider("Fullscreen", "fullscreen", new String[]{"Off", "On"}, OutputManager.getSetting("fullscreen"));
        addSlider("Anti-Aliasing", "anti_aliasing", new String[]{"Off", "On"}, OutputManager.getSetting("anti_aliasing"));
        addSlider("Quality", "quality", new String[]{"Good", "Great"}, OutputManager.getSetting("quality"));
        addSlider("Music Level", "music_volume", new String[]{"0", "25", "50", "75", "100"}, OutputManager.getSetting("music_volume"));
        addSlider("SFX Level", "sfx_volume", new String[]{"0", "25", "50", "75", "100"}, OutputManager.getSetting("sfx_volume"));
    }

    protected void buttonOutcome(int value) {
        switch (value) {
            case ButtonC.OPTIONS:
                initSettings();
                break;
            case ButtonC.HELP:
                //Open help menu
                break;
            case ButtonC.CONTINUE:
                gsm.level++;
                gsm.unloadState(GameStateManager.NEXTLEVEL);
                gsm.setState(GameStateManager.PLAY);
                break;
            case ButtonC.QUIT:
                Game.running = false;
                break;
            case ButtonC.RESUME:
                gsm.unloadState(GameStateManager.PAUSE);
                break;
            case ButtonC.BEGIN:
                gsm.setTopState(GameStateManager.FADEOUT);
                break;
            case ButtonC.RESTART:
                gsm.setState(GameStateManager.PLAY);
                break;
            case ButtonC.BACK:
                initButtons();
                break;
            case ButtonC.CREDITS:
                //Roll the credits!
                break;
            case ButtonC.MAINMENU:
                gsm.unloadState(gsm.topState);
                gsm.setState(GameStateManager.MAINMENU);
                break;
            default:
                System.out.println("Invalid button value: " + value);
                break;
        }
    }

    private Slider currentSlider = null;

    protected void defaultInputHandle(InputManager inputManager) {
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

    public void inputHandle(InputManager inputManager) {
        defaultInputHandle(inputManager);
    }

}
