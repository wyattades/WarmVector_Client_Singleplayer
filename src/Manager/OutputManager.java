package Manager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by wyatt on 8/17/2015.
 */
public class OutputManager {

    public static void setSetting() {

    }

    public static String[] getSetting() {

        Scanner sc = null;
        try {
            sc = new Scanner(new File("config.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        List<String> lines = new ArrayList<String>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        String[] arr = lines.toArray(new String[0]);

        return arr;

    }

}
