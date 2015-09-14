package Map;

import Entity.Tile;
import Main.Game;
import javafx.scene.Node;

import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.font.NumericShaper;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.TileObserver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.TreeMap;

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

        private static final float
                //Determines how thin the subsections can get
                reduction = 0.2f,
                //Minimum room length/width
                min_size = 30.0f;

        public Rect p;

        public Cell(Rect p, int iteration, int max_iteration) {

            this.p = p;

            if (iteration + 1 <= max_iteration) {
                if ((p.w/2.0f > min_size && p.h/2.0f < min_size) || p.h < p.w) { //new horizontal box
                    float randomFromCenter =  Game.random(-p.w * reduction, p.w * reduction);
                    cellA = new Cell(new Rect(p.x, p.y, (p.w/2.0f) + randomFromCenter, p.h), iteration + 1, max_iteration);
                    cellB = new Cell(new Rect(p.x + (p.w/2.0f) + randomFromCenter, p.y, (p.w/2.0f) - randomFromCenter, p.h), iteration + 1, max_iteration);
                } else { //new vertical box
                    float randomFromCenter = Game.random(-p.h * reduction, p.h * reduction);
                    cellA = new Cell(new Rect(p.x, p.y, p.w, (p.h/2.0f) + randomFromCenter), iteration + 1, max_iteration);
                    cellB = new Cell(new Rect(p.x, p.y + (p.h/2.0f) + randomFromCenter, p.w,( p.h/2.0f) - randomFromCenter), iteration + 1, max_iteration);
                }
                if (iteration + 2 == max_iteration) {
                    cells.add(cellA.cellA.p);
                    cells.add(cellA.cellB.p);
                    cells.add(cellB.cellA.p);
                    cells.add(cellB.cellB.p);
                }
            }

        }
    }

    //Simple custom rectangle class (basically just holds 4 variables)
    public class Rect {

        public float x,y,w,h;

        public Rect(float x, float y, float w, float h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }
//
//    private Rect corridorBetween2(int path_thickness, Rect r1, Rect r2) {
//        if (horizontallyAdjacent(r1,r2)) { //If the sister rooms are horizontally adjacent...
//
//            int randomY = (int) Game.random(Math.max(r1.y, r2.y), Math.min(r1.y + r1.h, r2.y + r2.h) - path_thickness);
//            return new Rect(r1.x + r1.w, randomY, r2.x - (r1.x + r1.w), path_thickness);
//
//        } else { //or vertically adjacent...
//
//            int randomX = (int) Game.random(Math.max(r1.x, r2.x), Math.min(r1.x + r1.w, r2.x + r2.w) - path_thickness);
//            return new Rect(randomX, r1.y + r1.h, path_thickness, r2.y - (r1.y + r1.h));
//
//        }
//    }

    private Rect getRandom(ArrayList<Rect> list) {
        return list.get((int)Game.random(0,list.size()-1));
    }

    private boolean horizontallyAdjacent(Rect r1, Rect r2) {
        return Math.abs(r1.x - r2.x) > Math.abs(r1.y - r2.y);
    }

    private Rect corridorBetween(ArrayList<Rect> others, int path_thickness, ArrayList<Rect> sister1, ArrayList<Rect> sister2) {
        int amount = sister1.size();

        ArrayList<Rect> shuffled1 = new ArrayList<Rect>(sister1);
        ArrayList<Rect> shuffled2 = new ArrayList<Rect>(sister2);
        Collections.shuffle(shuffled1);
        Collections.shuffle(shuffled2);

        Rect corridor;

        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {

                Rect r1 = shuffled1.get(i);
                Rect r2 = shuffled2.get(j);

                if (Math.abs(r1.y + r1.h - r2.y) >= path_thickness || Math.abs(r2.y + r2.h - r1.y) >= path_thickness) {
                    int min = (int) Math.max(r1.y, r2.y);
                    int max = (int) Math.min(r1.y + r1.h, r2.y + r2.h) - path_thickness;
                    ArrayList<Integer> options = new ArrayList<Integer>();
                    for (int k = 0; k < max - min; k++) {
                        options.add(min + k);
                    }
                    Collections.shuffle(options);
                    for (int k = 0; k < options.size(); k++) {
                        corridor = new Rect(r1.x + r1.w, k, r2.x - (r1.x + r1.w), path_thickness);
                        for (Rect o : others) {
                            if (o.x + o.w < corridor.x || o.x > corridor.x + corridor.w || o.y + o.h < corridor.y || o.y > corridor.y + corridor.h) {
                                return corridor;
                            }

                        }
                    }
                } else if (Math.abs(r1.x + r1.w - r2.x) >= path_thickness || Math.abs(r2.x + r2.w - r1.x) >= path_thickness) {
                    int min = (int) Math.max(r1.x, r2.x);
                    int max = (int) Math.min(r1.x + r1.w, r2.x + r2.w) - path_thickness;
                    ArrayList<Integer> options = new ArrayList<Integer>();
                    for (int k = 0; k < max - min; k++) {
                        options.add(min + k);
                    }
                    Collections.shuffle(options);
                    for (int k = 0; k < options.size(); k++) {
                        corridor = new Rect(k, r1.y + r1.h, path_thickness, r2.y - (r1.y + r1.h));
                        for (Rect o : others) {
                            if (o.x + o.w < corridor.x || o.x > corridor.x + corridor.w || o.y + o.h < corridor.y || o.y > corridor.y + corridor.h) {
                                return corridor;
                            }

                        }
                    }
                }
            }
        }
        return null;

//        Rect corridor = null;
//        if (horizontallyAdjacent(r1, r2)) {
//            if (Math.abs(r1.y+r1.h-r2.y) >= path_thickness || Math.abs(r2.y+r2.h-r1.y) >= path_thickness) {
//                boolean collides = true;
//                int i = 0;
//                while(collides) {
//                    int randomY = (int) Game.random(Math.max(r1.y, r2.y), Math.min(r1.y + r1.h, r2.y + r2.h) - path_thickness);
//                    corridor = new Rect(r1.x + r1.w, randomY, r2.x - (r1.x + r1.w), path_thickness);
//                    collides = false;
//                    for (Rect o : others) {
//                        if (o.x + o.w > corridor.x && o.x < corridor.x + corridor.w && o.y + o.h > corridor.y && o.y < corridor.y + corridor.h) {
//                            collides = true;
//                        }
//
//                    }
//                    i++;
//                    if (i > 200) return null;
//                }
//                return corridor;
//            } else {
//                return null;
//            }
//        } else {
//            if (Math.abs(r1.x+r1.w-r2.x) >= path_thickness || Math.abs(r2.x+r2.w-r1.x) >= path_thickness) {
//                boolean collides = true;
//                int i = 0;
//                while(collides) {
//                    int randomX = (int) Game.random(Math.max(r1.x, r2.x), Math.min(r1.x + r1.w, r2.x + r2.w) - path_thickness);
//                    corridor = new Rect(randomX, r1.y + r1.h, path_thickness, r2.y - (r1.y + r1.h));
//                    collides = false;
//                    for (Rect o : others) {
//                        if (o.x + o.w > corridor.x && o.x < corridor.x + corridor.w && o.y + o.h > corridor.y && o.y < corridor.y + corridor.h) {
//                            collides = true;
//                        }
//
//                    }
//                    i++;
//                    if (i > 200) return null;
//                }
//                return corridor;
//            } else {
//                return null;
//            }
//        }
    }

    public void addWalls() {

        //Constants
        float reductionFactor = 0.42f;
        int min_spacing = 4;
        int path_thickness = 4;
        int iterations = 4;


        //create empty array of rectangles
        cells = new ArrayList<Rect>();

        //add randomized subsection rects to the array
        Cell fatherCell = new Cell(new Rect(0, 0, width,height), 0, iterations);

        //randomly shrink the rectangles,
        //this produces the actual room dimensions
        for (Rect r : cells) {
            float oldRight = r.x + r.w,
                    oldBottom = r.y + r.h;
            r.w = Game.random(r.w * reductionFactor, r.w - min_spacing);
            r.x = Game.random(r.x + min_spacing/2.0f, oldRight-r.w-min_spacing/2.0f);
            r.h = Game.random(r.h * reductionFactor, r.h - min_spacing);
            r.y = Game.random(r.y + min_spacing/2.0f, oldBottom - r.h-min_spacing/2.0f);
        }

        int roomsAmount = cells.size();
        ArrayList<Rect> sister1, sister2;
        //TEMP integer instead of "iteration"
        int p = 0;
        for (int i = 0; i < 2; i++) {
            int iterateAmount = (int)Math.pow(2,i);
            for (int j = 0; j < roomsAmount; j += 2*iterateAmount) {
                sister1 = new ArrayList<Rect>();
                sister2 = new ArrayList<Rect>();
                for (int k = 0; k < iterateAmount; k++) {
                    sister1.add(cells.get(j + k));
                    sister2.add(cells.get(j + k + iterateAmount));
                }
                Rect corridor = corridorBetween(cells, path_thickness, sister1, sister2);

//                Rect r1 = sister1.get((int) Game.random(0, sister1.size() - 1));
//                Rect r2 = sister2.get((int) Game.random(0, sister2.size() - 1));
//                Rect corridor = corridorBetween(cells,path_thickness,r1,r2);
//                int x = 0;
//                while (corridor == null) {
//                    r1 = sister1.get((int) Game.random(0, sister1.size() - 1));
//                    r2 = sister2.get((int) Game.random(0, sister2.size() - 1));
//                    corridor = corridorBetween(cells,path_thickness,r1,r2);
//                    x++;
//                    if (x >= 200) {
//                        break;
//                    }
//                }
                if (corridor != null) {
                    cells.add(corridor);
                } else {
                    p++;
                    System.out.println(p);
                }
            }

        }

//        //add corridors to the sister-pair rooms
//        int length = cells.size();
//        ArrayList<Rect> all = new ArrayList<Rect>();
//        all.addAll(cells);
//        for (int i = 0; i < length; i += 2) {
//
//            Rect r1 = cells.get(i),
//                 r2 = cells.get(i+1);
//            Rect corridor = corridorBetween(all, path_thickness, r1, r2);
//            if (corridor != null) cells.add(corridor);
//
//        }
//
//        //Add corridors to the pairs of sister-pair rooms
//        int length2 = cells.size();
//        for (int i = 0; i < length2; i += 4) {
//            //Rooms
//            Rect r1, r2;
//            float randomRoom = Game.random(0.0f, 100.0f);
//            if (randomRoom < 33.3f) {
//                r1= cells.get(i);
//                r2 = cells.get(i+2);
//
//            } else if (randomRoom < 66.6f) {
//                r1 = cells.get(i+1);
//                r2 = cells.get(i+3);
//
//            } else {
//                r1 = cells.get(i/4);
//                r2 = cells.get(i/4 + 1);
//            }
//
//            cells.add(corridorBetweenRects(path_thickness, r1, r2));
//
//        }

    }

}
