package StaticManagers;

import StaticManagers.AudioManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by wyatt on 8/17/2015.
 */
public class OutputManager {

    public static String filePath = "out/production/WarmVector_Client_Singleplayer/Output/config";

    private static String[] settings;
    static {
        settings = loadSettings();

        //TEMP FOR DEBUGGING
//        for (int i = 0; i < settings.length; i++) {
//            System.out.println(settings[i]);
//        }
    }

    public static int getSettingValue(String name) {

        for (int i = 0; i < settings.length; i++) {
            String settingName = settings[i].split(" ")[0];
            if (settingName.equals(name)) {
                String value = settings[i].split(" ")[1];
                return Integer.parseInt(value);
            }
        }

        System.out.println("Cannot locate setting (to get): " + name);
        System.exit(1);
        return 0;
    }

    public static void setSetting(String name, int value) {
        String newData = name + " " + value;
        for (int i = 0; i < settings.length; i++) {
            String settingName = settings[i].split(" ")[0];
            if (settingName.equals(name)) {
                File file = new File(filePath);
                List<String> lines;
                try {
                    lines = Files.readAllLines(file.toPath());
                    lines.set(i, newData);
                    Files.write(file.toPath(), lines);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;

            }
        }
        System.out.println("Cannot locate setting (to set): " + name);
        System.exit(1);
    }

    public static void reloadSettings() {
        settings = loadSettings();
        AudioManager.updateSettings();
    }

    private static String[] loadSettings() {

        Scanner sc = null;
        try {
            sc = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        List<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        return lines.toArray(new String[lines.size()]);

    }

}
