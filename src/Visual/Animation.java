package Visual;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 2/26/2015.
 */
public class Animation {

    public int w, h;

    private float orient;
    private int x, y, cornerX, cornerY, frame, length, step, currentStep, frameRate;
    private BufferedImage[] sprites;

    public boolean state;

    public Animation(float _x, float _y, float _orient, int _frameRate, BufferedImage[] _sprites) {
        x = (int)_x;
        y = (int)_y;
        frameRate = _frameRate;
        orient = _orient;
        sprites = _sprites;
        length = sprites.length;
        w = sprites[0].getWidth();
        h = sprites[0].getHeight();
        cornerX = (int)(x - w * 0.5f);
        cornerY = (int)(y - h * 0.5f);
        frame = 0;
        state = true;
    }

    public void draw(Graphics2D g) {
        AffineTransform oldTForm = g.getTransform();
        if (orient != 0) g.rotate(orient, x, y);
        g.drawImage(sprites[frame], cornerX, cornerY, w, h, null);
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
