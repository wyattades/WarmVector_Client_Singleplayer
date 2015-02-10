package Map;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Wyatt on 1/26/2015.
 */
public class TileMap {
   // BufferedImage mapImage;
    public int[][] tileArray;
    public int width,height;
    public static final int EMPTY = 0;
    public static final int SOLID = 1;
    public static final int WINDOW = 2;
    public static final int SPAWN = 3;
    public static final int ENEMY = 4;

    int level;
    public static final int tileSize = 16;

    public TileMap(int level, BufferedImage mapImage) {
        this.level = level;
        //this.mapImage = mapImage;
        width = mapImage.getWidth();
        height = mapImage.getHeight();
        tileArray = setTileArray(mapImage);
    }

    public int[][] setTileArray(BufferedImage image) {
        int[][] tArray = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = image.getRGB(i,j);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                int a = (pixel >> 24) & 0xFF;
                Color color = new Color(r,g,b,a);
                if (color.equals(new Color(0, 0, 0, 255))) {
                    tArray[i][j] = SOLID;
                } else if (color.equals(new Color(0,255,0,255))) {
                    tArray[i][j] = SPAWN;
                } else if (color.equals(new Color(0,0,255,255))) {
                    tArray[i][j] = WINDOW;
                }else if (color.equals(new Color(255,0,0,255))){
                    tArray[i][j] = ENEMY;
                } else {
                    tArray[i][j] = EMPTY;
                }
                if (i==0 || i==width-1 || j==0 || j==height-1) tArray[i][j] = SOLID;
            }
        }
        return tArray;
    }

}
