package com.ncl.sketch.agent.di;

import javax.media.j3d.Link;

import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

/**
 * Helper functions pertaining to geometric calculations on {@link Stroke
 * stroke}s.
 * 
 * @see {@link Link
 *      http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.89.3800
 *      &rep=rep1&type=pdf}
 */
final class Strokes {

    private Strokes() {
        // empty;
    }

    /**
     * Returns the curvature - change in direction with respect to path length,
     * of the n-th stroke point.
     * <p>
     * {@code k} k is a small {@code integer} defining the neighborhood size
     * around the n-th point. The authors of the paper set it to {@code 2}
     * empirically as a tradeoff between the suppression of noise and the
     * sensitivity of vertex detection.
     * 
     * @param stroke the {@link Stroke stroke}
     * @param n the index of the point in the stroke for which the curvature
     *        shall be computed
     * @param k a small {@code integer} defining the neighborhood size around
     *        the n-th point
     * @return the curvature of the n-th stroke point
     */
    static final double curvature(final Stroke stroke, final int n, final int k) {
        double theta = 0.0;
        final int strokeSize = stroke.size();
        for (int i = n - k; i <= n + k - 1; i++) {
            theta += direction(stroke, absoluteIndex(strokeSize, i));
        }
        theta = Math.abs(theta);
        final double distance = distance(stroke, n - k, n + k);
        return theta / distance;
    }

    /**
     * Returns the direction of the n-th stroke point. The result is an angle in
     * <strong>radians</strong> in the range of -<i>pi</i> to <i>pi</i>.
     * 
     * @param stroke the {@link Stroke stroke}
     * @param n the index of the point in the stroke for which the direction
     *        shall be computed
     * @return the direction of the n-th stroke point in
     *         <strong>radians</strong> in the range of -<i>pi</i> to <i>pi</i>
     */
    static final double direction(final Stroke stroke, final int n) {
        return direction(stroke.get(n), stroke.get(absoluteIndex(stroke.size(), n + 1)));
    }

    /**
     * Returns the feature area of the specified stroke to the specified
     * {@link Line line} which is computed as the sum area of all the small
     * quadrangles formed by two consecutive stroke points and their foot points
     * on the line.
     * 
     * @param stroke the {@link Stroke stroke}
     * @param line the reference {@link Line line}
     * @return the feature area of the specified stroke to the specified
     *         {@link Line line}
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
     * Returns the feature area of the specified stroke against the specified
     * reference {@link Point point} which is equal to the sum area of all the
     * small triangles formed by two consecutive stroke points and that
     * reference {@link Point point}.
     * 
     * @param stroke the {@link Stroke stroke}
     * @param point the reference {@link Point point}
     * @return the feature area of the specified stroke against the specified
     *         reference {@link Point point}
     */
    static final double featureArea(final Stroke stroke, final Point point) {
        final int strokeSize = stroke.size();
        double area = 0.0;
        for (int i = 0; i < strokeSize - 1; i++) {
            area += Geometry2D.areaOf(stroke.get(i), stroke.get(i + 1), point);

        }
        return area;
    }

   

    private static int absoluteIndex(final int strokeSize, final int relativeIndex) {
        final int result;
        if (relativeIndex < 0) {
            result = strokeSize + relativeIndex;
        } else if (relativeIndex >= strokeSize) {
            result = relativeIndex - strokeSize;
        } else {
            result = relativeIndex;
        }
        return result;
    }

    private static double direction(final Point start, final Point end) {
        final double dy = end.y() - start.y();
        final double dx = end.x() - start.x();
        return Math.atan2(dy, dx);
    }

 

    private static double distance(final Stroke stroke, final int from, final int to) {
        double distance = 0.0;
        final int strokeSize = stroke.size();
        for (int i = from; i < to; i++) {
            final Point fromPt = stroke.get(absoluteIndex(strokeSize, i));
            final Point toPt = stroke.get(absoluteIndex(strokeSize, i + 1));
            distance += Geometry2D.distance(fromPt, toPt);
        }
        return distance;
    }

   
}
