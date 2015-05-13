package GameState;

import Main.Game;
import Visual.MouseCursor;
import Manager.FileManager;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/25/2015.
 */
public class GameStateManager {

    public boolean paused;
    private PauseState pauseState;

    private GameState[] gameStates;
    public int currentState;

    public static final int NUM_STATES = 4;
    public static final int INTRO = 0;
    public static final int MENU = 1;
    public static final int PLAY = 2;
    public static final int GAMEOVER = 3;
    public MouseCursor cursor;
    private Robot robot;

    public GameStateManager() {
        paused = false;
        pauseState = new PauseState(this);

        gameStates = new GameState[NUM_STATES];
        setState(PLAY);
        cursor = new MouseCursor(FileManager.CURSOR,FileManager.CROSSHAIR);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void setState(int i) {
        int previousState = currentState;
        unloadState(previousState);
        currentState = i;
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
        }
        gameStates[i].init();
    }

    void unloadState(int i) {
        gameStates[i] = null;
    }

    public void setPaused(boolean b) {
        cursor.setSpriteCursor(b);
        paused = b;
    }

    public void update() {
        //move mouse cursor to center of the display
        robot.mouseMove(Game.WIDTH / 2, Game.HEIGHT / 2);
        if (paused) {
            pauseState.inputHandle();
            pauseState.update();
        } else if (gameStates[currentState] != null) {
            gameStates[currentState].inputHandle();
            gameStates[currentState].update();
        }
    }

    public void draw(Graphics2D g) {
        if (gameStates[currentState] != null) {
            gameStates[currentState].draw(g);
        }
        if (paused) {
            pauseState.draw(g);
        }
        cursor.draw(g);
    }
}
