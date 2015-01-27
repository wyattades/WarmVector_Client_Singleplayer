package Map;

import Manager.FileManager;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Wyatt on 1/26/2015.
 */
public class TileMap {

    BufferedImage mapImage;
    public int[][] mapArray;
    int width,height;

    public static final int EMPTY = 0;
    public static final int SOLID = 1;
    static final int WINDOW = 2;
    static final int SPAWN = 3;
    boolean createdSpawnPoint;
    int level;
    public int tileSize;

    public TileMap(int level, int tileSize) {
        this.level = level;
        this.tileSize = tileSize;
        mapImage = FileManager.TILESET1;
    }

    public void setTileArray() {
        width = mapImage.getWidth();
        height = mapImage.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int c = mapImage.getRGB(i, j);
                int  red = (c & 0x00ff0000) >> 16;
                int  green = (c & 0x0000ff00) >> 8;
                int  blue = c & 0x000000ff;
                Color color = new Color(red,green,blue);
                if (color == Color.black) mapArray[i][j] = SOLID;
                else if (color == Color.green && createdSpawnPoint == false) {
                    mapArray[i][j] = SPAWN;
                    createdSpawnPoint = true;
                }
                else if (color == Color.blue) mapArray[i][j] = WINDOW;
                else mapArray[i][j] = EMPTY;
            }
        }
    }

}
