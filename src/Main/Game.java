package Main;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 12/29/2014.
 */

import GameState.GameStateManager;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Game implements Runnable {

    // Manager stuff
    private AssetManager assetManager;
    private GraphicsManager graphicsManager;
    private AudioManager audioManager;
    private Window window;

    private GameStateManager gsm;

    Game(AssetManager _assetManager, AudioManager _audioManager, GraphicsManager _graphicsManager) {

        assetManager = _assetManager;
        audioManager = _audioManager;
        graphicsManager = _graphicsManager;

        _audioManager.setVolume(OutputManager.getSetting("sfx_volume"), OutputManager.getSetting("music_volume"));

        window = new Window(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                gsm.quit();
            }
        });

        //Create a manager for handling the different game states e.g. intro, play, gameOver
        gsm = new GameStateManager(_assetManager, _audioManager, _graphicsManager, window);

        //Create a manager for handling key and mouse inputs
        new InputManager(window.canvas, gsm);
    }

    @Override
    public void run() {

        int FPS = 60;
        long fpsWait = (long) (1000 / FPS);
        long renderTime = 0;
        while (gsm.running) {

            long renderStart = System.nanoTime();

            // UPDATE
            gsm.update(renderTime);

            // RENDER
            window.render(gsm);

            // fps limiting
            renderTime = (System.nanoTime() - renderStart) / 1000000;
            try {
                Thread.sleep(Math.max(0, fpsWait - renderTime));
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
            renderTime = (System.nanoTime() - renderStart) / 1000000;
        }

        exit();

    }

    private void exit() {

        audioManager.stop();
        graphicsManager.stop();
        assetManager.stop();

        window.exit();

        OutputManager.printLog("Closing program...");
        OutputManager.saveAllSettings();
        OutputManager.printLog("--- LOG END ---");

        System.exit(0);

    }

    public static int currentTimeMillis() {
        long millisLong = System.currentTimeMillis();
        while (millisLong > Integer.MAX_VALUE) {
            millisLong -= Integer.MAX_VALUE;
        }
        return (int) millisLong;
    }

}