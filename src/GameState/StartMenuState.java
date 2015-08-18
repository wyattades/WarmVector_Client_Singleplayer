package GameState;

import Main.Game;
import Manager.FileManager;
import Manager.InputManager;
import Visual.ButtonC;
import Visual.MouseCursor;
import Visual.ThemeColors;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class StartMenuState extends MenuState {

    //private ArrayList<ButtonC> buttons;

//    private int titleTextScale;
    private BufferedImage title;

    public StartMenuState(GameStateManager gsm) {
        super(gsm);

    }

//    private void addButton(String name, int x, int y) {
//        buttons.add(new ButtonC(name, x, y, 300, 80));
//    }

    private int title_w, title_h, title_i_w, title_i_h;


    public void init() {
        startY = Game.HEIGHT/2 + 50;
        initDefault();
        addButton("Begin",true);
        title = FileManager.images.get("title.png");
        title_w = title_i_w = title.getWidth();
        title_h = title_i_h = title.getHeight();
    }

    public void setCursor() {
        gsm.cursor.setSprite(MouseCursor.CURSOR);
    }

//    public void initDefault() {
//        addButton("Begin",true);
//    }

    public void draw(Graphics2D g) {

        drawBackground(g,ThemeColors.background);

        for (ButtonC b : buttons) {
            b.draw(g);
        }

        g.drawImage(title, Game.WIDTH/2 - title_w/2, Game.HEIGHT/2 - title_h/2 - 180, title_w, title_h, null);
    }

    public void update() {

        float titleTextScale = (float) (Math.sin(Game.currentTimeMillis() / 600f)*0.1f + 0.6f);
        title_w = (int) (title_i_w*titleTextScale);
        title_h = (int) (title_i_h*titleTextScale);

        buttonOutcomes();
    }

    public void inputHandle() {
        gsm.cursor.setPosition(InputManager.mouse.x, InputManager.mouse.y);
    }



}
