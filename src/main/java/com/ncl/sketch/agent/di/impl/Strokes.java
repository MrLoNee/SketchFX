package com.ncl.sketch.agent.di.impl;

import com.ncl.sketch.agent.api.Arc;
import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

/**
 * Helper functions pertaining to geometric calculations on {@link Stroke stroke}s.
 * 
 * @see <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.89.3800&amp;rep=rep1&amp;type=pdf">A
 *      Domain-Independent System for Sketch Recognition</a>
 */
final class Strokes {

    private static final double TWO_PI = 2.0 * Math.PI;

    private static final double DIRECTION_DISCONTINUITY_THRESHOLD = Math.PI;

    private Strokes() {
        // empty;
    }

    /**
     * Returns a new {@link Arc} that best approximate the specified {@link Stroke stroke}. The arc will pass
     * through the first and last {@link Point point} of the stroke and the intersection between the perpendicular
     * bisector to the line <i>(first, last)</i> and the stroke. Returns <code>null</code> if no such arc can be
     * computed.
     * 
     * @param stroke the {@link Stroke stroke}
     * @return a new {@link Arc} that best approximate the specified {@link Stroke stroke} or <code>null</code> if
     *         no such arc can be computed
     */
    static final Arc arc(final Stroke stroke) {
        final Point first = stroke.get(0);
        final Point last = stroke.get(stroke.size() - 1);
        final LineEquation bisector = Geometry2D.perpendicularBisectorOf(first, last);
        Point intersection = null;
        for (int i = 0; i < stroke.size() - 1; i++) {
            final Point from = stroke.get(i);
            final Point to = stroke.get(i + 1);
            final LineEquation line1 = Geometry2D.lineEquationOf(from, to);
            try {
                intersection = Geometry2D.intersectionOf(line1, bisector);
                if (intersection != null && Geometry2D.withinRange(intersection, from, to)) {
                    break;
                }
            } catch (final CoincidentLineException e) {
                // ignore and move to next stroke segment
            }
        }

        final Arc result;
        if (intersection == null) {
            // no intersection found, return null
            result = null;
        } else {
            final Circle circle = Geometry2D.circumcircleOf(first, intersection, last);
            final double firstAngle = Geometry2D.angleOf(first, circle.center());
            final double lastAngle = Geometry2D.angleOf(last, circle.center());
            if (firstAngle < lastAngle) {
                result = new Arc(circle.center(), circle.radius(), firstAngle, lastAngle - firstAngle);
            } else {
                result = new Arc(circle.center(), circle.radius(), lastAngle, firstAngle - lastAngle);
            }
        }

        return result;

    }

    /**
     * Returns a new {@link Circle circle} which takes the center of the stroke's bounding box as its own center
     * and the mean distance between the center and each stroke point as its radius.
     * 
     * @param stroke the {@link Stroke stroke}
     * @return a new {@link Circle circle} which takes the center of the stroke's bounding box as its own center
     *         and the mean distance between the center and each stroke point as its radius
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
        final Point centre = Geometry2D.point(x, y);

        double radius = 0.0;
        for (int i = 0; i < size; i++) {
            radius += Geometry2D.distance(centre, stroke.get(i));
        }
        radius = radius / size;

        return new Circle(centre, radius);
    }

    /**
     * Returns the direction graph of the specified stroke. The result is an array containing the direction of each
     * of the points of the stroke - expect for the last one obviously. The direction is an angle in
     * <strong>radians</strong>.
     * <p>
     * Note that the direction angle values are shifted in order to make the data distribute continuously on the
     * direction axis.
     * 
     * @param stroke the {@link Stroke stroke}
     * @return an array containing <code>{@link Stroke#size()} - 1</code> angles in <strong>radians</strong>
     */
    static final double[] directionGraph(final Stroke stroke) {
        final int strokeSize = stroke.size();
        final double[] result = new double[strokeSize - 1];
        int shift = 0;
        double previousDirection = Double.MIN_VALUE;
        for (int i = 0; i < strokeSize - 1; i++) {
            final double direction = direction(stroke, i);
            if (i > 0) {
                /* increase/decrease shift on discontinuity. */
                if (previousDirection - direction > DIRECTION_DISCONTINUITY_THRESHOLD) {
                    shift++;
                } else if (previousDirection - direction < -DIRECTION_DISCONTINUITY_THRESHOLD) {
                    shift--;
                }
            }
            result[i] = direction + TWO_PI * shift;
            previousDirection = direction;
        }
        return result;
    }

    /**
     * Returns the feature area of the specified stroke to the specified {@link Line line} which is computed as the
     * sum area of all the small quadrangles formed by two consecutive stroke points and their foot points on the
     * line.
     * 
     * @param stroke the {@link Stroke stroke}
     * @param line the reference {@link Line line}
     * @return the feature area of the specified stroke to the specified {@link Line line}
     */
    static final double featureArea(final Stroke stroke, final Line line) {
        final int strokeSize = stroke.size();
        double area = 0.0;
        for (int i = 0; i < strokeSize - 1; i++) {
            final Point a = stroke.get(i);
            final Point b = stroke.get(i + 1);
            final Point c = Geometry2D.project(b, line);
            final Point d = Geometry2D.project(a, line);
            area += Geometry2D.areaOf(a, b, c, d);
        }
        return area;
    }

