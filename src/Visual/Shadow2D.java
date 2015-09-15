package Visual;

import Entities.Entity;
import Main.Game;
import Map.TileMap;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 3/12/2015.
 */
public class Shadow2D {

/*
    final float SHADOW_EXTRUDE = 2 * Game.WIDTH;

    private final static Color COLOR = new Color(0, 0, 0);

    private final static Polygon POLYGON = new Polygon();

    private int x, y, tileSize;
    //private float orient;
    private int[][] tileArray;
    Entity origin;

    public Shadow2D(int[][] tileArray, Entity origin) {
        x = y = 0;
        //orient = 0;
        tileSize = map.scale;
        this.tileArray = tileArray;
        this.origin = origin;
    }

    public void update() {
        x = origin.x;
        y = origin.y;
        //orient = origin.orient;
    }

    public void draw(Graphics2D g) {
        final Point2D.Float center = new Point2D.Float(x, y);

        for (int i = 0; i < tileArray.length; i++) {
            for (int j = 0; j < tileArray[1].length; j++) {
                if (tileArray[i][j] == TileMap.SOLID) {
                    Rectangle2D.Float bounds = new Rectangle2D.Float(i * tileSize, j * tileSize, tileSize + 2, tileSize + 2);

                    //radius of Entities's bounding circle
                    float r = (float) bounds.getWidth() / 2;

                    //get center of entity
                    float cx = (float) bounds.getX() + r;
                    float cy = (float) bounds.getY() + r;

                    //get direction from mouse to entity center
                    float dx = cx - center.x;
                    float dy = cy - center.y;

                    //get euclidean distance from mouse to center
                    float distSq = dx * dx + dy * dy; //avoid sqrt for performance

                    //normalize the direction to a unit vector
                    float len = (float) Math.sqrt(distSq);
                    float nx = dx;
                    float ny = dy;
                    if (len != 0) { //avoid division by 0
                        nx /= len;
                        ny /= len;
                    }

                    //get perpendicular of unit vector
                    float px = -ny;
                    float py = nx;

                    //our perpendicular points in either direction from radius
                    Point2D.Float A = new Point2D.Float(cx - px * r, cy - py * r);
                    Point2D.Float B = new Point2D.Float(cx + px * r, cy + py * r);

                    //project the points by our SHADOW_EXTRUDE amount
                    Point2D.Float C = project(center, A, SHADOW_EXTRUDE);
                    Point2D.Float D = project(center, B, SHADOW_EXTRUDE);

                    //construct a polygon from our points
                    POLYGON.reset();
                    POLYGON.addPoint((int) A.x, (int) A.y);
                    POLYGON.addPoint((int) B.x, (int) B.y);
                    POLYGON.addPoint((int) D.x, (int) D.y);
                    POLYGON.addPoint((int) C.x, (int) C.y);

                    //fill the polygon with the gradient paint
                    g.setColor(COLOR);
                    g.fill(POLYGON);
                }
            }
        }
    }


     // Projects a point from end along the vector (end - start) by the given scalar amount.

    private Point2D.Float project(Point2D.Float start, Point2D.Float end, float scalar) {
        float dx = end.x - start.x;
        float dy = end.y - start.y;
        //euclidean length
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        //normalize to unit vector
        if (len != 0) {
            dx /= len;
            dy /= len;
        }
        //multiply by scalar amount
        dx *= scalar;
        dy *= scalar;
        return new Point2D.Float(end.x + dx, end.y + dy);
    }
    */
}
