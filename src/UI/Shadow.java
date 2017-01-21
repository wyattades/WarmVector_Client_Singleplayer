package UI;

import Main.Window;
import Util.MyMath;
import Util.Point;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Directory: WarmVector_Client_Singleplayer/${PACKAGE_NAME}/
 * Created by Wyatt on 5/7/2015.
 */
public class Shadow {

    // Color of shadow
    private static final Color shadowFill = new Color(20, 20, 20);

    // Draw region
    private static final double DRAW_DIST = Window.WIDTH * 1.5;
    private Rectangle2D drawRegion;

    // These represent the map and the light location:
    private List<Segment> segments;
    private List<EndPoint> endpoints;
    private Point center;
    private boolean updateSegments, updateOrigin;

    // 'open' line segments, sorted so that the nearest
    // segment is first. It's used only during the sweep algorithm
    private List<Segment> open;

    // The output is a series of points that forms a visible area polygon
    public List<Point> output;

    private Map map;

    // Construct an empty visibility set
    public Shadow(Map _map, double _x, double _y) {

        map = _map;

        output = new ArrayList<>();
        center = new Point(0, 0);

        drawRegion = new Rectangle2D.Double();

        updateSegments = updateOrigin = true;
        update(_x, _y);
    }

    public void update(double x, double y) {
        if (updateOrigin) {
            drawRegion.setRect(x - DRAW_DIST * 0.5, y - DRAW_DIST * 0.5, DRAW_DIST, DRAW_DIST);
        }

//        if (updateSegments) {
//        }

        if (updateSegments || updateOrigin) {
            updateSegments();
            setLightLocation(x, y);
            sweep(0.0, MyMath.TWO_PI);
        }

        updateSegments = updateOrigin = false;
    }

    private void updateSegments() {
        open = new ArrayList<>();
        segments = new ArrayList<>();
        endpoints = new ArrayList<>();

        for (Line2D w : map.walls) {
            if (drawRegion.contains(w.getP1()) || drawRegion.contains(w.getP2()))
                addSegment(w.getX1() , w.getY1(), w.getX2(), w.getY2());
        }
    }

    public void draw(Graphics2D g) {

        int[] x_points = new int[output.size()];
        int[] y_points = new int[x_points.length];

        for (int i = 0; i < output.size(); i++) {
            Util.Point p = output.get(i);
            x_points[i] = MyMath.round(p.x);
            y_points[i] = MyMath.round(p.y);
        }

        Polygon cutout = new Polygon(x_points, y_points, x_points.length);

        Area shadow = new Area(map.innerRegion);
        shadow.subtract(new Area(cutout));

        g.setColor(shadowFill);
        g.fill(shadow);
    }


    private void addSegment(double x1, double y1, double x2, double y2) {

        // Add a segment, where the first point shows up in the
        // visualization but the second one does not. (Every endpoint is
        // part of two segments, but only show them once)
        EndPoint p1 = new EndPoint(x1, y1);
        p1.visualize = true;
        EndPoint p2 = new EndPoint(x2, y2);
        p2.visualize = false;
        Segment segment = new Segment(p1, p2);
        p1.segment = p2.segment = segment;
        segments.add(segment);
        endpoints.add(p1);
        endpoints.add(p2);

    }


    // Set the light location
    private void setLightLocation(double x, double y) {
        center.x = x;
        center.y = y;

        for (Segment segment : segments) {
            double dx = 0.5 * (segment.p1.x + segment.p2.x) - x;
            double dy = 0.5 * (segment.p1.y + segment.p2.y) - y;
            segment.d = dx * dx + dy * dy;

            segment.p1.angle = Math.atan2(segment.p1.y - y, segment.p1.x - x);
            segment.p2.angle = Math.atan2(segment.p2.y - y, segment.p2.x - x);

            double dAngle = segment.p2.angle - segment.p1.angle;
            if (dAngle <= -Math.PI) {
                dAngle += 2.0 * Math.PI;
            }
            if (dAngle > Math.PI) {
                dAngle -= 2.0 * Math.PI;
            }
            segment.p1.begin = (dAngle > 0.0);
            segment.p2.begin = !segment.p1.begin;
        }
    }

