package com.ncl.sketch.agent.di;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

/**
 * Helper functions pertaining to geometric calculations on {@link Point 2D point}s.
 */
final class Geometry2D {

    private static final double ZERO = 0.000001;

    private Geometry2D() {
        // empty;
    }

    /**
     * Returns the area of the specified {@link Circle circle}.
     * 
     * @param circle the {@link Circle circle} for which to compute the area
     * @return the area of the specified {@link Circle circle}
     */
    static final double areaOf(final Circle circle) {
        final double radius = circle.radius();
        return Math.PI * radius * radius;
    }

    /**
     * Returns the area of the triangle defined by the three specified vertices.
     * 
     * @param a first vertex
     * @param b second vertex
     * @param c third vertex
     * @return the area of the triangle defined by the three specified vertices
     */
    static final double areaOf(final Point a, final Point b, final Point c) {
        return Math.abs(signedAreaOf(a, b, c));
    }

    /**
     * Returns the area of the quadrilateral defined by the four specified vertices.
     * 
     * @param a first vertex
     * @param b second vertex
     * @param c third vertex
     * @param d fourth vertex
     * @return the area of the quadrilateral defined by the four specified vertices
     */
    static final double areaOf(final Point a, final Point b, final Point c, final Point d) {
        final double area;
        final double[] intersection = intersection(a, d, c, b);
        if (intersection == null) {
            /*
             * Simple quadrilateral. Add the two areas. If quadrilateral is concave this will also
             * work.
             */
            final double a1 = signedAreaOf(a, b, c);
            final double a2 = signedAreaOf(c, d, a);
            area = Math.abs(a1 + a2);
        } else if (intersection.length == 0) {
            area = 0.0;
        } else {
            final Point i = point(intersection[0], intersection[1]);
            /*
             * area is the sum of the area of the two triangle intersecting at i.
             */
            area = areaOf(a, b, i) + areaOf(i, d, c);
        }
        return area;
    }

    /**
     * Returns a new {@link Circle circle} which takes the center of the stroke�s bounding box as
     * its own center and the mean distance between the center and each stroke point as its radius.
     * 
     * @param stroke the {@link Stroke stroke}
     * @return a new {@link Circle circle} which takes the center of the stroke�s bounding box as
     *         its own center and the mean distance between the center and each stroke point as its
     *         radius
     */
    static final Circle circle(final Stroke stroke) {
        double maxX = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;

        final int size = stroke.size();
        for (int i = 0; i < size; i++) {
            final Point point = stroke.get(i);
            maxX = Math.max(point.x(), maxX);
            minX = Math.min(point.x(), minX);
            maxY = Math.max(point.y(), maxY);
            minY = Math.min(point.y(), minY);
        }

        final double x = (maxX - minX) / 2 + minX;
        final double y = (maxY - minY) / 2 + minY;
        final Point centre = point(x, y);

        double radius = 0.0;
        for (int i = 0; i < size; i++) {
            radius += distance(centre, stroke.get(i));
        }
        radius = radius / size;

        return new Circle(centre, radius);
    }

    /**
     * Returns the distance between the two specified {@link Point point}s.
     * 
     * @param from first point
     * @param to second point
     * @return the distance between the two specified {@link Point point}s
     */
    static final double distance(final Point from, final Point to) {
        final double a = from.x() - to.x();
        final double b = from.y() - to.y();
        return Math.sqrt(a * a + b * b);
    }

    /**
     * Returns the length of the specified {@link Line line}.
     * 
     * @param line the line
     * @return the length of the specified {@link Line line}
     */
    static final double lengthOf(final Line line) {
        return distance(line.start(), line.end());
    }

    /**
     * Orthogonally projects the specified {@link Point point} on the specified {@link Line line}.
     * 
     * @param point the point to projected
     * @param line the line on which the point shall be orthogonally projected
     * @return a new {@link Point point} that is the result of the orthogonal projection of the
     *         specified point on the specified line
     */
    static final Point project(final Point point, final Line line) {
        final Point start = line.start();
        final double[] a = vector(start, point);
        final double[] b = vector(start, line.end());
        final double scale = dotProductOf(a, b) / dotProductOf(b, b);
        final double[] a1 = scale(scale, b);
        final double x = x(a1) + start.x();
        final double y = y(a1) + start.y();
        return point(x, y);
    }

    private static double[] add(final double[] v1, final double[] v2) {
        return new double[] { x(v1) + x(v2), y(v1) + y(v2) };
    }

    private static double dotProductOf(final double[] v1, final double[] v2) {
        return x(v1) * x(v2) + y(v1) * y(v2);
    }

    private static double exteriorProductOf(final double[] v1, final double[] v2) {
        return x(v1) * y(v2) - x(v2) * y(v1);
    }

    /*
     * returns [x,y] if such an intersection exists, [] if the two lines are collinear, null
     * otherwise.
     */
    private static double[] intersection(final Point a, final Point b, final Point c, final Point d) {
        final double[] p = vector(a);
        final double[] r = vector(a, b);
        final double[] q = vector(c);
        final double[] s = vector(c, d);
        final double[] qp = subtract(q, p);
        final double rs = exteriorProductOf(r, s);
        final double qpr = exteriorProductOf(qp, r);
        final double[] result;
        if (isZero(qpr) && isZero(rs)) {
            // collinear
            result = new double[] {};
        } else if (isZero(rs)) {
            // parrallel
            result = null;
        } else {
            final double t = exteriorProductOf(qp, s) / rs;
            final double u = qpr / rs;
            if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
                result = add(p, scale(t, r));
            } else {
                // intersection outside lines
                result = null;
            }
        }
        return result;
    }

    private static boolean isZero(final double val) {
        return Math.abs(val) < ZERO;
    }

    private static Point point(final double x, final double y) {
        return new Point() {

            @Override
            public final String toString() {
                return "[" + x + ", " + y + "]";
            }

            @Override
            public final double x() {
                return x;
            }

            @Override
            public final double y() {
                return y;
            }
        };
    }

    private static double[] scale(final double scale, final double[] v) {
        return new double[] { scale * x(v), scale * y(v) };
    }

    private static double signedAreaOf(final Point a, final Point b, final Point c) {
        final double[] v1 = vector(a, b);
        final double[] v2 = vector(a, c);
        return 0.5 * exteriorProductOf(v1, v2);
    }

    private static double[] subtract(final double[] v1, final double[] v2) {
        return new double[] { x(v1) - x(v2), y(v1) - y(v2) };
    }

    // vector from origin (0,0) to point.
    private static double[] vector(final Point pt) {
        return new double[] { pt.x(), pt.y() };
    }

    // vector from "from" to "to".
    private static double[] vector(final Point from, final Point to) {
        return new double[] { to.x() - from.x(), to.y() - from.y() };
    }

    private static double x(final double[] v) {
        return v[0];
    }

    private static double y(final double[] v) {
        return v[1];
    }

}
