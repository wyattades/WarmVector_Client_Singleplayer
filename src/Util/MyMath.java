package Util;

import java.util.Random;

/**
 * Directory: WarmVector_Client_Singleplayer/Util/
 * Created by Wyatt on 12/13/2015.
 */
public abstract class MyMath {

    // Custom constants
    public static final double PI = 3.141593,
                        HALF_PI = 1.570796,
                        TWO_PI = 6.283185;

    // Maps a value in one range (a1 -> a2) to another range (b1 -> b2)
    public static double map(double value, double a1, double a2, double b1, double b2) {
        return b1 + (value - a1)*(b2 - b1)/(a2 - a1);
    }

    // Random number between two values
    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    // Random number using seed
    public static double random(double min, double max, Random random) {
        return random.nextDouble() * (max - min) + min;
    }

    // Rounds to nearest multiple of v from input
    public static int round(double input, int multiple) {
        return (int) Math.round(input / multiple) * multiple;
    }

    // Custom round function
    public static int round(double input) {
        int floor = (int) input;

        if (input - floor > 0.5) {
            return floor + 1;
        } else {
            return floor;
        }
    }

    // Lerp
    public static double lerp(double a, double b, double f) {
        return a + f * (b - a);
    }


}
