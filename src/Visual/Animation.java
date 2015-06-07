package Visual;

import Manager.FileManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 2/26/2015.
 */
public class Animation {

    private int x,y,w,h;
    private float orient;
    private int frame, length, step, currentStep, frameRate;
    private BufferedImage[] sprites;
    public boolean state;

    public Animation(int x, int y, float orient, int frameRate, Color hitColor, String fileName) {
        this.x = x;
        this.y = y;
        this.frameRate = frameRate;
        this.orient = orient;
        sprites = FileManager.animations.get(fileName);
        length = sprites.length;
        w = sprites[0].getWidth();
        h = sprites[0].getHeight();
        for (int i = 0; i < length; i++) {
            sprites[i] = tintImage(sprites[i], hitColor);
        }
        frame = 0;
        state = true;
    }

    private static BufferedImage tintImage(BufferedImage original, Color c) {
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
                    tint.setRGB(i, j, c.getRGB());
                }
            }
        }
        graphics.drawImage(tint, 0, 0, null);
        graphics.dispose();
        return tinted;
    }

    public void draw(Graphics2D g) {
        AffineTransform oldTForm = g.getTransform();
        g.rotate(orient, x, y);
        g.drawImage(sprites[frame], x - w / 2, y - h / 2, null);
        g.setTransform(oldTForm);
    }

    public void update() {
        currentStep++;
        if (currentStep - step > frameRate) {
            step = currentStep;
            frame++;
            if (frame + 1 > length) {
                state = false;
            }
        }
    }
}
