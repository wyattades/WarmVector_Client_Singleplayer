package Helper;

/**
 * Directory: WarmVector_Client_Singleplayer/Helper/
 * Created by Wyatt on 12/13/2015.
 */
public class MyMath {

    public static float map(float value, float a1, float a2, float b1, float b2) {
        return b1 + (value - a1)*(b2 - b1)/(a2 - a1);
    }

    public static float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    //Rounds to nearest multiple of v from input i
    public static int round(float i, int v) {
        return Math.round(i / v) * v;
    }


}
