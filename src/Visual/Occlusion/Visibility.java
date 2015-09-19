package Visual.Occlusion;

import Map.GeneratedEnclosure;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 5/7/2015.
 */
public class Visibility {

    //Color of shadow
    private static Color COLOR = new Color(20, 20, 20, 255);

    // These represent the map and the light location:
    private ArrayList<Segment> segments;
    private ArrayList<EndPoint> endpoints;
    private Point center;

    // These are currently 'open' line segments, sorted so that the nearest
    // segment is first. It's used only during the sweep algorithm, and exposed
    // as a public field here so that the demo can display it.
    private ArrayList<Segment> open;

    // The output is a series of points that forms a visible area polygon
    public ArrayList<Point> output;

    //private TileMap tileMap;
    private Rectangle2D.Float BORDER;

    // Construct an empty visibility set
    public Visibility(GeneratedEnclosure map) {

        BORDER = new Rectangle2D.Float(
                -map.width,
                -map.height,
                3 * map.width,
                3 * map.height
        );

        segments = new ArrayList<Segment>();
        endpoints = new ArrayList<EndPoint>();
        open = new ArrayList<Segment>();
        center = new Point(0, 0);
        output = new ArrayList<Point>();
        for (Line2D w : map.walls) {
            addSegment((float) w.getX1() , (float) w.getY1(), (float) w.getX2(), (float) w.getY2());
        }
    }

    public void draw(Graphics2D g) {
        Polygon CUTOUT = new Polygon();
        for (Point p : output) {
            CUTOUT.addPoint(Math.round(p.x), Math.round(p.y));
        }
        GeneralPath SHADOW = new GeneralPath(CUTOUT);
        SHADOW.append(BORDER, false);

        g.setColor(COLOR);
        g.fill(SHADOW);
    }


    private void addSegment(float x1, float y1, float x2, float y2) {
        // Add a segment only if there is not an existing segment in that space
//        boolean alreadyExists = false;
//        for (Segment s : segments) {
//            if (((int) x1 == (int) s.p1.x && (int) y1 == (int) s.p1.y && (int) x2 == (int) s.p2.x && (int) y2 == (int) s.p2.y) ||
//                    ((int) x1 == (int) s.p2.x && (int) y1 == (int) s.p2.y && (int) x2 == (int) s.p1.x && (int) y2 == (int) s.p1.y)) {
//                alreadyExists = true;
//            }
//        }
//        if (!alreadyExists) {
        // Add a segment, where the first point shows up in the
        // visualization but the second one does not. (Every endpoint is
        // part of two segments, but we want to only show them once.)
        EndPoint p1 = new EndPoint(x1, y1);
        p1.visualize = true;
        EndPoint p2 = new EndPoint(x2, y2);
        p2.visualize = false;
        Segment segment = new Segment(p1, p2);
        p1.segment = p2.segment = segment;
        segments.add(segment);
        endpoints.add(p1);
        endpoints.add(p2);
//        }

    }


    // Set the light location
    public void setLightLocation(float x, float y) {
        center.x = x;
        center.y = y;

        for (Segment segment : segments) {
            float dx = 0.5f * (segment.p1.x + segment.p2.x) - x;
            float dy = 0.5f * (segment.p1.y + segment.p2.y) - y;
            segment.d = dx * dx + dy * dy;

            segment.p1.angle = (float) Math.atan2(segment.p1.y - y, segment.p1.x - x);
            segment.p2.angle = (float) Math.atan2(segment.p2.y - y, segment.p2.x - x);

            float dAngle = segment.p2.angle - segment.p1.angle;
            if (dAngle <= -Math.PI) {
                dAngle += 2 * Math.PI;
            }
            if (dAngle > Math.PI) {
                dAngle -= 2 * Math.PI;
            }
            segment.p1.begin = (dAngle > 0.0);
            segment.p2.begin = !segment.p1.begin;
        }
    }

    // According to an outside source: leftOf(segment, point) returns true if point is "left"
    // of segment treated as a vector. Note that this assumes a 2D
    // coordinate system in which the Y axis grows downwards, which
    // matches common 2D graphics libraries, but is the opposite of
    // the usual convention from mathematics and in 3D graphics
    // libraries.
    private boolean leftOf(Segment s, Point p) {
        // <http://en.wikipedia.org/wiki/Geometric_algebra>
        float cross = (s.p2.x - s.p1.x) * (p.y - s.p1.y)
                - (s.p2.y - s.p1.y) * (p.x - s.p1.x);
        return cross < 0;
    }

    // Return p*(1-f) + q*f
    private Point interpolate(Point p, Point q, float f) {
        return new Point(p.x * (1 - f) + q.x * f, p.y * (1 - f) + q.y * f);
    }


