package Main;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 12/29/2014.
 */
class Main {

    public static void main(String[] args) {

        AudioManager audioManager = new AudioManager();
        GraphicsManager graphicsManager = new GraphicsManager();

        Game game = new Game(new AssetManager(), audioManager, graphicsManager);

        new Thread(audioManager, "audio").start();
        new Thread(graphicsManager, "graphics").start();
        new Thread(game, "game").start();
    }

}
