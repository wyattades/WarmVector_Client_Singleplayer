package GameState;

import Main.Game;
import StaticManagers.FileManager;
import StaticManagers.InputManager;
import Visual.BarVisualizer;
import Visual.ButtonC;
import Visual.Slider;
import Visual.ThemeColors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class StartMenuState extends MenuState {

//    private BufferedImage title;
//    private int title_w, title_h, title_i_w, title_i_h;

    private BarVisualizer barVisualizer;

    private Color backGround;

    public StartMenuState(GameStateManager gsm) {super(gsm);}

    public void init() {
        startY = Game.HEIGHT - 100;
        initButtons();
        barVisualizer = new BarVisualizer(480, ThemeColors.menuBackground);
        //backGround = Color.black;
//        title = FileManager.images.get("title.png");
//        title_w = title_i_w = title.getWidth()/2;
//        title_h = title_i_h = title.getHeight()/2;
    }

    protected void initButtons() {
        buttons = new ArrayList<ButtonC>();
        sliders = new ArrayList<Slider>();
        initDefault();
        addButton("BEGIN");
    }

    public void draw(Graphics2D g) {

        drawBackground(g,backGround);

        barVisualizer.draw(g);

        for (ButtonC b : buttons) {
            b.draw(g);
        }

        for (Slider s : sliders) {
            s.draw(g);
        }

        g.setColor(ThemeColors.buttonDefault);
        g.setFont(new Font("Dotum Bold",Font.BOLD,200));
        g.drawString("W", Game.WIDTH - 300, Game.HEIGHT / 2 - 200);
        g.drawString("V", Game.WIDTH - 273, Game.HEIGHT / 2 - 140);

        //g.drawImage(title, Game.WIDTH/2 - title_w/2, Game.HEIGHT/2 - title_h/2 - 180, title_w, title_h, null);
    }

    float value = 0.0f;

    public void update() {


        backGround = Color.getHSBColor(value,1,1);
        ThemeColors.buttonOver = backGround;
        value+=0.001f;
        if (value >= 1) value = 0;
    }

    public void inputHandle() {
        defaultInputHandle();
        barVisualizer.react(InputManager.mouse.y);
        if (InputManager.isMousePressed("LEFTMOUSE")) barVisualizer.spike(InputManager.mouse.y);
    }

}
