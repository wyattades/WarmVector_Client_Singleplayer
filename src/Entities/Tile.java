package Entities;

import Map.TileMap;

import java.awt.*;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 1/22/2015.
 */
public class Tile extends Entity {
    protected Tile(float x, float y, float orient) {
        super(x, y, orient);
    }

    //
//    public int kind;
//
//    public Tile(int x, int y, int kind) {
//        super(x, y, 0);
//        w = TileMap.tileSize;
//        h = TileMap.tileSize;
//        this.x = x;
//        this.y = y;
//        this.kind = kind;
//        if (kind == TileMap.SOLID) hitColor = Color.black;
//        else if (kind == TileMap.WINDOW) hitColor = new Color(90, 148, 255);
//    }
//
//    public Tile(int x, int y, int w, int h, int kind) {
//        super(x, y, 0);
//        this.x = x;
//        this.y = y;
//        this.w = w;
//        this.h = h;
//        this.kind = kind;
//        if (kind == TileMap.SOLID) hitColor = Color.black;
//        else if (kind == TileMap.WINDOW) hitColor = new Color(90, 148, 255);
//    }
//
    public boolean hit(int amount, float angle) {
      //  return kind == TileMap.WINDOW;
        return false;
    }
}
