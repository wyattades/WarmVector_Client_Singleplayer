package Manager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/24/2015.
 */
public class FileManager {

    public static final Map<String, BufferedImage> images;
    static {
        Map<String,BufferedImage> temp = new HashMap<String, BufferedImage>();
        File file = new File("out/production/WarmVector_Client_Singleplayer/Images");
        File[] listOfFiles = file.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filename = listOfFiles[i].getName();
                temp.put(filename, load(file.getPath()+"\\"+filename));
            }
        }
        images = Collections.unmodifiableMap(temp);

    }

    public static final Map<String, BufferedImage[]> animations;
    static {
        Map<String,BufferedImage[]> temp = new HashMap<String, BufferedImage[]>();
        File file = new File("out/production/WarmVector_Client_Singleplayer/Animations");
        File[] listOfFiles = file.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory()) {
                int amount = listOfFiles[i].listFiles().length;
                String filename = listOfFiles[i].getName();
                temp.put(filename, loadAnimation(file.getPath()+"\\"+filename+"\\"+filename,amount));
            }
        }
        animations = Collections.unmodifiableMap(temp);

    }

//    public static BufferedImage TILESET1 = load("/Tilesets/leveltiles_01");
//    public static BufferedImage FOREGROUND1 = load("/Maps/levelmap_01");
//    public static BufferedImage BACKGROUND1 = load("/Background/background_01");
//    public static BufferedImage PLAYER0G = load("/Sprites/player_0_1");
//    public static BufferedImage PLAYER0 = load("/Sprites/player_0_0");
//    public static BufferedImage PLAYER1G = load("/Sprites/player_1_1");
//    public static BufferedImage PLAYER1 = load("/Sprites/player_1_0");
//    public static BufferedImage CROSSHAIR = load("/Sprites/crosshair");
//    public static BufferedImage M4 = load("/Sprites/gun_0");
//    public static BufferedImage CURSOR = load("/Sprites/cursor");
//    public static BufferedImage[] HIT = loadAnimation("/Sprites/hit_", 4s);

    private static BufferedImage load(String s) {
        try {

            return ImageIO.read(new File(s));
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
                bs[i] = ImageIO.read(new File(s + i+".png"));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading graphics");
                System.exit(0);
            }

        }
        return bs;
    }

}
