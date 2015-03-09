package Map;

import Entity.Enemy;
import Entity.Entity;
import Entity.ThisPlayer;
import Entity.Tile;
import Entity.Weapon.M4rifle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Wyatt on 1/26/2015.
 */
public class TileMap {
    private BufferedImage background;
    public int[][] tileArray;
    private int width;
    private int height;
    private int dx;
    private int dy;
    private static final int EMPTY = 0;
    public static final int SOLID = 1;
    public static final int WINDOW = 2;
    private static final int SPAWN = 3;
    private static final int ENEMY = 4;

    public static final int tileSize = 16;

    public TileMap(int level, BufferedImage mapImage, BufferedImage background) {
        this.background = background;
        width = mapImage.getWidth();
        height = mapImage.getHeight();
        tileArray = setTileArray(mapImage);
    }

    public void draw(Graphics2D g) {
        g.drawImage(background, dx, dy, null);
    }

    public void updateDispPos(double px, double py) {
        dx = Entity.dispPosX(0, px);
        dy = Entity.dispPosY(0, py);
    }

    int[][] setTileArray(BufferedImage image) {
        int[][] tArray = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = image.getRGB(i, j);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                int a = (pixel >> 24) & 0xFF;
                Color color = new Color(r, g, b, a);
                if (color.equals(new Color(0, 0, 0, 255))) {
                    tArray[i][j] = SOLID;
                } else if (color.equals(new Color(0, 255, 0, 255))) {
                    tArray[i][j] = SPAWN;
                } else if (color.equals(new Color(0, 0, 255, 255))) {
                    tArray[i][j] = WINDOW;
                } else if (color.equals(new Color(255, 0, 0, 255))) {
                    tArray[i][j] = ENEMY;
                } else {
                    tArray[i][j] = EMPTY;
                }
                if (i == 0 || i == width - 1 || j == 0 || j == height - 1) tArray[i][j] = SOLID;
            }
        }
        return tArray;
    }

    public HashMap<String, ArrayList<Entity>> setEntities() {
        HashMap<String, ArrayList<Entity>> values = new HashMap<String, ArrayList<Entity>>();
        values.put("thisPlayer", new ArrayList<Entity>());
        values.put("enemy", new ArrayList<Entity>());
        values.put("weapon", new ArrayList<Entity>());
        values.put("tile", new ArrayList<Entity>());
        values.put("bullet", new ArrayList<Entity>());
        values.get("thisPlayer").add(new ThisPlayer(400, 400, 4 * tileSize, 4 * tileSize, 0, new M4rifle(0, 0, 0, 0, 0, M4rifle.maxAmmo), this, values.get("tile")));
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int t = tileArray[i][j];
                if (t == TileMap.SOLID) {
                    values.get("tile").add(new Tile((i + 0.5) * tileSize, (j + 0.5) * tileSize, TileMap.SOLID));
                } else if (t == TileMap.WINDOW) {
                    values.get("tile").add(new Tile((i + 0.5) * tileSize, (j + 0.5) * tileSize, TileMap.WINDOW));
                } else if (t == TileMap.SPAWN) {
                    values.get("thisPlayer").get(0).x = i * tileSize;
                    values.get("thisPlayer").get(0).y = j * tileSize;
                } else if (t == TileMap.ENEMY) {
                    values.get("enemy").add(new Enemy(i * tileSize, j * tileSize, 4 * tileSize, 4 * tileSize, 0, new M4rifle(0, 0, 64, 32, 0, M4rifle.maxAmmo), this, values.get("tile")));
                }
            }
        }
        return values;
    }

}
