package Manager;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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

    public static final Map<String, Clip> sounds;
    static {
        Map<String,Clip> temp = new HashMap<String, Clip>();
        File file = new File("out/production/WarmVector_Client_Singleplayer/SFX");
        File[] listOfFiles = file.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filename = listOfFiles[i].getName();
                temp.put(filename, loadSound(file.getPath() + "\\" + filename));
            }
        }
        sounds = Collections.unmodifiableMap(temp);

    }

    private static Clip loadSound(String s) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(s)));
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading sounds");
            System.exit(0);
        }
        return null;
    }

    public static final Map<String, BufferedImage> images;
    static {
        Map<String,BufferedImage> temp = new HashMap<String, BufferedImage>();
        File file = new File("out/production/WarmVector_Client_Singleplayer/Images");
        File[] listOfFiles = file.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filename = listOfFiles[i].getName();
                temp.put(filename, loadImage(file.getPath() + "\\" + filename));
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

    private static BufferedImage loadImage(String s) {
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
