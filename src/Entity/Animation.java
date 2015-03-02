package Entity;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Wyatt on 2/26/2015.
 */
public class Animation {

    public double x,y,dx,dy,w,h,orient;
    private int frame,length,step,currentStep,frameRate;
    private BufferedImage[] sprites;
    public boolean state;

    public Animation(double x, double y, double orient, int frameRate, Color hitColor, BufferedImage[] sprites) {
        this.x = x;
        this.y = y;
        frame = 0;
        this.orient = orient;
        length = sprites.length;
        w = sprites[0].getWidth();
        h = sprites[0].getHeight();
        this.sprites = new BufferedImage[length];
        for (int i = 0; i < length; i++) {
            this.sprites[i] = colorImage(sprites[i],hitColor);
        }
        this.frameRate = frameRate;
        state = true;
    }

    public static BufferedImage colorImage(BufferedImage loadImg ,Color tint) {
        BufferedImage img = new BufferedImage(loadImg.getWidth(), loadImg.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D g = img.createGraphics();
        g.setXORMode(tint);
        g.drawImage(loadImg, null, 0, 0);
        g.dispose();
        return img;
    }

    public void draw(Graphics2D g) {
        AffineTransform oldTForm = g.getTransform();
        g.rotate(orient, dx, dy);
        g.drawImage(sprites[frame], (int)(dx-w/2),(int)(dy-h/2),null);
        g.setTransform(oldTForm);
    }

    public void update() {
        currentStep++;
        if(currentStep-step>frameRate) {
            step = currentStep;
            frame++;
            if (frame+1 > length) {
                state = false;
            }
        }
    }

    public void updateDispPos(double px, double py) {
        dx = Entity.dispPosX(x,px);
        dy = Entity.dispPosY(y,py);
    }
}