    /**
     * Returns the feature area of the specified stroke against the specified reference {@link Point point} which
     * is equal to the sum area of all the small triangles formed by two consecutive stroke points and that
     * reference {@link Point point}.
     * 
     * @param stroke the {@link Stroke stroke}
     * @param point the reference {@link Point point}
     * @return the feature area of the specified stroke against the specified reference {@link Point point}
     */
    static final double featureArea(final Stroke stroke, final Point point) {
        final int strokeSize = stroke.size();
        double area = 0.0;
        for (int i = 0; i < strokeSize - 1; i++) {
            area += Geometry2D.areaOf(stroke.get(i), stroke.get(i + 1), point);

        }
        return area;
    }

    /**
     * Returns the index of the {@link Point point} in the specified {@link Stroke stroke} which has the highest
     * curvature - change in direction with respect to path length, of the n-th stroke point.
     * <p>
     * <i>k</i> is a small {@code integer} defining the neighborhood size around the n-th point. The authors of the
     * paper set it to 2 empirically as a tradeoff between the suppression of noise and the sensitivity of vertex
     * detection. This value may be decreased if the stroke does not contain enough points.
     * 
     * @param stroke the {@link Stroke stroke}
     * @param k a small {@code integer} defining the neighborhood size around the each point of the stroke
     * @return the index of the {@link Point point} in the specified {@link Stroke stroke} which has the highest
     *         curvature
     */
    static final int indexOfMaxCurvature(final Stroke stroke, final int k) {
        final int strokeSize = stroke.size();
        int result = -1;
        if (strokeSize == 3) {
            result = 1;
        } else if (strokeSize > 2) {
            int maxK = (int) Math.ceil((strokeSize - 1) / 2);
            maxK = strokeSize % 2 == 0 ? maxK : maxK - 1;
            final int actualK = Math.min(maxK, k);
            double maxCurvature = 0;
            for (int i = actualK; i < strokeSize - actualK - 1; i++) {
                final double curvature = curvature(stroke, i, actualK);
                if (curvature > maxCurvature) {
                    maxCurvature = curvature;
                    result = i;
                }
            }
        }
        return result;
    }

    /**
     * Returns the curvature - change in direction with respect to path length, of the n-th stroke point.
     * <p>
     * {@code k} k is a small {@code integer} defining the neighborhood size around the n-th point. The authors of
     * the paper set it to {@code 2} empirically as a tradeoff between the suppression of noise and the sensitivity
     * of vertex detection.
     * 
     * @param stroke the {@link Stroke stroke}
     * @param n the index of the point in the stroke for which the curvature shall be computed
     * @param k a small {@code integer} defining the neighborhood size around the n-th point
     * @return the curvature of the n-th stroke point
     */
    private static double curvature(final Stroke stroke, final int n, final int k) {
        double theta = 0.0;
        double di = direction(stroke, n - k);
        for (int i = n - k; i <= n + k - 1; i++) {
            final double di1 = direction(stroke, i + 1);
            theta += shift(di1 - di);
            di = di1;
        }
        theta = Math.abs(theta);
        final double distance = distance(stroke, n - k, n + k);
        return theta / distance;
    }

    private static double direction(final Point start, final Point end) {
        final double dy = end.y() - start.y();
        final double dx = end.x() - start.x();
        return Math.atan2(dy, dx);
    }

    /**
     * Returns the direction of the n-th stroke point. The result is an angle in <strong>radians</strong> in the
     * range of -<i>pi</i> to <i>pi</i>.
     * 
     * @param stroke the {@link Stroke stroke}
     * @param n the index of the point in the stroke for which the direction shall be computed
     * @return the direction of the n-th stroke point in <strong>radians</strong> in the range of -<i>pi</i> to
     *         <i>pi</i>
     */
    private static double direction(final Stroke stroke, final int n) {
        return direction(stroke.get(n), stroke.get(n + 1));
    }

    private static double distance(final Stroke stroke, final int from, final int to) {
        double distance = 0.0;
        for (int i = from; i < to; i++) {
            final Point fromPt = stroke.get(i);
            final Point toPt = stroke.get(i + 1);
            distance += Geometry2D.distance(fromPt, toPt);
        }
        return distance;
    }

    // shifts the specified angle in the range -pi to pi. Note that angle shall
    // be in range -3*pi to 3*pi
    private static double shift(final double angle) {
        if (angle > Math.PI) {
            return angle - TWO_PI;
        } else if (angle < -Math.PI) {
            return angle + TWO_PI;
        } else {
            return angle;
        }
    }

}
