package GameState;

import java.awt.*;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class GameStateManager {

    public boolean paused;
    private PauseState pauseState;

    private GameState[] gameStates;
    public int currentState;

    private static final int NUM_STATES = 4;
    private static final int INTRO = 0;
    private static final int MENU = 1;
    public static final int PLAY = 2;
    private static final int GAMEOVER = 3;

    public GameStateManager() {
        paused = false;
        pauseState = new PauseState(this);

        gameStates = new GameState[NUM_STATES];
        setState(PLAY);
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
        paused = b;
    }

    public void update() {
        //if (gameStates[currentState] != null) gameStates[currentState].inputHandle();
        if (paused) {
            pauseState.inputHandle();
            pauseState.update();
        } else if (gameStates[currentState] != null) {
            gameStates[currentState].inputHandle();
            gameStates[currentState].update();
        }
    }

    public void draw(Graphics2D g) {
        if (paused) {
            pauseState.draw(g);
        } else if (gameStates[currentState] != null) {
            gameStates[currentState].draw(g);
        }
    }
}
