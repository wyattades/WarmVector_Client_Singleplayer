package GameState;

import Manager.InputManager;
import Visual.MouseCursor;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class GameStateManager {

    public GameState[] gameStates;
    public int currentState;
    public int level;
    public static int MAXLEVEL = 2;

    public static final int
            NUM_STATES = 5,
            INTRO = 0,
            MENU = 1,
            PLAY = 2,
            GAMEOVER = 3,
            NEXTLEVEL = 4;

    public boolean paused;

    public MouseCursor cursor;

    public GameStateManager() {
        level = 1;
        cursor = new MouseCursor();
        gameStates = new GameState[NUM_STATES];
        setState(INTRO);
    }

    public void setState(int i) {
        int previousState = currentState;
        currentState = i;
        unloadState(previousState);
        switch (i) {
            case (INTRO):
                gameStates[i] = new IntroState(this);
                break;
            case (MENU):
                gameStates[i] = new MenuState(this);
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

    void unloadState(int i) {
        gameStates[i] = null;
    }

    public void setPaused(boolean p) {
        paused = p;
        if (p) cursor.setSprite(MouseCursor.CURSOR);
        else cursor.setSprite(MouseCursor.CROSSHAIR);
    }

    public void update() {
        if (gameStates[currentState] != null) {
            gameStates[currentState].inputHandle();
            gameStates[currentState].update();
        } else {
            System.out.println("gameState is null during update()");
            System.exit(1);
        }
    }

    public void draw(Graphics2D g) {
        if (gameStates[currentState] != null) {
            gameStates[currentState].draw(g);
            cursor.draw(g);
        } else {
            System.out.println("gameState is null during draw()");
            System.exit(1);
        }
    }
}
