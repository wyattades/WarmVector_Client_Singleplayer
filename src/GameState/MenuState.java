package GameState;

import Main.Game;
import StaticManagers.InputManager;
import StaticManagers.OutputManager;
import Visual.ButtonC;
import Visual.MouseCursor;
import Visual.Slider;

import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/17/2015.
 */
public abstract class MenuState extends GameState{

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
        buttons = new ArrayList<ButtonC>();
        sliders = new ArrayList<Slider>();
        addButton("BACK", ButtonC.BACK);
        OutputManager.reloadSettings();
        addSlider("Fullscreen", "fullscreen", new String[]{"On", "Off"}, OutputManager.getSettingValue("fullscreen"));
        addSlider("Anti-Aliasing", "anti_aliasing", new String[]{"On", "Off"}, OutputManager.getSettingValue("anti_aliasing"));
        addSlider("Quality", "quality", new String[]{"Good", "Great"}, OutputManager.getSettingValue("quality"));
        addSlider("Music Level", "music_volume", new String[]{"0", "25", "50", "75", "100"}, OutputManager.getSettingValue("music_volume"));
        addSlider("SFX Level", "sfx_volume", new String[]{"0", "25", "50", "75", "100"}, OutputManager.getSettingValue("sfx_volume"));
    }

    protected void buttonOutcome(int value) {
        switch(value) {
            case ButtonC.OPTIONS:
                initSettings();
                break;
            case ButtonC.HELP:
                //Open help menu
                break;
            case ButtonC.CONTINUE:
                gsm.level++;
                gsm.setState(GameStateManager.PLAY);
                break;
            case ButtonC.QUIT:
                System.exit(0);
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
            case ButtonC.RETURN:
                gsm.unloadState(GameStateManager.PAUSE);
                gsm.setState(GameStateManager.MAINMENU);
                break;
            default:
                System.out.println("Invalid button value: " + value);
                break;
        }
    }

    protected void sliderOutcome(String name, int setting) {

    }

    private boolean snapSliders;

    protected void defaultInputHandle() {
        gsm.cursor.setPosition(InputManager.mouse.x, InputManager.mouse.y);


        if (InputManager.isMousePressed("LEFTMOUSE") || InputManager.isMouseClicked("LEFTMOUSE")) {

            snapSliders = false;

            for (Slider s : sliders) {
                s.pressed = false;
                if (s.overBox(gsm.cursor.x, gsm.cursor.y)) {
                    s.pressed = true;
                    if (InputManager.mouse.dragged) {
                        s.slide(gsm.cursor.x - s.dragPos);
                    } else {
                        s.setDragPos(gsm.cursor.x);
                    }
                }
            }
            for (ButtonC b : buttons) {
                if (b.overBox &&
                        Game.currentTimeMillis() - InputManager.getMouseTime("LEFTMOUSE") > 400) {
                    InputManager.setMouseTime("LEFTMOUSE", Game.currentTimeMillis());
                    buttonOutcome(b.value);
                    break;
                }
            }
        } else {
            if (!snapSliders) {
                for (Slider s : sliders) {
                    s.snapSlider();
                    OutputManager.setSetting(s.name, s.current_option);
                    sliderOutcome(s.text, s.current_option);
                    s.pressed = false;
                }
                snapSliders = true;
            }
        }
    }

    public void inputHandle() {
        defaultInputHandle();
    }

}
