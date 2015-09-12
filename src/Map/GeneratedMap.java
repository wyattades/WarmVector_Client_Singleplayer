package Map;

import Entity.Tile;
import Main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Map.TileMap.*;

/**
 * Created by wyatt on 8/27/2015.
 */
public class GeneratedMap {

    final int roomSize = 8;
    public ArrayList<Tile> walls;

    public GeneratedMap(int x, int y) {
        walls = new ArrayList<Tile>();
        addWalls(x,y);
    }

    void addWalls(int roomsX, int roomsY) {
        for (int i = 0; i < roomsX; i++) {
            for (int j = 0; j < roomsY; j++) {
                float random = Game.random(0.0f,6.0f);
                if (random < 1.0f) {
                    walls.add(new Tile((int)((i+0.5f)*roomSize*TileMap.tileSize),0,TileMap.tileSize,TileMap.tileSize*roomSize,TileMap.SOLID));
                } else if (random < 2.0f) {
                    walls.add(new Tile(0, (int)((j+0.5f)*roomSize*TileMap.tileSize),TileMap.tileSize,TileMap.SOLID,TileMap.tileSize*roomSize));
                } else if (random < 2.5f){
                    //Don't create anything
                } else {
                    walls.add(new Tile((int)((i+0.5f)*roomSize*TileMap.tileSize),0,TileMap.tileSize,TileMap.tileSize*roomSize,TileMap.SOLID));
                    walls.add(new Tile(0, (int)((j+0.5f)*roomSize*TileMap.tileSize),TileMap.tileSize,TileMap.SOLID,TileMap.tileSize*roomSize));
                }
            }
        }
    }

}
