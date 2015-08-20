package GameState;

import Main.Game;
import StaticManagers.InputManager;
import Visual.ButtonC;
import Visual.MouseCursor;
import Visual.Slider;

import java.util.ArrayList;

/**
 * Created by wyatt on 8/17/2015.
 */
public abstract class MenuState extends GameState{

    protected ArrayList<ButtonC> buttons;
    protected ArrayList<Slider> sliders;

    protected int startY;

    static final int buttonDist = 70;

    public MenuState(GameStateManager gsm) {super(gsm);}

    protected void addButton(String name) {
        int y = startY;
        for (ButtonC b : buttons) {
            if (y >= b.y) y = b.y - buttonDist;
        }
        if (sliders != null) {
            for (Slider s : sliders) {
                if (y >= s.y) y = s.y - buttonDist;
            }
        }
        buttons.add(new ButtonC(name, Game.WIDTH - 50, y));
    }

    protected void addSlider(String name, String[] options, int current_pos) {
        int y = startY;
        for (ButtonC b : buttons) {
            if (y >= b.y) y = b.y - buttonDist;
        }
        for (Slider s : sliders) {
            if (y >= s.y) y = s.y - buttonDist;
        }
        sliders.add(new Slider(Game.WIDTH - 50, y, name, options, current_pos));
    }

    protected abstract void initButtons();

    protected void initDefault() {
        addButton("QUIT");
        addButton("HELP");
        addButton("CREDITS");
        addButton("OPTIONS");

    }

    private void initSettings() {
        buttons = new ArrayList<ButtonC>();
        sliders = new ArrayList<Slider>();
        addButton("BACK");
        addSlider("Fullscreen", new String[]{"On","Off"}, 0);
        addSlider("Anti-Aliasing", new String[]{"On", "Off"}, 1);
        addSlider("Quality", new String[]{"Good","Great"},0);
        addSlider("Music Level", new String[]{"0","25","50","75","100"}, 4);
        addSlider("SFX Level", new String[]{"0","25","50","75","100"},4);
    }

    protected void buttonOutcome(ButtonC b) {
        if (b.text.equals("OPTIONS")) {
            initSettings();
        } else if (b.text.equals("HELP")) {
            //Open help menu
        } else if (b.text.equals("CONTINUE")) {
            gsm.level++;
            gsm.setState(GameStateManager.PLAY);
        } else if (b.text.equals("QUIT")) {
            System.exit(0);
        } else if (b.text.equals("RESUME")) {
            gsm.setPaused(false);
        } else if (b.text.equals("BEGIN")) {
            gsm.setState(GameStateManager.PLAY);
        } else if (b.text.equals("RESTART")) {
            gsm.setState(GameStateManager.PLAY);
        } else if (b.text.equals("BACK")) {
            initButtons();
        }
    }

    protected void sliderOutcome(Slider s) {

    }

    public void setCursor() {
        gsm.cursor.setSprite(MouseCursor.CURSOR);
    }

    boolean snapSliders;

    protected void defaultInputHandle() {
        gsm.cursor.setPosition(InputManager.mouse.x, InputManager.mouse.y);


        if (InputManager.isMousePressed("LEFTMOUSE") || InputManager.isMouseClicked("LEFTMOUSE")) {

            snapSliders = false;

            for (Slider s : sliders) {
                s.pressed = false;
                if (s.overBox(InputManager.mouse.x, InputManager.mouse.y)) {
                    s.pressed = true;
                    if (InputManager.mouse.dragged) {
                        s.slide(InputManager.mouse.x - s.dragPos);
                    } else {
                        s.setDragPos(InputManager.mouse.x);
                    }
                }
            }
            for (ButtonC b : buttons) {
                if (b.overBox(InputManager.mouse.x, InputManager.mouse.y) &&
                        Game.currentTimeMillis() - InputManager.getMouseTime("LEFTMOUSE") > 400) {
                    InputManager.setMouseTime("LEFTMOUSE", Game.currentTimeMillis());
                    buttonOutcome(b);
                    break;
                }
            }
        } else {
            if (!snapSliders) {
                for (Slider s : sliders) {
                    s.snapSlider();
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
