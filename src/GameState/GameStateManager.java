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

    public GameState[] gameStates;
    public int currentState;

    public static final int NUM_STATES = 4;
    public static final int INTRO = 0;
    public static final int MENU = 1;
    public static final int PLAY = 2;
    public static final int GAMEOVER = 3;

    public boolean paused;

    public MouseCursor cursor;

    public GameStateManager() {
        gameStates = new GameState[NUM_STATES];
        setState(PLAY);
        cursor = new MouseCursor(FileManager.images.get("cursor.png"),FileManager.images.get("crosshair.png"));
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
        }
        gameStates[i].init();
    }

    void unloadState(int i) {
        gameStates[i] = null;
    }

    public void setPaused(boolean p) {
        paused = p;
        cursor.setSpriteCursor(p);
    }

    public void update() {
        if (gameStates[currentState] != null) {
            gameStates[currentState].inputHandle();
            gameStates[currentState].update();
        } else {
            System.out.println("gameState = null (during update)");
            System.exit(0);
        }
    }

    public void draw(Graphics2D g) {
        if (gameStates[currentState] != null) {
            gameStates[currentState].draw(g);
        } else {
            System.out.println("gameState = null (during draw)");
            System.exit(0);
        }
        cursor.draw(g);
    }
}
