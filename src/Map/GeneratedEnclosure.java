package Map;

import Main.Game;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/27/2015.
 */
public class GeneratedEnclosure {

    // This class creates a randomly generated map, consisting of rooms with corridors connecting them.
    // I use the Binary Space Partitioning (look it up) to initially split the space into partitions.
    // Then, I randomly reduce the size of these partitions, and I add corridors that branch out to all the rooms
    // The room and corridor rectangles are stored separately, as well as the list of the surround walls

    public int width, height, scale;

    //Stores all types of rooms
    public List<Rect> cells;
    //Stores rooms (not corridors)
    public List<Rect> rooms;
    //Stores only corridors
    public List<Rect> corridors;

    //Accessible area of map
    public Area region;

    //Stores line segments of map (these are used for shadowing, collision detection, etc.)
    public List<Line2D> walls;

    //Global variables
    private int iterations;
    private final float splitSizeFactor;
    private final int minRoomSize;

    public GeneratedEnclosure(int i_width, int i_height, float scaleFactor) {

        width = i_width;
        height = i_height;

        scale = 12;

        //Local constants
        float roomReductionFactor = 0.42f;
        int min_spacing = 4;
        int path_thickness = 6;

        //Global constants
        splitSizeFactor = 0.22f;
        minRoomSize = 50;

        //Modify iterations so that the rooms sizes change based on scaleFactor (default is 4 cause it looks the best)
        iterations = 4 + (int) (Math.log(scaleFactor) / Math.log(2));

        //Make sure width and height are always even numbers
        width = round(width, 2);
        height = round(height, 2);

        //Keep looping until there are no null rooms (a null room
        //has about a 1 in 50 chance of spawning)
        boolean creationFailed = true;
        while (creationFailed) {

            creationFailed = false;

            //create empty lists of rectangles
            cells = new ArrayList<>();
            rooms = new ArrayList<>();
            corridors = new ArrayList<>();

            //add randomized subsection rects to the array
            new Cell(new Rect(0, 0, width, height), 0);

            //Randomly shrink the rectangles,
            //this produces the actual room dimensions
            for (Rect r : cells) {
                int oldRight = r.x + r.w,
                        oldBottom = r.y + r.h;
                r.w = round(Game.random(r.w * roomReductionFactor, r.w - min_spacing), 2);
                r.x = round(Game.random(r.x + min_spacing / 2.0f, oldRight - r.w - min_spacing / 2.0f), 2);
                r.h = round(Game.random(r.h * roomReductionFactor, r.h - min_spacing), 2);
                r.y = round(Game.random(r.y + min_spacing / 2.0f, oldBottom - r.h - min_spacing / 2.0f), 2);
            }

            //Copy all rooms int rooms list
            rooms.addAll(cells);

            //Keep a copy of cells.size() because cells is modified within the loop (below)
            int roomsAmount = cells.size();

            //The code below creates two lists of rectangles (sisters), where there will be one corridor
            List<Rect> sister1, sister2;
            for (int i = 0; i < iterations; i++) {
                int iterateAmount = (int) Math.pow(2, i);
                for (int j = 0; j < roomsAmount; j += 2 * iterateAmount) {

                    sister1 = new ArrayList<>();
                    sister2 = new ArrayList<>();

                    for (int k = 0; k < iterateAmount; k++) {
                        sister1.add(cells.get(j + k));
                        sister2.add(cells.get(j + k + iterateAmount));
                    }

                    //Creates one corridor in between a random rectangle from both sister lists
                    Rect corridor = corridorBetween(cells, min_spacing, path_thickness, sister1, sister2);

                    //If the corridor was created successfully, add it to the list of cells
                    if (corridor != null) {
                        corridors.add(corridor);
                        cells.add(corridor);
                    } else {//If not, restart the map generation
                        creationFailed = true;
                    }

                }
            }
        }

        //Create an empty list of lines
        walls = new ArrayList<>();

        //Create a single region from all of the cells
        region = new Area();
        for (Rect r : cells) {
            region.add(new Area(new Rectangle2D.Float(r.x, r.y, r.w, r.h)));
        }

        //Add randomized "obstacles" to the rooms
        for (Rect r : rooms) {

            float area = r.w*r.h, a1 = minRoomSize*minRoomSize, a2 = 1500, b1 = 2, b2 = 4;

            int amount = round(b1 + (area-a1)*(b2-b1)/(a2-a1), 1);

            System.out.println(amount);
            for (int i = 0; i < amount; i++) {
                region.subtract(new Area(objectInRoom(r)));
            }
        }

        //Create list of points that describe the region
        List<float[]> areaPoints = new ArrayList<>();
        float[] coords = new float[6];

        for (PathIterator pi = region.getPathIterator(null); !pi.isDone(); pi.next()) {
            //"type" will either be SEG_LINETO, SEG_MOVETO, or SEG_CLOSE
            int type = pi.currentSegment(coords);

            float[] pathIteratorCoords = {type, coords[0], coords[1]};
            areaPoints.add(pathIteratorCoords);
        }

        float[] start = new float[3]; // To record where each polygon starts

        //Based on the region, add to the list of walls
        for (int i = 0; i < areaPoints.size(); i++) {
            //if not on the last point, return a line from this point to the next
            float[] currentElement = areaPoints.get(i);

            //default value in case it reached the end of the ArrayList
            float[] nextElement = {-1, -1, -1};
            if (i < areaPoints.size() - 1) {
                nextElement = areaPoints.get(i + 1);
            }

            //make the lines
            if (currentElement[0] == PathIterator.SEG_MOVETO) {
                start = currentElement; // Record where the polygon started to close it later
            }

            if (nextElement[0] == PathIterator.SEG_LINETO) {
                walls.add(
                        new Line2D.Float(
                                currentElement[1] * scale, currentElement[2]* scale,
                                nextElement[1]* scale, nextElement[2]* scale
                        )
                );
            } else if (nextElement[0] == PathIterator.SEG_CLOSE) {
                walls.add(
                        new Line2D.Float(
                                currentElement[1]* scale, currentElement[2]* scale,
                                start[1]* scale, start[2]* scale
                        )
                );
            }
        }

        width *= scale;
        height *= scale;

        for (Rect r : cells) {
            r.x *= scale;
            r.y *= scale;
            r.w *= scale;
            r.h *= scale;
        }

    }

