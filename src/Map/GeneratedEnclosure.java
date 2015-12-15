package Map;

import Helper.MyMath;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by wyatt on 8/27/2015.
 */

//TODO: create a "bedrock" border around the map where terrain cannot be destroyed and the player cannot pass

public class GeneratedEnclosure {

    // This class creates a randomly generated map, consisting of rooms with corridors connecting them.
    // I use the Binary Space Partitioning (look it up) to initially split the space into partitions.
    // Then, I randomly reduce the size of these partitions, and I add corridors that branch out to all the rooms
    // The room and corridor rectangles are stored separately, as well as the list of the surround walls
    // Then I add random obstacles to the edges of the rooms (without blocking the corridors)
    // Then I use my smoothing algorithm to smooth the edges and make it "cave-like" and more natural

    public int width, height, scale;

    //Stores all types of rooms
    public List<Rect> cells;
    //Stores rooms (not corridors)
    public List<Rect> rooms;
    //Stores only corridors
    public List<Rect> corridors;

    //Accessible area of map
    public Area region;
    public Area inverseRegion;

    //Stores line segments of map (these are used for shadowing, collision detection, etc.)
    public List<Line2D> walls;

    //Global variables
    private int iterations;
    private final float splitSizeFactor;
    private final int minRoomSize;

    private final Rectangle2D border;

    //TODO: scale everything while generating initial map, not after

    public GeneratedEnclosure(int i_width, int i_height, float scaleFactor) {

        //Make sure width and height are always even numbers
        width = MyMath.round(i_width, 2);
        height = MyMath.round(i_height, 2);

        //Modify iterations so that the rooms sizes change based on scaleFactor (default is 4 cause it looks the best)
        iterations = 4 + (int) (Math.log(scaleFactor) / Math.log(2));

        //Local constants
        float roomReductionFactor = 0.42f;
        int min_spacing = 4;
        int path_thickness = 6;
        int smoothingIterations = 3;
        float smoothingFactor = 0.5f;
        boolean randomObstaclesOnEdges = true;

        //Global constants
        splitSizeFactor = 0.22f;
        minRoomSize = 50;

        //The value in which the generated map is scaled by
        scale = 12;

        border = new Rectangle2D.Float(-width*scale,-height*scale,3*width*scale, 3*height*scale);
        //border = new Rectangle2D.Float(0,0,width*scale, height*scale);

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
                r.w = MyMath.round(MyMath.random(r.w * roomReductionFactor, r.w - min_spacing), 2);
                r.x = MyMath.round(MyMath.random(r.x + min_spacing / 2.0f, oldRight - r.w - min_spacing / 2.0f), 2);
                r.h = MyMath.round(MyMath.random(r.h * roomReductionFactor, r.h - min_spacing), 2);
                r.y = MyMath.round(MyMath.random(r.y + min_spacing / 2.0f, oldBottom - r.h - min_spacing / 2.0f), 2);
            }

            //Copy all rooms into rooms list
            rooms.addAll(cells);

            //Keep a copy of cells.size() because cells is modified within the loop (below)
            int roomsAmount = cells.size();

            //The code below creates two lists of rectangles (sisters), where there will be one corridor
            //(it also needs revising: the Cell class is poorly written, it should be tree data instead)
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

        //Create a single region from all of the cells
        region = new Area();

        //Add the rooms and corridors to the region
        for (Rect r : cells) {
            region.add(new Area(new Rectangle2D.Float(r.x, r.y, r.w, r.h)));
        }

        if (randomObstaclesOnEdges) {

            //Create list of regions where obstacles cannot be placed
            List<Rectangle2D> openSpaces = new ArrayList<>();
            for (Rect c : corridors) {
                if (c.h == path_thickness) { //horizontal corridors
                    openSpaces.add(new Rectangle2D.Float(c.x - path_thickness, c.y, path_thickness, path_thickness));
                    openSpaces.add(new Rectangle2D.Float(c.x + c.w, c.y, path_thickness, path_thickness));
                }
                if (c.w == path_thickness) { //vertical corridors
                    openSpaces.add(new Rectangle2D.Float(c.x, c.y - path_thickness, path_thickness, path_thickness));
                    openSpaces.add(new Rectangle2D.Float(c.x, c.y + c.h, path_thickness, path_thickness));
                }
            }

            //Add randomized "obstacles" to the rooms
            for (Rect r : rooms) {

                int amount = Math.round(MyMath.map(Math.min(3500, r.w * r.h),100,3500,1,5));

                for (int i = 0; i < amount; i++) {
                    Rectangle2D object = objectInRoom(r, openSpaces);
                    if (object != null) region.subtract(new Area(object));
                }
            }

        }

