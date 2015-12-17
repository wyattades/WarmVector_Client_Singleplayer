package StaticManagers;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/24/2015.
 */
public class FileManager {

    private static String location;
    static {
//        location = ClassLoader.getSystemResource("").getPath();
//        InputStream is = FileManager.class.getResourceAsStream("");
        location = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        location = location.replace(".jar","");
//        location += "/";
//        System.out.println("LOCATION: "+location);
//      location = "src/";

//        try {
//            BufferedImage image = ImageIO.read(FileManager.class.getResourceAsStream("/Images/cursor.png"));
//            System.out.println("IT WORKED");
//        } catch(Exception e) {
//            System.out.println("COULDNT FIND SHIT");
//        }
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileFromJarFile);
//        BufferedImage b = is.;
    }

    public static Clip getSound(String name) {
        Clip sound = sounds.get(name);
        if (sound == null) {
            System.out.println("Sound " + name + " does not exist");
            System.exit(1);
        }
        return sound;
    }

    public static BufferedImage getImage(String name) {
        BufferedImage image = images.get(name);
        if (image == null) {
            System.out.println("Image " + name + " does not exist");
            System.exit(1);
        }
        return image;
    }

    public static BufferedImage[] getAnimation(String name) {
        BufferedImage[] animation = animations.get(name);
        if (animation == null) {
            System.out.println("Animation " + name + " does not exist");
            System.exit(1);
        }
        return animation;
    }

    private static final Map<String, Clip> sounds;
    static {
        Map<String, Clip> temp = new HashMap<>();
        File file = new File(location + "SFX");
        File[] listOfFiles = file.listFiles();
        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    String filename = listOfFile.getName();
                    temp.put(filename, loadSound(file.getPath() + "\\" + filename));
                }
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
            System.exit(1);
        }
        return null;
    }

    private static final Map<String, BufferedImage> images;
    static {
        Map<String, BufferedImage> temp = new HashMap<>();
        File file = new File(location + "Images");
        File[] listOfFiles = file.listFiles();
        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    String filename = listOfFile.getName();
                    temp.put(filename, loadImage(file.getPath() + "\\" + filename));
                }
            }
        }
        images = Collections.unmodifiableMap(temp);
    }

    private static final Map<String, BufferedImage[]> animations;
    static {
        Map<String, BufferedImage[]> temp = new HashMap<>();
        File file = new File(location + "Animations");
        File[] listOfFiles = file.listFiles();
        if (listOfFiles != null) {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isDirectory()) {
                    int amount = listOfFile.listFiles().length;
                    String filename = listOfFile.getName();
                    temp.put(filename, loadAnimation(file.getPath() + "\\" + filename + "\\" + filename, amount));
                }
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
            System.exit(1);
        }
        return null;
    }

    private static BufferedImage[] loadAnimation(String s, int amount) {
        BufferedImage[] bs = new BufferedImage[amount];
        for (int i = 0; i < amount; i++) {
            try {
                bs[i] = ImageIO.read(new File(s + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading graphics");
                System.exit(1);
            }

        }
        return bs;
    }

}