    private class Cell {

        public Cell cellA, cellB;

        public Rect p;

        public Cell(Rect p, int iteration) {

            this.p = p;

            if (iteration + 1 <= iterations) {
                if ((p.w / 2.0f > minRoomSize && p.h / 2.0f < minRoomSize) || p.h < p.w) { //new horizontal box
                    int randomCenter = round(Game.random(-p.w * splitSizeFactor, p.w * splitSizeFactor) + p.w / 2.0f, 2);
                    cellA = new Cell(new Rect(p.x, p.y, randomCenter, p.h), iteration + 1);
                    cellB = new Cell(new Rect(p.x + randomCenter, p.y, p.w - randomCenter, p.h), iteration + 1);
                } else { //new vertical box
                    int randomCenter = round(Game.random(-p.h * splitSizeFactor, p.h * splitSizeFactor) + p.h / 2.0f, 2);
                    cellA = new Cell(new Rect(p.x, p.y, p.w, randomCenter), iteration + 1);
                    cellB = new Cell(new Rect(p.x, p.y + randomCenter, p.w, p.h - randomCenter), iteration + 1);
                }
                if (iteration + 2 == iterations) {
                    cells.add(cellA.cellA.p);
                    cells.add(cellA.cellB.p);
                    cells.add(cellB.cellA.p);
                    cells.add(cellB.cellB.p);
                }
            }

        }
    }