        //Create list of points that describe the region
        List<float[]> areaPoints = areaPoints(region);

        for (float[] p : areaPoints) {
            p[1] *= scale;
            p[2] *= scale;
        }

        //smooth edges algorithm
        for (int i = 0; i < smoothingIterations; i++) {
            areaPoints = smoothEdges(areaPoints, smoothingFactor);
        }

        //Redefine region based on new points
        Path2D newPoints = new Path2D.Float();
        newPoints.moveTo(areaPoints.get(0)[1], areaPoints.get(0)[2]);
        for (int i = 1; i < areaPoints.size(); i++) {
            float[] p = areaPoints.get(i);
            newPoints.lineTo(p[1], p[2]);
        }
        newPoints.closePath();
        region = new Area(newPoints);

        //Define an inverse of the region
        inverseRegion = new Area(border);
        inverseRegion.subtract(region);

        //Define the walls (used for shadowing) based on the new region
        defineWalls(areaPoints);

        width *= scale;
        height *= scale;

        for (Rect r : cells) {
            r.x *= scale;
            r.y *= scale;
            r.w *= scale;
            r.h *= scale;
        }

    }

    private void defineWalls(List<float[]> areaPoints) {

        float[] start = new float[3]; // To record where each polygon starts

        //Create an empty list of lines
        walls = new ArrayList<>();

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
                                currentElement[1], currentElement[2],
                                nextElement[1], nextElement[2]
                        )
                );
            } else if (nextElement[0] == PathIterator.SEG_CLOSE) {
                walls.add(
                        new Line2D.Float(
                                currentElement[1], currentElement[2],
                                start[1], start[2]
                        )
                );
            }
        }

