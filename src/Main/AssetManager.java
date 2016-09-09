package Main;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    public AssetManager() {
        assets = new ConcurrentHashMap<>();
    }

    public void loadAssets(String[] fileNames) {
        if (!isAvailable()) {
            OutputManager.printError("Error: cannot add to loading queue while loader is running." + Arrays.toString(fileNames));
            System.exit(1);
        }

        executor = Executors.newSingleThreadExecutor();
        for (String fileName : fileNames) {

            if (!assets.containsKey(fileName)) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (fileName.endsWith(".mp3")) {
                            assets.put(fileName, loadLongAudio(fileName));
                        } else if (fileName.endsWith(".wav")) {
                            assets.put(fileName, loadShortAudio(fileName));
                        } else if (fileName.endsWith(".png")) {
                            assets.put(fileName, loadImage("resources/Images/" + fileName));
                        } else if (fileName.endsWith("_")) {
                            assets.put(fileName, loadAnimation("resources/Animations/" + fileName));
                        } else {
                            OutputManager.printError("Error: invalid file type requested.");
                            System.exit(1);
                        }
                    }
                });
            }
        }
        executor.shutdown();
    }

    public boolean isAvailable() {
        return executor == null || executor.isTerminated();
    }

    public Object getAsset(String name) {
        Object asset = null;
        if (isAvailable()) {
            asset = assets.get(name);
        } else {
            OutputManager.printError("Error: cannot access asset " + name + " while loader is running.");
            System.exit(1);
        }
        if (asset == null) {
            OutputManager.printError("Error: asset " + name + " has not been loaded.");
            System.exit(1);
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
            OutputManager.printError("Error: cannot unload assets while loader is running.");
            System.exit(1);
        }
    }

    public void unload(String[] fileNames) {
        if (isAvailable()) {
            for (String fileName : fileNames) {
                assets.remove(fileName);
            }
        } else {
            OutputManager.printError("Error: cannot unload assets while loader is running.");
            System.exit(1);
        }
    }


    private AudioClip loadShortAudio(String name) {

        String path = "resources/Audio/" + name;

        return new AudioClip(Paths.get(path).toUri().toString());
    }

    private Media loadLongAudio(String name) {

        String path = "resources/Audio/" + name;

        return new Media(Paths.get(path).toUri().toString());
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
//            return ImageIO.read(getClass().getResourceAsStream(name));
        } catch (IOException e) {
            OutputManager.printError(e);
            OutputManager.printError("Error: failed to load image file: " + path);
            System.exit(1);
        }
        return null;
    }

    private BufferedImage[] loadAnimation(String path) {
        List<BufferedImage> frames = new ArrayList<>();
        try {
//            File folder = new File(getClass().getResource(path).toURI());
            File folder = new File(path);
            if (!folder.exists()) {
                OutputManager.printError("Error: could not locate animation directory: " + path);
                System.exit(1);
            }
            File[] files = folder.listFiles();
            if (files == null) {
                OutputManager.printError("Error: animation directory " + path + " is empty");
                System.exit(1);
            }
            for (File file : files) {
                if (file.isFile()) {
                    frames.add(loadImage(path + "/" + file.getName()));
                }
            }
        } catch (Exception e) {
            OutputManager.printError(e);
            OutputManager.printError("Error: failed to load animation files: " + path);
            System.exit(1);
        }
        return frames.toArray(new BufferedImage[frames.size()]);
    }

}
