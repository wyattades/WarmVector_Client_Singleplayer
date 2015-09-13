package Map;

import Entity.Tile;
import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.TileObserver;
import java.util.ArrayList;

import static Map.TileMap.*;

/**
 * Created by wyatt on 8/27/2015.
 */
public class GeneratedMap {

    private int width,height;

    private float factor;

    public ArrayList<Tile> walls;
    public ArrayList<Rect> cells;


    public GeneratedMap(int width, int height, float factor) {
        this.width = width;
        this.height = height;
        this.factor = factor;
        walls = new ArrayList<Tile>();
        addWalls();
    }

    private class Cell {

        public Cell cellA, cellB;

        private static final int leeWay = 200;

        public int x,y,w,h;

        public Cell(int x, int y, int w, int h, int iteration, int max_iteration) {

            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;

            if (iteration + 1 <= max_iteration) {
                //float randDir = Game.random(0.0f, 100.0f);
                if (h < w) { //new horizontal box
                    int randomBetween = (int) Game.random(leeWay, w-leeWay);
                    if (randomBetween < 0) randomBetween = w/2;
                    cellA = new Cell(x, y, randomBetween, h, iteration + 1, max_iteration);
                    cellB = new Cell(x + randomBetween, y, w - randomBetween, h, iteration + 1, max_iteration);
                } else { //new vertical box
                    int randomBetween = (int) Game.random(leeWay, h-leeWay);
                    if (randomBetween < 0) randomBetween = h/2;
                    cellA = new Cell(x, y, w, randomBetween, iteration + 1, max_iteration);
                    cellB = new Cell(x, y + randomBetween, w, h - randomBetween, iteration + 1, max_iteration);
                }
                if (iteration + 2 == max_iteration) {
                    cells.add(getRectFromCell(cellA.cellA));
                    cells.add(getRectFromCell(cellA.cellB));
                    cells.add(getRectFromCell(cellB.cellA));
                    cells.add(getRectFromCell(cellB.cellB));
                }
            }

        }
    }

    private class Rect {

        public int x,y,w,h;

        public Rect(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    protected Rect getRectFromCell(Cell c) {
        return new Rect(c.x,c.y,c.w,c.h);
    }

    void addWalls() {

        //create empty array of rects
        cells = new ArrayList<Rect>();

        //add randomized subsection rects to the array
        Cell fatherCell = new Cell(0, 0, width,height, 0, 4);

        //randomly shrink the rects
        int maxReduction = 50;
        for (Rect r : cells) {
            int x_left = (int)Game.random(0,maxReduction);
            int x_right = (int)Game.random(0,maxReduction);
            r.x += x_left;
            r.w += -x_left - x_right;
            int y_top = (int)Game.random(0,maxReduction);
            int y_bottom = (int)Game.random(0,maxReduction);
            r.y += y_top;
            r.h += -y_top - y_bottom;
        }

        //add corridors to the sister-pair rooms
        int thickness = 60;
        int length = cells.size();
        for (int i = 0; i < length; i += 2) {

            Rect r1 = cells.get(i),
                 r2 = cells.get(i+1);
//            int chanceOf2Segments = (int)Game.random(1,1.7f);
//            for (int j = 0; j < chanceOf2Segments; j++) {
                if (Math.abs(r1.x - r2.x) > Math.abs(r1.y - r2.y)) { //If the sister rooms are horizontally adjacent...

                    int randomY = (int) Game.random(Math.max(r1.y, r2.y), Math.min(r1.y + r1.h, r2.y + r2.h) - thickness);
                    cells.add(new Rect(r1.x + r1.w, randomY, r2.x - (r1.x + r1.w), thickness));

                } else { //or vertically adjacent...

                    int randomX = (int) Game.random(Math.max(r1.x, r2.x), Math.min(r1.x + r1.w, r2.x + r2.w) - thickness);
                    cells.add(new Rect(randomX, r1.y + r1.h, thickness, r2.y - (r1.y + r1.h)));

                }
//            }
        }

        for (int i = 0; i < length; i += 4) {
            //Rooms
            Rect r1, r2;
            int randomRoom = (int)Game.random(0,3);
            if (randomRoom == 0) {
                r1= cells.get(i);
                r2 = cells.get(i+2);

            } else if (randomRoom == 1) {
                r1 = cells.get(i+1);
                r2 = cells.get(i+3);

            } else if (randomRoom == 2) {
                r1 = cells.get(i/4);
                r2 = cells.get(i/4 + 2);
            }

            //Corridors

            //If the sister rooms are horizontally adjacent...
            if (Math.abs(r1.x-r2.x) > Math.abs(r1.y - r2.y)) {
                int randomY = (int)Game.random(Math.max(r1.y, r2.y),  Math.min(r1.y + r1.h, r2.y + r2.h) - thickness);
                cells.add(new Rect(r1.x+r1.w, randomY, r2.x-(r1.x+r1.w), thickness));
            } else { //or vertically adjacent...
                int randomX = (int)Game.random(Math.max(r1.x, r2.x), Math.min(r1.x + r1.w, r2.x + r2.w) - thickness);
                cells.add(new Rect(randomX, r1.y+r1.h, thickness, r2.y-(r1.y+r1.h)));
            }

        }

        //TEMP
        //display rects
        for (Rect r : cells) {
            walls.add(new Tile(r.x+r.w/2, r.y+r.h/2, r.w, r.h, TileMap.SOLID));
        }

    }

}