//        float x = (float) border.getX(), y = (float) border.getY(), w = (float) border.getWidth(), h = (float) border.getHeight();
//        walls.add(new Line2D.Float(x,y,x+w,y));
//        walls.add(new Line2D.Float(x+w,y,x+w,y+h));
//        walls.add(new Line2D.Float(x+w,y+h,x,y+h));
//        walls.add(new Line2D.Float(x,y+h,x,y));

    }

    //A randomly placed rectangular obstacle inside a room
    private Rectangle2D objectInRoom(Rect r, List<Rectangle2D> openSpaces) {
        Rectangle2D object;
        for (int i = 0; i < 10; i++) {

            int w = (int) (MyMath.random(7, 15));
            int h = (int) (MyMath.random(7, 15));

            int x, y;
            float random = MyMath.random(1.0f, 2.0f);
            if (random > 1.75f) {
                x = r.x;
                y = r.y + r.h - h;
            } else if (random > 1.5f) {
                x = r.x;
                y = r.y;
            } else if (random > 1.25f) {
                x = r.x + r.w - w;
                y = r.y;
            } else// if (random > 1.0f) {
            {    x = r.x + r.w - w;
                y = r.y + r.h - h;
            }/* else if (random > 0.75f) {
                x = r.x;
                y = (int) Game.random(r.y + path_thickness, r.y + r.h - path_thickness - h);
            } else if (random > 0.5f) {
                x = r.x + r.w - w;
                y = (int) Game.random(r.y + path_thickness, r.y + r.h - path_thickness - h);
            } else if (random > 0.25f) {
                x = (int) Game.random(r.x + path_thickness, r.x + r.w - path_thickness - w);
                y = r.y;
            } else {
                x = (int) Game.random(r.x + path_thickness, r.x + r.w - path_thickness - w);
                y = r.y + r.h - h;
            }*/

            object = new Rectangle2D.Float(x, y, w, h);

            boolean passes = true;
            //test for adjacency to corridors
            for (Rectangle2D openSpace : openSpaces) {
                if (object.intersects(openSpace)) {
                    passes = false;
                    break;
                }
            }

            if (passes) {
                return object;
            }

        }

        return null;

    }

    //An algorithm for smoothing the edges of a region:
    //For each edge, the midpoint is found and the original vertices
    //are moved towards their associated midpoints
    private List<float[]> smoothEdges(List<float[]> input, float factor) {

        //Create list for the midpoints of all the lines
        List<Point2D> midpoints = new ArrayList<>();
        for (float[] edge : input) {
            float x = (edge[1] + edge[1])/2.0f;
            float y = (edge[2] + edge[2])/2.0f;
            midpoints.add(new Point2D.Float(x,y));
        }

        //Create a list of points for the original vertices to move towards
        List<Point2D> anchorPoints = new ArrayList<>();

        //Define this list as the midpoints between the midpoints we made earlier
        Point2D p0 = midpoints.get(midpoints.size()-1),
                p1 = midpoints.get(0);

        anchorPoints.add(new Point2D.Float(
                (float)((p1.getX()+p0.getX())/2.0f),
                (float)((p1.getY()+p0.getY())/2.0f)));

        for (int i = 0; i < midpoints.size()-1; i += 1) {

            Point2D pa = midpoints.get(i),
                    pb = midpoints.get(i+1);

            anchorPoints.add(new Point2D.Float(
                    (float)((pa.getX()+pb.getX())/2.0f),
                    (float)((pa.getY()+pb.getY())/2.0f)));

        }

        //Create output list of vertices
        List<float[]> output = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {

            float[] vertex = input.get(i);
            Point2D ap = anchorPoints.get(i);

            //Move original vertices halfway to the anchorPoints
            //float dist = (float) Math.sqrt( (vertex[1]-ap.getX())*(vertex[1]-ap.getX()) +(vertex[2]-ap.getY())*(vertex[2]-ap.getY()) );
            float dx = (float)(factor*(vertex[1]-ap.getX()));
            float dy = (float)(factor*(vertex[2]-ap.getY()));
            vertex[1] = (float) (ap.getX() + dx);
            vertex[2] = (float) (ap.getY() + dy);

            //Add the midpoints and new vertices to the output
            output.add(new float[]{vertex[0], (float) ap.getX(), (float) ap.getY()});
            output.add(vertex);

        }

        return output;
    }

    private List<float[]> areaPoints(Area area) {
        //Create list of points that describe the region
        List<float[]> areaPoints = new ArrayList<>();
        float[] coords = new float[6];

        for (PathIterator pi = area.getPathIterator(null); !pi.isDone(); pi.next()) {
            //"type" will either be SEG_LINETO, SEG_MOVETO, or SEG_CLOSE
            int type = pi.currentSegment(coords);

            float[] pathIteratorCoords = {type, coords[0], coords[1]};
            areaPoints.add(pathIteratorCoords);
        }

        return areaPoints;

    }

    public void addExplosion(float x, float y, float radius, int vertices) {

//        int[] x_points = {-2*radius, -radius, radius, 2*radius, 2*radius, radius, -radius, -2*radius};
//        int[] y_points = {radius, 2*radius, 2*radius, radius, -radius, -2*radius, -2*radius, -radius};

        //create a randomized polygon
        Polygon explosion = new Polygon();
        float deltaTheta = 6.283f / vertices;
        float maxRadius = radius*1.2f;
        float minRadius = radius*0.8f;
        for (int i = 0; i < vertices; i++) {
            float angle = i * deltaTheta;
            float length = MyMath.random(minRadius, maxRadius);
            int px = (int) (Math.cos(angle) * length + x);
            int py = (int) (Math.sin(angle) * length + y);
            explosion.addPoint(px, py);
        }
        Area subtraction = new Area(explosion);

        //Update the regions to accommodate the new explosion
        region.add(subtraction);
        inverseRegion.subtract(subtraction);

        defineWalls(areaPoints(region));

    }

    //Class used as a tree generator for new cells
    private class Cell {

        public Cell cellA, cellB;

        public Rect p;

        public Cell(Rect p, int iteration) {

            this.p = p;

            if (iteration + 1 <= iterations) {
                if ((p.w / 2.0f > minRoomSize && p.h / 2.0f < minRoomSize) || p.h < p.w) { //new horizontal box
                    int randomCenter = MyMath.round(MyMath.random(-p.w * splitSizeFactor, p.w * splitSizeFactor) + p.w / 2.0f, 2);
                    cellA = new Cell(new Rect(p.x, p.y, randomCenter, p.h), iteration + 1);
                    cellB = new Cell(new Rect(p.x + randomCenter, p.y, p.w - randomCenter, p.h), iteration + 1);
                } else { //new vertical box
                    int randomCenter = MyMath.round(MyMath.random(-p.h * splitSizeFactor, p.h * splitSizeFactor) + p.h / 2.0f, 2);
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

    public void draw(Graphics2D g, Color stroke, Color fill) {

//        Area inverseRegion = new Area(new Rectangle2D.Float(-width, -height, 3 * width, 3 * height));
//        inverseRegion.subtract(region);

        g.setStroke(new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g.setColor(stroke);
        g.draw(inverseRegion);

        g.setColor(fill);
        g.fill(inverseRegion);

    }

}
