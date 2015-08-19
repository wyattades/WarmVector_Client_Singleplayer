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
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class StartMenuState extends MenuState {

//    private BufferedImage title;
//    private int title_w, title_h, title_i_w, title_i_h;

    private BarVisualizer barVisualizer;

    public StartMenuState(GameStateManager gsm) {super(gsm);}

    public void init() {
        startY = Game.HEIGHT/2 + 50;
        initButtons();
        barVisualizer = new BarVisualizer(480, ThemeColors.menuBackground);
//        title = FileManager.images.get("title.png");
//        title_w = title_i_w = title.getWidth()/2;
//        title_h = title_i_h = title.getHeight()/2;
    }

    protected void initButtons() {
        buttons = new ArrayList<ButtonC>();
        sliders = new ArrayList<Slider>();
        addButton("BEGIN");
        initDefault();
    }

    public void draw(Graphics2D g) {

        drawBackground(g,ThemeColors.background);

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

    public void update() {
        barVisualizer.react(InputManager.mouse.y);

//        float titleTextScale = (float) (Math.sin(Game.currentTimeMillis() / 600f)*0.1f + 0.6f);
//        title_w = (int) (title_i_w*titleTextScale);
//        title_h = (int) (title_i_h*titleTextScale);

    }

}
