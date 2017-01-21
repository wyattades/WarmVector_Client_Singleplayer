package GameState;

import Main.Window;
import UI.ButtonUI;
import UI.MouseCursor;
import Util.MyInputEvent;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class PauseState extends MenuState {

    public PauseState(GameStateManager _gsm) {
        super(_gsm, Window.HEIGHT - 2 * ButtonUI.BUTTON_HEIGHT);
    }

    public void load() {}

    public void init() {
        super.init();
        gsm.cursor.saveOldPos();
    }

    protected void customMainInit() {
        addButton("MAIN MENU", ButtonUI.ButtonType.MAINMENU);
        addButton("RESUME", ButtonUI.ButtonType.RESUME);
    }

    public void unload() {
        gsm.cursor.setSprite(MouseCursor.CROSSHAIR);
        gsm.cursor.loadOldPos();
    }

    public void draw(Graphics2D g) {
        super.draw(g);

        gsm.cursor.draw(g);
    }

    public void update(double deltaTime) {}

    public void inputHandle(MyInputEvent event) {
        super.inputHandle(event);
        if (event.type == MyInputEvent.KEY_DOWN && event.code == KeyEvent.VK_ESCAPE) {
            gsm.unloadState(GameStateManager.TOP);
        }
    }

}
