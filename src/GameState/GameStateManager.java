package GameState;

import Main.Game;
import StaticManagers.InputManager;
import Visual.MouseCursor;
import Visual.ThemeColors;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class GameStateManager {

    private GameState[] gameStates;

    private int currentState, topState;

    public int level;

    public boolean paused;

    public MouseCursor cursor;

    public static final int
            MAXLEVEL = 2,

            NUM_STATES = 5,

            INTRO = 0,
            MAINMENU = 1,
            PLAY = 2,
            GAMEOVER = 3,
            NEXTLEVEL = 4;

    public GameStateManager() {

        level = 1;
        cursor = new MouseCursor();
        gameStates = new GameState[NUM_STATES];
        setState(INTRO);

    }

    public void setState(int i) {

        int previousState = currentState;
        currentState = i;
        if (gameStates[previousState] != null) unloadState(previousState);
        switch (i) {
            case (INTRO):
                gameStates[i] = new IntroState(this);
                break;
            case (MAINMENU):
                gameStates[i] = new StartMenuState(this);
                break;
            case (PLAY):
                gameStates[i] = new PlayState(this);
                break;
            case (GAMEOVER):
                gameStates[i] = new GameOverState(this);
                break;
            case (NEXTLEVEL):
                gameStates[i] = new NextLevelState(this);
                break;
        }
        gameStates[i].init();

    }

    public void setTopState(int i) {
        topState = i;
        switch (i) {
            case (INTRO):
                gameStates[i] = new IntroState(this);
                break;
            case (MAINMENU):
                gameStates[i] = new StartMenuState(this);
                break;
            case (PLAY):
                gameStates[i] = new PlayState(this);
                break;
            case (GAMEOVER):
                gameStates[i] = new GameOverState(this);
                break;
            case (NEXTLEVEL):
                gameStates[i] = new NextLevelState(this);
                break;
        }
        gameStates[i].init();
    }

    private void unloadState(int i) {
        gameStates[i].unload();
        gameStates[i] = null;
    }

    public void setPaused(boolean p) {

        paused = p;
        if (p) cursor.setSprite(MouseCursor.CURSOR);
        else cursor.setSprite(MouseCursor.CROSSHAIR);

    }

    public void inputHandle() {

        if (gameStates[topState] != null) {
            gameStates[topState].inputHandle();
        } else if (gameStates[currentState] != null) {
            gameStates[currentState].inputHandle();
        } else {
            System.out.println("gameState is null during inputHandle()");
            System.exit(1);
        }

    }

    public void update() {
        if (gameStates[topState] != null) {
            gameStates[topState].update();
        } else if (gameStates[currentState] != null) {
//            gameStates[currentState].inputHandle();
            gameStates[currentState].update();
        } else {
            System.out.println("gameState is null during update()");
            System.exit(1);
        }

    }

    public void draw(Graphics2D g) {

        if (gameStates[currentState] != null) {
            gameStates[currentState].draw(g);
            if (gameStates[topState] != null) gameStates[topState].draw(g);
            cursor.draw(g);
        } else {
            System.out.println("gameState is null during draw()");
            System.exit(1);
        }

    }

}
