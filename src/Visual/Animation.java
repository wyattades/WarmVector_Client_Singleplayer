package Visual;

import Entity.Entity;

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
    private Color hitColor;

    public Animation(double x, double y, double orient, int frameRate, Color hitColor, BufferedImage[] _sprites) {
        this.x = x;
        this.y = y;
        frame = 0;
        this.hitColor = hitColor;
        this.orient = orient+Math.PI;
        length = _sprites.length;
        w = _sprites[0].getWidth();
        h = _sprites[0].getHeight();
        sprites = new BufferedImage[length];
        for (int i = 0; i < length; i++) {
            sprites[i] = tintImage(_sprites[i],hitColor);
        }
        //sprites = _sprites;
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

    public static BufferedImage tintImage(Image original, Color c){
        int width = original.getWidth(null);
        int height = original.getHeight(null);
        BufferedImage tinted = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D graphics = (Graphics2D) tinted.getGraphics();
        graphics.drawImage(original, 0, 0, width, height, null);
        Color n = new Color(0,0,0,0);
        BufferedImage tint = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        for(int i = 0 ; i < width ; i++){
            for(int j = 0 ; j < height ; j++){
                if(tinted.getRGB(i, j) != n.getRGB()){
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
        dx = Entity.dispPosX(x, px);
        dy = Entity.dispPosY(y,py);
    }
}
