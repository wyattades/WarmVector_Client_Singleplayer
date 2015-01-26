package Manager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Wyatt on 1/24/2015.
 */



public class FileManager {

    public static BufferedImage PLAYER1G = load("/Sprites/player_0_1.png");
    public static BufferedImage PLAYER1 = load("/Sprites/player_0_0.png");
    public static BufferedImage PLAYER2G = load("/Sprites/player_1_1.png");
    public static BufferedImage PLAYER2 = load("/Sprites/player_1_0.png");
    public static BufferedImage M4 = load("/Sprites/gun_0.png");
    //do this later ^

    private static BufferedImage load(String s) {
        try {
            return ImageIO.read(FileManager.class.getResourceAsStream(s));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading graphics.");
            System.exit(0);
        }
        return null;
    }
}
