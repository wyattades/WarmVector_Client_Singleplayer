package Util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/Util/
 * Created by Wyatt on 8/29/2016.
 */
public abstract class ImageFilter {

    private final static float ratio255 = 1.0f / 255.0f;

    public static BufferedImage recolorImage(BufferedImage original, Color color) {
        int width = original.getWidth();
        int height = original.getHeight();
        BufferedImage tinted = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D graphics = (Graphics2D) tinted.getGraphics();
        graphics.drawImage(original, 0, 0, width, height, null);
        Color n = new Color(0, 0, 0, 0);
        BufferedImage tint = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (tinted.getRGB(i, j) != n.getRGB()) {
                    tint.setRGB(i, j, color.getRGB());
                }
            }
        }
        graphics.drawImage(tint, 0, 0, null);
        graphics.dispose();
        return tinted;
    }

    public static BufferedImage[] recolorAnimation(BufferedImage[] originals, Color color) {
        BufferedImage[] result = new BufferedImage[originals.length];
        for (int i = 0; i < originals.length; i++) {
            result[i] = recolorImage(originals[i], color);
        }
        return result;
    }

    public static BufferedImage colorizeImage(BufferedImage original, float hue) {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                result.setRGB(i, j, Color.HSBtoRGB(hue, 1.0f, (original.getRGB(i, j) & 0xFF) * ratio255));
            }
        }
        return result;
    }

//    public static BufferedImage[] colorizeAnimation(BufferedImage[] originals, float hue) {
//        BufferedImage[] result = new BufferedImage[originals.length];
//        for (int i = 0; i < originals.length; i++) {
//            result[i] = colorizeImage(originals[i], hue);
//        }
//        return result;
//    }

}
