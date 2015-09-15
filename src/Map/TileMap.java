package Map;

import Entities.Enemy;
import Entities.Entity;
import Entities.ThisPlayer;
import Entities.Tile;
import Entities.Weapon.M4rifle;
import StaticManagers.FileManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/26/2015.
 */
public class TileMap {
//    private BufferedImage background, foreground;
//    public int[][] tileArray;
//    public int width;
//    public int height;
//    public static final int EMPTY = 0;
//    public static final int SOLID = 1;
//    public static final int WINDOW = 2;
//    public static final int SPAWN = 3;
//    public static final int ENEMY = 4;
//    //public Color backgroundColor;
//
//    public static final int tileSize = 10;
//
//    public TileMap(int level) {
//        background = FileManager.images.get("background_0" + level + ".png");
//        foreground = FileManager.images.get("foreground_0" + level + ".png");
//        BufferedImage mapImage = FileManager.images.get("leveltiles_0" + level + ".png");
//        width = mapImage.getWidth();
//        height = mapImage.getHeight();
//        tileArray = setTileArray(mapImage);
//    }
//
//
//    public void drawBack(Graphics2D g) {
//        g.drawImage(background, 0, 0, null);
//    }
//
//    public void drawFore(Graphics2D g) {
//        g.drawImage(foreground, 0, 0, null);
//    }
//
//
//    int[][] setTileArray(BufferedImage image) {
//        int[][] tArray = new int[width][height];
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                int pixel = image.getRGB(i, j);
//                int r = (pixel >> 16) & 0xFF;
//                int g = (pixel >> 8) & 0xFF;
//                int b = pixel & 0xFF;
//                int a = (pixel >> 24) & 0xFF;
//                Color color = new Color(r, g, b, a);
//                if (color.equals(new Color(0, 0, 0, 255))) {
//                    //tArray[i][j] = SOLID;
//                } else if (color.equals(new Color(0, 255, 0, 255))) {
//                    tArray[i][j] = SPAWN;
//                } else if (color.equals(new Color(0, 0, 255, 255))) {
//                    //tArray[i][j] = WINDOW;
//                } else if (color.equals(new Color(255, 0, 0, 255))) {
//                    //tArray[i][j] = ENEMY;
//                } else {
//                    tArray[i][j] = EMPTY;
//                }
//                //if (i == 0 || i == width - 1 || j == 0 || j == height - 1) tArray[i][j] = WINDOW;
//            }
//        }
//        return tArray;
//    }
//
//    public HashMap<String, ArrayList<Entity>> setEntities() {
//        HashMap<String, ArrayList<Entity>> values = new HashMap<String, ArrayList<Entity>>();
//        values.put("thisPlayer", new ArrayList<Entity>());
//        values.put("enemy", new ArrayList<Entity>());
//        values.put("weapon", new ArrayList<Entity>());
//        values.put("tile", new ArrayList<Entity>());
//        ThisPlayer tp = new ThisPlayer(0, 0, 0, 0, 0, new M4rifle(0, 0, 0, 0, 0, null), this, values.get("tile"));
//        tp.weapon.user = tp;
//        values.get("thisPlayer").add(tp);
//
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                int t = tileArray[i][j];
//                if (t == SOLID) {
//                    values.get("tile").add(new Tile((int) ((i + 0.5f) * tileSize), (int) ((j + 0.5f) * tileSize), SOLID));
//                } else if (t == WINDOW) {
//                    values.get("tile").add(new Tile((int) ((i + 0.5f) * tileSize), (int) ((j + 0.5f) * tileSize), WINDOW));
//                } else if (t == SPAWN) {
//                    values.get("thisPlayer").get(0).x = i * tileSize;
//                    values.get("thisPlayer").get(0).y = j * tileSize;
//                    tileArray[i][j] = EMPTY;
//                } else if (t == ENEMY) {
//                    Enemy e = new Enemy(i * tileSize, j * tileSize, 0, 0, 0, new M4rifle(0, 0, 64, 32, 0, null), this, values.get("tile"));
//                    e.weapon.user = e;
//                    values.get("enemy").add(e);
//                    tileArray[i][j] = EMPTY;
//                }
//            }
//        }
//        return values;
//    }


}
