package Visual.Occlusion;

import Map.GeneratedEnclosure;

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
    private Color shadowFill;

    // These represent the map and the light location:
    private List<Segment> segments;
    private List<EndPoint> endpoints;
    private Util.Point center;

    // These are currently 'open' line segments, sorted so that the nearest
    // segment is first. It's used only during the sweep algorithm, and exposed
    // as a public field here so that the demo can display it.
    private List<Segment> open;

    // The output is a series of points that forms a visible area polygon
    public List<Util.Point> output;

    private final Area border;

    private GeneratedEnclosure map;

    //An object used to check if map.walls changes
    private Line2D changedWall;

    // Construct an empty visibility set
    public Shadow(GeneratedEnclosure map, Color shadowFill) {

        this.map = map;

        changedWall = map.walls.get(map.walls.size()-1);

        this.shadowFill = shadowFill;

        //TODO: might have to change this when we add bedrock to the generatedEnclosure
        border = new Area(new Rectangle2D.Float(
                -map.width,
                -map.height,
                3 * map.width,
                3 * map.height
        ));

        output = new ArrayList<>();
        center = new Util.Point(0, 0);
        defineSegments(map.walls);

    }

    public void defineSegments(List<Line2D> walls) {
        open = new ArrayList<>();
        segments = new ArrayList<>();
        endpoints = new ArrayList<>();
        for (Line2D w : walls) {
            addSegment((float) w.getX1() , (float) w.getY1(), (float) w.getX2(), (float) w.getY2());
        }
    }

    public void draw(Graphics2D g) {

        //TODO: find a better way to check if map changes
        if (!map.walls.get(map.walls.size()-1).equals(changedWall)) {
            defineSegments(map.walls);
            changedWall = map.walls.get(map.walls.size()-1);
        }

        int[] x_points = new int[output.size()];
        int[] y_points = new int[x_points.length];

        for (int i = 0; i < output.size(); i++) {
            Util.Point p = output.get(i);
            x_points[i] = Math.round(p.x);
            y_points[i] = Math.round(p.y);
        }

        Polygon cutout = new Polygon(x_points, y_points, x_points.length);

        Area shadow = new Area(border);
        shadow.subtract(new Area(cutout));

        g.setColor(shadowFill);
        g.fill(shadow);
    }


    private void addSegment(float x1, float y1, float x2, float y2) {

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

    // returns true if point is "left" of segment treated as a vector
    private boolean leftOf(Segment s, Util.Point p) {
        float cross = (s.p2.x - s.p1.x) * (p.y - s.p1.y) - (s.p2.y - s.p1.y) * (p.x - s.p1.x);
        return cross < 0;
    }

    // A neat algorithm that works for reasons outside of my knowledge
    private boolean _segment_in_front_of(Segment a, Segment b, Util.Point relativeTo) {

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

        return false;
    }

    //Part of above algorithm ^
    private Util.Point interpolate(Util.Point p, Util.Point q, float f) {
        return new Util.Point(p.x * (1 - f) + q.x * f, p.y * (1 - f) + q.y * f);
    }


    // Run the algorithm, sweeping over all or part of the circle to find
    // the visible area, represented as a set of triangles
    public void sweep(float maxAngle) {
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


    private Util.Point lineIntersection(Util.Point p1, Util.Point p2, Util.Point p3, Util.Point p4) {
        float s = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x))
                / ((p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y));
        return new Util.Point(p1.x + s * (p2.x - p1.x), p1.y + s * (p2.y - p1.y));
    }


    private void addTriangle(float angle1, float angle2, Segment segment) {
        Util.Point p1 = center;
        Util.Point p2 = new Util.Point(center.x + (float) Math.cos(angle1), center.y + (float) Math.sin(angle1));
        Util.Point p3 = new Util.Point(0, 0);
        Util.Point p4 = new Util.Point(0, 0);

        if (segment != null) {
            // Stop the triangle at the intersecting segment
            p3.x = segment.p1.x;
            p3.y = segment.p1.y;
            p4.x = segment.p2.x;
            p4.y = segment.p2.y;
        } else {
            // Stop the triangle at a fixed distance just in case
            p3.x = center.x + (float) Math.cos(angle1) * 500;
            p3.y = center.y + (float) Math.sin(angle1) * 500;
            p4.x = center.x + (float) Math.cos(angle2) * 500;
            p4.y = center.y + (float) Math.sin(angle2) * 500;
        }

        Util.Point pBegin = lineIntersection(p3, p4, p1, p2);

        p2.x = center.x + (float) Math.cos(angle2);
        p2.y = center.y + (float) Math.sin(angle2);
        Util.Point pEnd = lineIntersection(p3, p4, p1, p2);

        output.add(pBegin);
        output.add(pEnd);
    }

}
