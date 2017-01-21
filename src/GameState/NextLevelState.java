package GameState;

import Main.OutputManager;
import Main.Window;
import UI.ButtonUI;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/GameState/
 * Created by Wyatt on 7/10/2015.
 */
public class NextLevelState extends MenuState {

    private boolean win;

    public NextLevelState(GameStateManager _gsm) {
        super(_gsm, Window.HEIGHT - ButtonUI.BUTTON_HEIGHT);
    }

    public void load() {}

    protected void customMainInit() {
        addButton("MAIN MENU", ButtonUI.ButtonType.MAINMENU);

        int level = OutputManager.getSetting("level");
        if (level < PlayState.MAXLEVEL) {
            addButton("NEXT LEVEL", ButtonUI.ButtonType.NEXTLEVEL);
            OutputManager.setSetting("level", level + 1);
        } else {
            win = true;
        }
    }

    public void unload() {}

    public void draw(Graphics2D g) {

       super.draw(g);

        if (win) {
            String text = "YOU WIN! (work in progress btw)";
            g.setColor(ButtonUI.COLOR_OVER);
            g.drawString(
                    text,
                    (int)(Main.Window.WIDTH * 0.5 - g.getFontMetrics().getStringBounds(text, g).getWidth() * 0.5),
                    (int)(Window.HEIGHT * 0.5 - 150.0)
            );
        }

        gsm.cursor.draw(g);

    }

    public void update(double deltaTime) {}

}