    private boolean _segment_in_front_of(Segment a, Segment b, Point relativeTo) {
        // A neat algorithm that works for reasons outside of my knowledge
        boolean A1 = leftOf(a, interpolate(b.p1, b.p2, 0.01f));
        boolean A2 = leftOf(a, interpolate(b.p2, b.p1, 0.01f));
        boolean A3 = leftOf(a, relativeTo);
        boolean B1 = leftOf(b, interpolate(a.p1, a.p2, 0.01f));
        boolean B2 = leftOf(b, interpolate(a.p2, a.p1, 0.01f));
        boolean B3 = leftOf(b, relativeTo);
        if (B1 == B2 && B2 != B3) return true;
        if (A1 == A2 && A2 == A3) return true;
        if (A1 == A2 && A2 != A3) return false;
        if (B1 == B2 && B2 == B3) return false;

        ArrayList<Point> points = new ArrayList<Point>();
        points.add(a.p1);
        points.add(a.p2);
        points.add(b.p1);
        points.add(b.p2);
        return false;
    }


    // Run the algorithm, sweeping over all or part of the circle to find
    // the visible area, represented as a set of triangles
    public void sweep(float maxAngle) {
        output = new ArrayList<Point>();  // output set of triangles
        Collections.sort(endpoints, new Comparator<EndPoint>() {
            // comparison function for sorting points by angle
            @Override
            public int compare(EndPoint a, EndPoint b) {
                // Traverse in angle order
                if (a.angle > b.angle) return 1;
                if (a.angle < b.angle) return -1;
                if (!a.begin && b.begin) return 1;
                if (a.begin && !b.begin) return -1;
                return 0;
            }
        });

        open.clear();
        float beginAngle = 0;

        // At the beginning of the sweep we want to know which
        // segments are active. The simplest way to do this is to make
        // a pass collecting the segments, and make another pass to
        // both collect and process them. However it would be more
        // efficient to go through all the segments, figure out which
        // ones intersect the initial sweep line, and then sort them.
        for (int i = 0; i < 2; i++) {
            for (EndPoint p : endpoints) {
                if (i == 1 && p.angle > maxAngle) {
                    // Early exit for the visualization to show the sweep process
                    break;
                }

                Segment current_old = open.isEmpty() ? null : open.get(0);

                if (p.begin) {
                    // Insert into the right place in the list
                    Segment node = open.isEmpty() ? null : open.get(0);
                    while (node != null && _segment_in_front_of(p.segment, node, center)) {
                        node = open.size() == open.indexOf(node) + 1 ? null : open.get((open.indexOf(node) + 1));
                    }
                    if (node == null) {
                        open.add(p.segment);
                    } else {
                        open.add(open.indexOf(node), p.segment); // (index-1) or just (index)???
                    }
                } else {
                    open.remove(p.segment);
                }

                Segment current_new = open.isEmpty() ? null : open.get(0);
                if (current_old != current_new) {
                    if (i == 1) {
                        addTriangle(beginAngle, p.angle, current_old);
                    }
                    beginAngle = p.angle;
                }
            }
        }
    }


    private Point lineIntersection(Point p1, Point p2, Point p3, Point p4) {
        float s = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x))
                / ((p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y));
        return new Point(p1.x + s * (p2.x - p1.x), p1.y + s * (p2.y - p1.y));
    }


    private void addTriangle(float angle1, float angle2, Segment segment) {
        Point p1 = center;
        Point p2 = new Point(center.x + (float) Math.cos(angle1), center.y + (float) Math.sin(angle1));
        Point p3 = new Point(0, 0);
        Point p4 = new Point(0, 0);

        if (segment != null) {
            // Stop the triangle at the intersecting segment
            p3.x = segment.p1.x;
            p3.y = segment.p1.y;
            p4.x = segment.p2.x;
            p4.y = segment.p2.y;
        } else {
            // Stop the triangle at a fixed distance; this probably is
            // not what we want, but it never gets used in the demo
            p3.x = center.x + (float) Math.cos(angle1) * 500;
            p3.y = center.y + (float) Math.sin(angle1) * 500;
            p4.x = center.x + (float) Math.cos(angle2) * 500;
            p4.y = center.y + (float) Math.sin(angle2) * 500;
        }

        Point pBegin = lineIntersection(p3, p4, p1, p2);

        p2.x = center.x + (float) Math.cos(angle2);
        p2.y = center.y + (float) Math.sin(angle2);
        Point pEnd = lineIntersection(p3, p4, p1, p2);

        output.add(pBegin);
        output.add(pEnd);
    }

}
