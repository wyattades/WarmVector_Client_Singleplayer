package StaticManagers;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/17/2015.
 */
public class OutputManager {

    //TODO: use Float instead of Integer to store settings

    private static List<String> defaultSettings = Arrays.asList(
            "fullscreen 1",
            "quality 0",
            "anti_aliasing 0",
            "music_volume 2",
            "sfx_volume 2",
            "x_sensitivity 1",
            "y_sensitivity 1"
    );


    private static HashMap<String, Integer> settings;
    private static String filePath = "";
    static {

        System.out.println("Loading settings...");

        JFileChooser fr = new JFileChooser();
        FileSystemView fw = fr.getFileSystemView();
        filePath = fw.getDefaultDirectory().getPath() + "/My Games/WarmVector/config";

        File saveFile = new File(filePath);
        if (!saveFile.exists()) {
            saveFile.getParentFile().mkdirs();
            try {
                saveFile.createNewFile();
                Files.write(saveFile.toPath(), defaultSettings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Scanner sc = null;
        try {
            sc = new Scanner(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not locate settings file");
            System.exit(1);
        }
        List<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        if (lines.size() != defaultSettings.size()) {
            try {
                Files.write(saveFile.toPath(), defaultSettings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        settings = new HashMap<>();

        for (String s : lines) {

            String[] line = s.split(" ");

            //           name     value
            settings.put(line[0], Integer.parseInt(line[1]));

        }

    }

    public static int getSetting(String name) {
        try {
            return settings.get(name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not locate setting: " + name);
            System.exit(1);
        }
        return 0;
    }

    public static void saveAllSettings() {

        List<String> settingsList = new ArrayList<>();
        Iterator it = settings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            settingsList.add(pair.getKey() + " " + pair.getValue());
        }

        try {
            Files.write((new File(filePath)).toPath(), settingsList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void setSetting(String name, int value) {
        try {
            settings.replace(name, value);
        } catch (Exception e) {
            System.out.println("Could not locate setting to replace: " + name);
        }

        //TODO: don't do this somehow
        if (name.equals("sfx_volume") || name.equals("music_volume")) {
            AudioManager.updateSettings();
        }

    }

}
