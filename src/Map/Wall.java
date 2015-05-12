package Map;

import java.awt.geom.Point2D;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/19/2015.
 */
public class Wall {

    public Point2D[] vertices;
    private int[][] tiles;
    private int x;
    private int y;

    public Wall(int ix, int iy, int direction, int[][] tiles) {
        int ix1 = x = ix;
        int iy1 = y = iy;
        vertices = new Point2D[4];
        this.tiles = tiles;
        switch(direction) {
            case(0):
                if (tileRight()) {
                    x++;
                }
        }

    }

    private boolean tileLeft() {
        return tiles[x-1][y] == tiles[x][y];
    }

    private boolean tileRight() {
        return tiles[x+1][y] == tiles[x][y];
    }

    private boolean tileUp() {
        return tiles[x][y-1] == tiles[x][y];
    }

    private boolean tileDown() {
        return tiles[x][y+1] == tiles[x][y];
    }
}
