package Main;

/**
 * Created by Wyatt on 12/29/2014.
 */
public class WarmVector_Client_Singleplayer {

    public static void main(String[] args) {
        Game game = new Game();
        new Thread(game).start();
    }

    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

}