    //A randomly placed rectangular obstacle inside a room
    private Rectangle2D objectInRoom(Rect r) {

        int w = (int)Game.random(4,12);
        int h = (int)Game.random(4,12);
        int x,y;

        float random = Game.random(0,1);
        if (random > 0.75f) {
            x = r.x;
            y = (int) Game.random(r.y, r.y + r.h);
        } else if (random > 0.5f) {
            x = r.x - r.w;
            y = (int) Game.random(r.y, r.y + r.h);
        } else if (random > 0.25f) {
            x = (int) Game.random(r.x, r.x + r.w);
            y = r.y;
        } else {
            x = (int) Game.random(r.x, r.x + r.w);
            y = r.y - r.h;
        }


        return new Rectangle2D.Float(x, y, w, h);
    }

    //A randomly placed corridor between two sister rooms
    // (Can't collide with other corridors/rooms)
    private Rect corridorBetween(List<Rect> others, int leeWay, int path_thickness,
                                 List<Rect> sister1, List<Rect> sister2) {
        int amount = sister1.size();

        List<Rect> shuffled1 = new ArrayList<>(sister1);
        List<Rect> shuffled2 = new ArrayList<>(sister2);
        Collections.shuffle(shuffled1);
        Collections.shuffle(shuffled2);

        Rect corridor;

        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {

                Rect r1 = shuffled1.get(i);
                Rect r2 = shuffled2.get(j);

                if (Math.abs((r1.y + r1.h / 2.0f) - (r2.y + r2.h / 2.0f)) <=
                        r1.h / 2.0f + r2.h / 2.0f - path_thickness) {
                    int min = Math.round(Math.max(r1.y, r2.y));
                    int max = Math.round(Math.min(r1.y + r1.h, r2.y + r2.h) - path_thickness);
                    List<Integer> options = new ArrayList<>();
                    for (int k = 0; k < max - min; k++) {
                        options.add(min + k);
                    }
                    Collections.shuffle(options);
                    for (Integer option : options) {
                        corridor = new Rect(r1.x + r1.w, option, r2.x - (r1.x + r1.w), path_thickness);
                        boolean collides = false;
                        for (Rect o : others) {
                            if (o.x + o.w > corridor.x && o.x < corridor.x + corridor.w &&
                                    o.y + o.h > corridor.y - leeWay && o.y < corridor.y + corridor.h + leeWay) {
                                collides = true;
                            }
                        }
                        if (!collides) {
                            return corridor;
                        }
                    }
                } else if (Math.abs((r1.x + r1.w / 2.0f) - (r2.x + r2.w / 2.0f)) <=
                        r1.w / 2.0f + r2.w / 2.0f - path_thickness) {
                    float min = Math.max(r1.x, r2.x);
                    float max = Math.min(r1.x + r1.w, r2.x + r2.w) - path_thickness;
                    List<Integer> options = new ArrayList<>();
                    for (int k = 0; k < max - min; k++) {
                        options.add(Math.round(min + k));
                    }
                    Collections.shuffle(options);
                    for (Integer option : options) {
                        corridor = new Rect(option, r1.y + r1.h, path_thickness, r2.y - (r1.y + r1.h));
                        boolean collides = false;
                        for (Rect o : others) {
                            if (o.x + o.w > corridor.x - leeWay && o.x < corridor.x + corridor.w + leeWay &&
                                    o.y + o.h > corridor.y && o.y < corridor.y + corridor.h) {
                                collides = true;
                            }
                        }
                        if (!collides) {
                            return corridor;
                        }
                    }
                }
            }
        }
        return null;
    }

    //Rounds to nearest multiple of v from input i
    int round(float i, int v) {
        return Math.round(i / v) * v;
    }


    public void draw(Graphics2D g, Color color) {
        Polygon CUTOUT = new Polygon();
        for (Line2D w : walls) {
            CUTOUT.addPoint((int)w.getX1(), (int)w.getY1());
        }
        GeneralPath SHADOW = new GeneralPath(CUTOUT);
        SHADOW.append( new Rectangle2D.Float(-width, -height, 3 * width, 3 * height), false);

        g.setColor(color);
        g.fill(SHADOW);
    }

}
