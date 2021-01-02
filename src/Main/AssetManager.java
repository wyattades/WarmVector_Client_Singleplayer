package Main;

import Util.ImageUtils;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Directory: WarmVector_Client_Singleplayer/Main/
 * Created by Wyatt on 9/8/2016.
 * Last edited by Wyatt on 9/8/2016.
 */
public class AssetManager {

    private ConcurrentHashMap<String, Object> assets;

    private ExecutorService executor;

    AssetManager() {
        assets = new ConcurrentHashMap<>();
    }

    public void loadAssets(String[] fileNames) {
        if (!isAvailable()) {
            System.err.println("Error: cannot add to loading queue while loader is running." + Arrays.toString(fileNames));
            System.exit(1);
        }

        executor = Executors.newSingleThreadExecutor();
        for (String fileName : fileNames) {

            if (!assets.containsKey(fileName)) {
                executor.execute(() -> {
                    if (fileName.endsWith(".mp3")) {
                        assets.put(fileName, loadLongAudio(fileName));
                    } else if (fileName.endsWith(".wav")) {
                        assets.put(fileName, loadShortAudio(fileName));
                    } else if (fileName.endsWith(".png")) {
                        assets.put(fileName, loadImage("resources/Images/" + fileName));
                    } else if (fileName.endsWith("_")) {
                        assets.put(fileName, loadAnimation("resources/Animations/" + fileName));
                    } else {
                        System.err.println("Error: invalid file type requested.");
                        System.exit(1);
                    }
                });
            }
        }
        executor.shutdown();
    }

    public boolean isAvailable() {
        return executor == null || executor.isTerminated();
    }

    // TODO: testing!!!!!
    public AudioClip getSFX(String name) {
        return loadShortAudio(name);
    }

    public Object getAsset(String name) {
        Object asset = null;
        if (isAvailable()) {
            asset = assets.get(name);
        } else {
            System.err.println("Error: cannot access asset " + name + " while loader is running.");
            System.exit(1);
        }
        if (asset == null) {
            OutputManager.fatalAlert("Error: asset " + name + " has not been loaded.");
        }
        return asset;
    }

    public void stop() {
        executor.shutdownNow();
        unload();
    }

    public void unload() {
        if (isAvailable()) {
            assets.clear();
        } else {
            System.err.println("Error: cannot unload assets while loader is running.");
            System.exit(1);
        }
    }

    public void unload(String[] fileNames) {
        if (isAvailable()) {
            for (String fileName : fileNames) {
                assets.remove(fileName);
            }
        } else {
            System.err.println("Error: cannot unload assets while loader is running.");
            System.exit(1);
        }
    }

    private static URI ABS_PATH = null;
    static {
        try {
            ABS_PATH = Main.DEBUG ?
                    Paths.get("").toAbsolutePath().toUri() :
                    AssetManager.class.getProtectionDomain()
                            .getCodeSource().getLocation().toURI().resolve(".");
        } catch(URISyntaxException e) {
            OutputManager.fatalAlert("Error: Failed to locate class location");
        }
    }

    private static File resolveFile(String relativePath) {

        File file = new File(ABS_PATH.resolve(relativePath));

        if (!file.exists())
            OutputManager.fatalAlert("Error: Failed to locate resource: " + relativePath);

        return file;
    }

    //TODO temp, should be private
    public static AudioClip loadShortAudio(String name) {

        File file = resolveFile("resources/Audio/" + name);

        try {
            return new AudioClip(file.toURI().toString());
        } catch(Exception e) {
            e.printStackTrace();
            OutputManager.fatalAlert("Error: Failed to load AudioClip: " + name);
        }
        return null;
    }

    private Media loadLongAudio(String name) {

        File file = resolveFile("resources/Audio/" + name);

        try {
            return new Media(file.toURI().toString());
        } catch(Exception e) {
            e.printStackTrace();
            OutputManager.fatalAlert("Error: Failed to load Media: " + name);
        }
        return null;
    }

    private BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(resolveFile(path));
        } catch (IOException e) {
            e.printStackTrace();
            OutputManager.fatalAlert("Error: failed to read image file: " + path);
        }
        if (image == null) {
            OutputManager.fatalAlert("Error: image path " + path + " is null");
        }
        return ImageUtils.getCompatableVersion(image);
    }

    private BufferedImage[] loadAnimation(String path) {
        List<BufferedImage> frames = new ArrayList<>();

        File folder = resolveFile(path);
        if (!folder.isDirectory()) {
            OutputManager.fatalAlert("Error: path '" + path + "' is not a directory");
        }

        File[] files = folder.listFiles();
        if (files == null) {
            OutputManager.fatalAlert("Error: animation directory " + path + " is empty");
        }

        for (File file : files) {
            if (file.isFile()) {
                frames.add(loadImage(path + "/" + file.getName()));
            }
        }
        return frames.toArray(new BufferedImage[frames.size()]);
    }

}
