package Manager;

import GameState.IntroState;
import GameState.PauseState;
import GameState.GameOverState;
import GameState.MenuState;
import GameState.PlayState;
import GameState.GameState;

import java.awt.*;

/**
 * Created by Wyatt on 1/25/2015.
 */
public class GameStateManager {

    private boolean paused;
    private PauseState pauseState;

    private GameState[] gameStates;
    private int currentState;
    private int previousState;

    public static final int NUM_STATES = 4;
    public static final int INTRO = 0;
    public static final int MENU = 1;
    public static final int PLAY = 2;
    public static final int GAMEOVER = 3;

    public GameStateManager(InputManager inputManager) {
        paused = false;
        pauseState = new PauseState(this,inputManager);

        gameStates = new GameState[NUM_STATES];
        setState(INTRO);
    }

    public void setState(int i) {
        previousState = currentState;
        unloadState(previousState);
        currentState = i;
        switch (i){
            case(INTRO):
                gameStates[i] = new IntroState(this);
                break;
            case(MENU):
                gameStates[i] = new MenuState(this);
                break;
            case(PLAY):
                gameStates[i] = new PlayState(this);
                break;
            case(GAMEOVER):
                gameStates[i] = new GameOverState(this);
                break;
        }
        gameStates[i].init();
    }

    public void unloadState(int i) {
        gameStates[i] = null;
    }

    public void setPaused(boolean b) {
        paused = b;
    }

    public void update() {
        if(paused) {
            pauseState.update();
        }
        else if(gameStates[currentState] != null) {
            gameStates[currentState].update();
        }
    }

    public void draw(Graphics2D g) {
        if(paused) {
            pauseState.draw(g);
        }
        else if(gameStates[currentState] != null) {
            gameStates[currentState].draw(g);
        }
    }
}
