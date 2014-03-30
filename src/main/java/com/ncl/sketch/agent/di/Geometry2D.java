package com.ncl.sketch.agent.di;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;

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
        final double[] intersection = intersectionOf(a, d, c, b);
        if (intersection == null) {
            /*
             * Simple quadrilateral. Add the two areas. If quadrilateral is concave this will also work.
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
     * Returns the circumcircle of the triangle defined by the three specified {@link Point point}s: the
     * {@link Circle circle} which passes through all three point and whose center is equidistant from all three
     * {@link Point point}s.
     * 
     * @param p1 the first point
     * @param p2 the second point
     * @param p3 the third point
     * 
     * @return the circumcircle of the triangle defined by the three specified {@link Point point}s
     */
    static final Circle circumcircleOf(final Point p1, final Point p2, final Point p3) {
        final double slope12 = slope(p1, p2);
        final double slope23 = slope(p2, p3);

        final double centerX =
                (slope12 * slope23 * (p1.y() - p3.y()) + slope23 * (p1.x() + p2.x()) - slope12 * (p2.x() + p3.x()))
                    / (2 * (slope23 - slope12));
        final double centerY = -1 * (centerX - (p1.x() + p2.x()) / 2) / slope12 + (p1.y() + p2.y()) / 2;

        final Point center = point(centerX, centerY);
        final double radius = distance(center, p1);
        return new Circle(center, radius);
    }

    /**
     * Returns the distance between the two specified {@link Point point}s.
     * 
     * @param from first point
     * @param to second point
     * @return the distance between the two specified {@link Point point}s
     */
    static final double distance(final Point from, final Point to) {
        return norm(vector(from, to));
    }

    /**
     * Returns the intersection {@link Point point} of the two lines defined by their parametric equations: <i>y =
     * m * x + b</i>. Each line shall by specified as an array with two values: <i>[m, b]</i>.
     * 
     * @param line1 the equation of the first line
     * @param line2 the equation of the second line
     * @return the intersection {@link Point point} of the two lines or <code>null</code> if the two lines are
     *         parallels
     * @throws CoincidentLineException if the two lines are coincident
     */
    static final Point intersectionOf(final double[] line1, final double[] line2) throws CoincidentLineException {
        final double m1 = line1[0];
        final double m2 = line2[0];
        final double b1 = line1[1];
        final double b2 = line2[1];

        final Point result;
        if (isZero(b1 - b2) && isZero(m1 - m2)) {
            // coincident lines
            throw new CoincidentLineException();
        }
        if (isZero(m1 - m2)) {
            // parallel
            result = null;
        } else {
            final double x = (b2 - b1) / (m1 - m2);
            final double y = m1 * x + b1;
            result = point(x, y);
        }
        return result;
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
     * Returns [m, b] such that <i>y = m * x + b</i> for both specified {@link Point point}s.
     * 
     * @param p1 first point of the line
     * @param p2 second point of the line
     * @return [m, b] such that <i>y = m * x + b</i> for both specified {@link Point point}s
     */
    static final double[] lineEquationOf(final Point p1, final Point p2) {
        final double m = slope(p1, p2);
        final double[] midPoint = midPoint(p1, p2);

        /*
         * y = m * x + b -> b = y - m * x
         */
        final double b = midPoint[1] - m * midPoint[0];

        return new double[] { m, b };
    }

    /**
     * Returns the parameters of the perpendicular bisector of the two specified {@link Point point}s. The result
     * is an array containing <i>m</i> as first element and <i>b</i> as second element. The equation of the
     * perpendicular bisector being <i>y = m*x + b</i>.
     * 
     * @param p1 first point
     * @param p2 second point
     * @return the parameters of the perpendicular bisector of the two specified {@link Point point}s
     */
    static final double[] perpendicularBisectorOf(final Point p1, final Point p2) {
        final double slope = slope(p1, p2);
        /*
         * slope of perpendicular bisector is the negative reciprocal of the slope of the two points.
         */
        final double m = -1 / slope;

        final double[] midPoint = midPoint(p1, p2);

        /*
         * y = m * x + b -> b = y - m * x
         */
        final double b = midPoint[1] - m * midPoint[0];

        return new double[] { m, b };
    }

    /**
     * Returns a new {@link Point point} with the specified coordinates.
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @return a new {@link Point point} with the specified coordinates
     */
    static Point point(final double x, final double y) {
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

    /**
     * Orthogonally projects the specified {@link Point point} on the specified {@link Line line}.
     * 
     * @param point the point to projected
     * @param line the line on which the point shall be orthogonally projected
     * @return a new {@link Point point} that is the result of the orthogonal projection of the specified point on
     *         the specified line
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

    /**
     * Returns <code>true</code> iff the specified {@link Point point} is on the segment of line between
     * <i>from</i> and <i>to</i>.
     * 
     * @param pt the {@link Point point} to test
     * @param from the first point of the line segment
     * @param to the first point of the line segment
     * @return <code>true</code> iff the specified {@link Point point} is on the segment of line between
     *         <i>from</i> and <i>to</i>
     */
    static final boolean withinRange(final Point pt, final Point from, final Point to) {
        final double maxX = Math.max(from.x(), to.x());
        final double minX = Math.min(from.x(), to.x());
        final double maxY = Math.max(from.y(), to.y());
        final double minY = Math.min(from.y(), to.y());
        return pt.x() > minX && pt.x() < maxX && pt.y() > minY && pt.y() < maxY;
    }

    private static double dotProductOf(final double[] v1, final double[] v2) {
        return x(v1) * x(v2) + y(v1) * y(v2);
    }

    private static double exteriorProductOf(final double[] v1, final double[] v2) {
        return x(v1) * y(v2) - x(v2) * y(v1);
    }

    /*
     * returns [x,y] if such an intersection exists, [] if the two lines are collinear, null otherwise.
     */
    private static double[] intersectionOf(final Point a, final Point b, final Point c, final Point d) {
        final double[] eq1 = lineEquationOf(a, b);
        final double[] eq2 = lineEquationOf(c, d);
        double[] result;
        try {
            final Point i = intersectionOf(eq1, eq2);
            if (i == null) {
                // parallel
                result = null;
            } else {
                if (withinRange(i, a, b) && withinRange(i, c, d)) {
                    result = new double[] { i.x(), i.y() };
                } else {
                    // intersection outside lines
                    result = null;
                }
            }
        } catch (final CoincidentLineException e) {
            result = new double[] {};
        }
        return result;
    }

    private static boolean isZero(final double val) {
        return Math.abs(val) < ZERO;
    }

    private static double[] midPoint(final Point p1, final Point p2) {
        final double midX = (p1.x() + p2.x()) / 2;
        final double midY = (p1.y() + p2.y()) / 2;
        return new double[] { midX, midY };
    }

    private static double norm(final double[] v) {
        return Math.sqrt(x(v) * x(v) + y(v) * y(v));
    }

    private static double[] scale(final double scale, final double[] v) {
        return new double[] { scale * x(v), scale * y(v) };
    }

    private static double signedAreaOf(final Point a, final Point b, final Point c) {
        final double[] v1 = vector(a, b);
        final double[] v2 = vector(a, c);
        return 0.5 * exteriorProductOf(v1, v2);
    }

    /*
     * slope of line (p1, p2)
     */
    private static double slope(final Point p1, final Point p2) {
        return (p2.y() - p1.y()) / (p2.x() - p1.x());
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
