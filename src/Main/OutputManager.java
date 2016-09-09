package Main;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/17/2015.
 */
public class OutputManager {

    // Create/overwrite log file
    private static String logPath = "";
    static {
        JFileChooser fr = new JFileChooser();
        FileSystemView fw = fr.getFileSystemView();
        logPath = fw.getDefaultDirectory().getPath() + "/My Games/WarmVector/log.txt";

        File saveFile = new File(logPath);
        if (!saveFile.exists()) {
            saveFile.getParentFile().mkdirs();
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to create log file.");
                System.exit(1);
            }
        }

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(logPath);
        } catch (FileNotFoundException e) {
            printError(e);
            e.printStackTrace();
            System.exit(1);
        }
        pw.close();

        printLog("\n--- LOG START ---");
    }

    private static void writeLog(String line) {
        try(FileWriter fw = new FileWriter(logPath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not write to log file.");
            System.exit(1);
        }
    }

    public static void printLog(String line) {
        System.out.println(line);
        writeLog(line);
    }

    public static void printError(String line) {
        System.err.println(line);
        writeLog(line);
    }

    public static void printError(Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        throwable.printStackTrace();
        printLog(sw.getBuffer().toString());
    }

    //TODO: use double instead of Integer to store settings??

    private static final List<String> defaultSettings = Arrays.asList(
            "fix_bugs 0",
            "quality 0",
            "cave_mode 0",
            "music_volume 2",
            "sfx_volume 2",
            "x_sensitivity 1",
            "y_sensitivity 1",
            "level 1"
    );
    private static final int settingsSize = defaultSettings.size();

    private static HashMap<String, Integer> settings;
    private static String filePath = "";
    static {

        printLog("Loading Settings...");

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
                printError(e);
            }
        }

        Scanner sc = null;
        try {
            sc = new Scanner(new File(filePath));
        } catch (Exception e) {
            printError(e);
            printError("Error: Could not locate settings file");
            System.exit(1);
        }
        List<String> lines = new ArrayList<>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        if (lines.size() != settingsSize) {
            try {
                Files.write(saveFile.toPath(), defaultSettings);
            } catch (IOException e) {
                printError(e);
            }
        }

        settings = new HashMap<>();

        for (String s : lines) {

            String[] line = s.split(" ");

            //           name     value
            settings.put(line[0], Integer.parseInt(line[1]));

        }

        printLog("Successfully loaded settings.");

    }

    public static int getSetting(String name) {
        try {
            return settings.get(name);
        } catch (Exception e) {
            printError(e);
            printLog("Error: Could not locate setting: " + name);
            System.exit(1);
        }
        return 0;
    }

    public static void saveAllSettings() {

        printLog("Saving data...");

        List<String> settingsList = new ArrayList<>();
        Iterator it = settings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            settingsList.add(pair.getKey() + " " + pair.getValue());
        }

        try {
            Files.write((new File(filePath)).toPath(), settingsList);
        } catch (IOException e) {
            printError(e);
            printError("Error: Failed to save data.");
            System.exit(1);
        }

        printLog("Successfully saved all data.");

    }

    public static void setSetting(String name, int value) {
        try {
            settings.replace(name, value);
        } catch (Exception e) {
            printError("Could not locate setting to replace: " + name);
        }

//        //TODO: don't do this somehow
//        if (name.equals("sfx_volume") || name.equals("music_volume")) {
//            AudioManager.updateSettings();
//        }

    }

}
