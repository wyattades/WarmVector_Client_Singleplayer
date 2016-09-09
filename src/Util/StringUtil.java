package Util;

/**
 * Directory: WarmVector_Client_Singleplayer/Util/
 * Created by Wyatt on 9/8/2016.
 * Last edited by Wyatt on 9/8/2016.
 */
public abstract class StringUtil {

    public static String randomAsset(String input, int max) {
        int random = (int) MyMath.random(0.0, max + 1.0);
        return String.format(input, random);
    }

}