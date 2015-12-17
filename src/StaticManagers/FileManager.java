package StaticManagers;

import sun.misc.Launcher;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/24/2015.
 */
public class FileManager {



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
        System.out.println("Loading Sounds...");
        Map<String, Clip> temp = new HashMap<>();
        List<String> fileNames = listFiles("SFX");
        for (String name : fileNames) {
            temp.put(name, loadSound("SFX/" + name));
        }
        sounds = Collections.unmodifiableMap(temp);
    }

    private static Clip loadSound(String s) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(
                    AudioSystem.getAudioInputStream(
                            new BufferedInputStream(
                                    FileManager.class.getProtectionDomain().getClassLoader().getResourceAsStream(s))));
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
        System.out.println("Loading Images...");
        Map<String, BufferedImage> temp = new HashMap<>();
        List<String> fileNames = listFiles("Images");
        for (String name : fileNames) {
            if (!name.endsWith(".db")) {
                temp.put(name, loadImage("Images" + "/" + name));
            }
        }
        images = Collections.unmodifiableMap(temp);
    }

    private static final Map<String, BufferedImage[]> animations;
    static {
        Map<String, BufferedImage[]> temp = new HashMap<>();
        List<String> folderNames = listFiles("Animations");
        System.out.println("Loading Animations...");
        for (String folderName : folderNames) {
            folderName = folderName.replace("/","");
            if (folderName.endsWith("_")) {
                List<String> fileNames = listFiles("Animations/" + folderName);
                BufferedImage[] animation = new BufferedImage[fileNames.size()];
                for (int i = 0; i < fileNames.size(); i++) {
                    animation[i] = loadImage("Animations/" + folderName + "/" + fileNames.get(i));
                }
                temp.put(folderName, animation);
            }
        }
        animations = Collections.unmodifiableMap(temp);

    }

    private static BufferedImage loadImage(String s) {
        try {
            return ImageIO.read(FileManager.class.getProtectionDomain().getClassLoader().getResourceAsStream(s));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading graphics: " + s);
            System.exit(1);
        }
        return null;
    }

    private static List<String> listFiles(String dir) {
        final File jarFile = new File(FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        List<String> output = new ArrayList<>();
        if(jarFile.isFile()) {  // Run with JAR file
            JarFile jar = null;
            try {
                jar = new JarFile(jarFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while(entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(dir + "/")) { //filter according to the path
                    name = name.replace(dir + "/","");
                    if (!name.equals("")) output.add(name);
                }
            }
            try {
                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // Run with IDE
            final URL url = Launcher.class.getResource("/" + dir);
            if (url != null) {
                try {
                    final File apps = new File(url.toURI());
                    for (File app : apps.listFiles()) {
                        output.add(app.getName());
                    }
                } catch (URISyntaxException ex) {}
            }
        }

        if (output.size() == 0) System.out.println("Directory " + dir + " is empty");

        return output;

    }

}