    // returns true if point is "left" of segment treated as a vector
    private boolean leftOf(Segment s, Point p) {
        double cross = (s.p2.x - s.p1.x) * (p.y - s.p1.y) - (s.p2.y - s.p1.y) * (p.x - s.p1.x);
        return cross < 0;
    }

    // A neat algorithm that works for reasons outside of my knowledge (I didn't write this)
    private boolean segmentInFrontOf(Segment a, Segment b, Point relativeTo) {

        boolean A1 = leftOf(a, interpolate(b.p1, b.p2, 0.01));
        boolean A2 = leftOf(a, interpolate(b.p2, b.p1, 0.01));
        boolean A3 = leftOf(a, relativeTo);
        boolean B1 = leftOf(b, interpolate(a.p1, a.p2, 0.01));
        boolean B2 = leftOf(b, interpolate(a.p2, a.p1, 0.01));
        boolean B3 = leftOf(b, relativeTo);
        if (B1 == B2 && B2 != B3) return true;
        if (A1 == A2 && A2 == A3) return true;
        if (A1 == A2 && A2 != A3) return false;
        if (B1 == B2 && B2 == B3) return false;

        return false;
    }

    // Interpolate from p to q with scale f
    private Point interpolate(Point p, Point q, double f) {
        return new Point(p.x * (1.0 - f) + q.x * f, p.y * (1.0 - f) + q.y * f);
    }

    // Run the algorithm, sweeping over all or part of the circle to find
    // the visible area, represented as a set of triangles
    private void sweep(double beginAngle, double maxAngle) {

        output = new ArrayList<>();  // output set of triangles
        Collections.sort(endpoints, (a, b) -> {
            // Traverse in angle order
            if (a.angle > b.angle) return 1;
            if (a.angle < b.angle) return -1;
            if (!a.begin && b.begin) return 1;
            if (a.begin && !b.begin) return -1;
            return 0;
        });

        open.clear();

        // Iterate through all the segments, figure out which
        // ones intersect the initial sweep line, and then sort them
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
                    while (node != null && segmentInFrontOf(p.segment, node, center)) {
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
        double s = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x))
                / ((p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y));
        return new Util.Point(p1.x + s * (p2.x - p1.x), p1.y + s * (p2.y - p1.y));
    }


    private void addTriangle(double angle1, double angle2, Segment segment) {
        Point p1 = center;
        Point p2 = new Point(center.x + Math.cos(angle1), center.y + Math.sin(angle1));
        Point p3 = new Point(0.0, 0.0);
        Point p4 = new Point(0.0, 0.0);

        if (segment != null) {
            // Stop the triangle at the intersecting segment
            p3.x = segment.p1.x;
            p3.y = segment.p1.y;
            p4.x = segment.p2.x;
            p4.y = segment.p2.y;
        } else {
            // Stop the triangle at a fixed distance just in case
            p3.x = center.x + Math.cos(angle1) * 500.0;
            p3.y = center.y + Math.sin(angle1) * 500.0;
            p4.x = center.x + Math.cos(angle2) * 500.0;
            p4.y = center.y + Math.sin(angle2) * 500.0;
        }

        Point pBegin = lineIntersection(p3, p4, p1, p2);

        p2.x = center.x + Math.cos(angle2);
        p2.y = center.y + Math.sin(angle2);
        Point pEnd = lineIntersection(p3, p4, p1, p2);

        output.add(pBegin);
        output.add(pEnd);
    }

    public void queueWorldUpdate() {
        updateSegments = true;
    }

    public void queueOriginUpdate() {
        updateOrigin = true;
    }

    private class EndPoint extends Point {
        public boolean begin = false;
        public Segment segment = null;
        public double angle = 0;
        public boolean visualize = false;

        public EndPoint(double x, double y) {
            super(x, y);
        }
    }

    private class Segment {
        public EndPoint p1, p2;
        public double d;
        public boolean draw;

        public Segment(EndPoint p1, EndPoint p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

    }

}
