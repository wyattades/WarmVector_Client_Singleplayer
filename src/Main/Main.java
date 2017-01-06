package Main;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 12/29/2014.
 */
class Main {

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl","True");
        //-Dsun.java2d.accthreshold=0

        AudioManager audioManager = new AudioManager();
        GraphicsManager graphicsManager = new GraphicsManager();

        Game game = new Game(audioManager, graphicsManager);

        new Thread(audioManager, "audio").start();
        new Thread(graphicsManager, "graphics").start();
        game.run();
    }

}
