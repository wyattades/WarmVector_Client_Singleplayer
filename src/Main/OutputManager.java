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
    private static String filePath = "";
    private static HashMap<String, Integer> settings;
    static {
        settings = new HashMap<>();
        settings.put("fix_bugs", 0);
        settings.put("quality", 0);
        settings.put("cave_mode", 0);
        settings.put("music_volume", 2);
        settings.put("sfx_volume", 2);
        settings.put("x_sensitivity", 1);
        settings.put("y_sensitivity", 1);
        settings.put("level", 1);

        printLog("Loading Settings...");

        JFileChooser fr = new JFileChooser();
        FileSystemView fw = fr.getFileSystemView();
        filePath = fw.getDefaultDirectory().getPath() + "/My Games/WarmVector/config";

        File saveFile = new File(filePath);
        if (saveFile.exists()) {
            Scanner sc = null;
            try {
                sc = new Scanner(new File(filePath));
            } catch (Exception e) {
                printError(e);
                printError("Error: Could not locate settings file");
                System.exit(1);
            }
            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(" ");
                if (line.length == 2) {
                    String name = line[0];
                    String value = line[1];
                    if (settings.containsKey(name) && isInteger(value)) {
                        settings.replace(name, Integer.parseInt(value));
                    }
                }
            }

        }

        printLog("Successfully loaded settings.");

    }

    private static boolean isInteger(String input){
        try{
            Integer.parseInt(input);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }

    public static int getSetting(String name) {
        try {
            return settings.get(name);
        } catch (Exception e) {
            printError(e);
            printLog("Error: Could not locate setting to get: " + name);
            System.exit(1);
        }
        return 0;
    }

    public static void setSetting(String name, int value) {
        try {
            settings.replace(name, value);
        } catch (Exception e) {
            printError("Could not locate setting to replace: " + name);
        }
    }

    public static void saveAllSettings() {

        printLog("Saving data...");

        List<String> settingsList = new ArrayList<>();
        for (Object o : settings.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            settingsList.add(pair.getKey() + " " + pair.getValue());
        }

        File saveFile = new File(filePath);
        if (!saveFile.exists()) {
            saveFile.getParentFile().mkdirs();
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                printError(e);
                printError("Error: failed to create new save file.");
                System.exit(1);
            }
        }

        try {
            Files.write(saveFile.toPath(), settingsList);
        } catch (IOException e) {
            printError(e);
            printError("Error: failed to write to save file.");
            System.exit(1);
        }

        printLog("Successfully saved all data.");

    }

}
