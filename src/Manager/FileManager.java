package Manager;

import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Wyatt on 1/24/2015.
 */

public class FileManager {

    public static BufferedImage TILESET1 = load("/Tilesets/leveltiles_01");
    public static BufferedImage BACKGROUND1 = load("/Maps/levelmap_01");
    public static BufferedImage PLAYER0G = load("/Sprites/player_0_1");
    public static BufferedImage PLAYER0 = load("/Sprites/player_0_0");
    public static BufferedImage PLAYER1G = load("/Sprites/player_1_1");
    public static BufferedImage PLAYER1 = load("/Sprites/player_1_0");
    public static BufferedImage M4 = load("/Sprites/gun_0");
    public static BufferedImage[] HIT = loadAnimation("/Sprites/hit_",4);

    private static BufferedImage load(String s) {
        try {
            return ImageIO.read(FileManager.class.getResourceAsStream(s+".png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading graphics");
            System.exit(0);
        }
        return null;
    }

    private static BufferedImage[] loadAnimation(String s, int amount) {
        BufferedImage[] bs = new BufferedImage[amount];
        for (int i = 0; i < amount; i++) {
            try {
                bs[i] = ImageIO.read(FileManager.class.getResourceAsStream(s+i+".png"));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading graphics");
                System.exit(0);
            }

        }
        return bs;
    }

}
