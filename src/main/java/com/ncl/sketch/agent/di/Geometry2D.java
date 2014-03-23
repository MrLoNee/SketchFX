package com.ncl.sketch.agent.di;

import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;

/**
 * Helper functions pertaining to geometric calculations on {@link Point 2D
 * point}s.
 */
final class Geometry2D {

    private static final double ZERO = 0.000001;

    private Geometry2D() {
        // empty;
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
     * Returns the area of the quadrilateral defined by the four specified
     * vertices.
     * 
     * @param a first vertex
     * @param b second vertex
     * @param c third vertex
     * @param d fourth vertex
     * @return the area of the quadrilateral defined by the four specified
     *         vertices
     */
    static final double areaOf(final Point a, final Point b, final Point c, final Point d) {
        /*
         * account for self-intersecting quadrilaterals: compute signed area of
         * triangles (a, b, c) and (c, d, a).
         */
        final double a1 = signedAreaOf(a, b, c);
        final double a2 = signedAreaOf(c, d, a);
        final double area;
        if (a1 * a2 >= 0) {
            /*
             * same sign, simple quadrilateral. Add the two areas. If
             * quadrilateral is concave this will also work.
             */
            area = Math.abs(a1 + a2);
        } else {
            /*
             * self intersecting quadrilateral. Compute intersection.
             */
            final Point i = intersection(a, d, c, b);
            if (i == null) {
                /*
                 * no intersection, area is 0
                 */
                area = 0;
            } else {
                /*
                 * area is the sum of the area of the two triangle intersecting
                 * at i.
                 */
                area = areaOf(a, b, i) + areaOf(i, d, c);
            }
        }
        return area;
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
    static final double length(final Line line) {
        return distance(line.start(), line.end());
    }

    /**
     * Orthogonaly projects the specified {@link Point point} on the specified
     * {@link Line line}.
     * 
     * @param point the point to projected
     * @param line the line on which the point shall be orthogonaly projected
     * @return a new {@link Point point} that is the result of the orthogonal
     *         projection of the specified point on the specified line
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

    static final double signedAreaOf(final Point a, final Point b, final Point c) {
        final double[] v1 = vector(a, b);
        final double[] v2 = vector(a, c);
        return 0.5 * exteriorProductOf(v1, v2);
    }

    private static double dotProductOf(final double[] v1, final double[] v2) {
        return x(v1) * x(v2) + y(v1) * y(v2);
    }

    private static double exteriorProductOf(final double[] v1, final double[] v2) {
        return x(v1) * y(v2) - x(v2) * y(v1);
    }

    private static Point intersection(final Point a, final Point b, final Point c, final Point d) {
        final double x1 = a.x();
        final double y1 = a.y();
        final double x2 = b.x();
        final double y2 = b.y();
        final double x3 = c.x();
        final double y3 = c.y();
        final double x4 = d.x();
        final double y4 = d.y();

        final double divisor = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        final Point intersection;
        if (Math.abs(divisor) < ZERO) {
            intersection = null;
        } else {
            final double ab = x1 * y2 - y1 * x2;
            final double cd = x3 * y4 - y3 * x4;
            final double xi = (ab * (x3 - x4) - (x1 - x2) * cd) / divisor;
            final double yi = (ab * (y3 - y4) - (y1 - y2) * cd) / divisor;
            intersection = point(xi, yi);
        }
        return intersection;
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
