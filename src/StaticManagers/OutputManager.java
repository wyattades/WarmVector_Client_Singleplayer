package StaticManagers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/17/2015.
 */
public class OutputManager {

    public static String filePath = "src/Output/config";

    private static String[] settingsList;
    static {

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

        settingsList = lines.toArray(new String[lines.size()]);

    }

    private static HashMap<String, Integer> settings;
    static {

        settings = new HashMap<>();

        for (String s : settingsList) {

            String[] line = s.split(" ");

            //           name     value
            settings.put(line[0], Integer.parseInt(line[1]));

        }

    }

    public static int getSetting(String name) {
        return settings.get(name);
    }

    public static void setSetting(String name, int value) {

        settings.replace(name, value);

        if (name.equals("sfx_volume") || name.equals("music_volume")) {
            AudioManager.updateSettings();
        }

        String newData = name + " " + value;
        for (int i = 0; i < settingsList.length; i++) {
            String settingName = settingsList[i].split(" ")[0];
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

}
