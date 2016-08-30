package Util;

/**
 * Directory: WarmVector_Client_Singleplayer/Util/
 * Created by Wyatt on 12/13/2015.
 */
public abstract class MyMath {

    // Custom constants
    public static final float PI = 3.141593f,
                        HALF_PI = 1.570796f,
                        TWO_PI = 6.283185f;

    // Maps a value in one range (a1 -> a2) to another range (b1 -> b2)
    public static float map(float value, float a1, float a2, float b1, float b2) {
        return b1 + (value - a1)*(b2 - b1)/(a2 - a1);
    }

    // Random number between two values
    public static float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    // Rounds to nearest multiple of v from input i
    public static int round(float i, int v) {
        return Math.round(i / v) * v;
    }

    // Lerp
    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }


}
