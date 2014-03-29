package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;

public final class Geometry2DTest {

    private static final double DELTA = 0.00001;

    @Test
    public final void circleArea() {
        final Circle circle = new Circle(null, 154.0);
        assertEquals(74506.01137, Geometry2D.areaOf(circle), DELTA);
    }

    @Test
    public final void circumcircle() {
        final Point p1 = GeometricElements.point(154, 15);
        final Point p2 = GeometricElements.point(-15, 45);
        final Point p3 = GeometricElements.point(54, 145);
        final Circle circumcircle = Geometry2D.circumcircleOf(p1, p2, p3);
        final Point circumcenter = circumcircle.center();
        assertEquals(74.32340, circumcenter.x(), DELTA);
        assertEquals(57.17185, circumcenter.y(), DELTA);
        assertEquals(circumcircle.radius(), Geometry2D.distance(p1, circumcenter), DELTA);
        assertEquals(circumcircle.radius(), Geometry2D.distance(p2, circumcenter), DELTA);
        assertEquals(circumcircle.radius(), Geometry2D.distance(p3, circumcenter), DELTA);
    }

    @Test
    public final void distance() {
        final Point from = GeometricElements.point(5.6, -18.9);
        final Point to = GeometricElements.point(154, 89.45);
        assertEquals(183.74515639, Geometry2D.distance(from, to), DELTA);
    }

    @Test
    public final void lengthOf() {
        final Point start = GeometricElements.point(5.6, -18.9);
        final Point end = GeometricElements.point(154, 89.45);
        final Line line = new Line(start, end);
        assertEquals(183.74515639, Geometry2D.lengthOf(line), DELTA);
    }

    @Test
    public final void lineEquationOf() {
        final Point p1 = GeometricElements.point(2, 5);
        final Point p2 = GeometricElements.point(8, 3);
        final double[] pb = Geometry2D.lineEquationOf(p1, p2);
        assertEquals(-1.0 / 3.0, pb[0], DELTA);
        assertEquals(5.666666666, pb[1], DELTA);
    }

    @Test
    public final void perpendicularBisectorOf() {
        final Point p1 = GeometricElements.point(2, 5);
        final Point p2 = GeometricElements.point(8, 3);
        final double[] pb = Geometry2D.perpendicularBisectorOf(p1, p2);
        assertEquals(3, pb[0], DELTA);
        assertEquals(-11, pb[1], DELTA);
    }

    @Test
    public final void project() {
        final Point a = GeometricElements.point(5.6, -18.9);
        final Point start = GeometricElements.point(154, 89.45);
        final Point end = GeometricElements.point(36.8, -28.75);
        final Line line = new Line(start, end);
        final Point projection = Geometry2D.project(a, line);
        assertEquals(26.257360728, projection.x(), DELTA);
        assertEquals(-39.382593532, projection.y(), DELTA);
    }

    @Test
    public final void projectOnHorizontalLine() {
        final Point a = GeometricElements.point(148.4, -18.9);
        final Point start = GeometricElements.point(5.6, -25.4);
        final Point end = GeometricElements.point(14.32, -25.4);
        final Line line = new Line(start, end);
        final Point projection = Geometry2D.project(a, line);
        assertEquals(148.4, projection.x(), DELTA);
        assertEquals(-25.4, projection.y(), DELTA);
    }

    @Test
    public final void projectOnVerticalLine() {
        final Point a = GeometricElements.point(148.4, -18.9);
        final Point start = GeometricElements.point(5.6, -25.4);
        final Point end = GeometricElements.point(5.6, 89.45);
        final Line line = new Line(start, end);
        final Point projection = Geometry2D.project(a, line);
        assertEquals(5.6, projection.x(), DELTA);
        assertEquals(-18.9, projection.y(), DELTA);
    }

    @Test
    public final void quadriangleArea() {
        final Point a = GeometricElements.point(1, 1);
        final Point b = GeometricElements.point(1, 5);
        final Point c = GeometricElements.point(5, 5);
        final Point d = GeometricElements.point(5, 1);
        assertEquals(16.0, Geometry2D.areaOf(a, b, c, d), DELTA);
    }

    @Test
    public final void selfIntersectingQuadriangleArea() {
        final Point a = GeometricElements.point(1, 1);
        final Point b = GeometricElements.point(1, 5);
        final Point c = GeometricElements.point(5, 1);
        final Point d = GeometricElements.point(5, 5);
        assertEquals(8.0, Geometry2D.areaOf(a, b, c, d), DELTA);
    }

    @Test
    public final void triangleArea() {
        final Point a = GeometricElements.point(5.6, -18.9);
        final Point b = GeometricElements.point(154, 89.45);
        final Point c = GeometricElements.point(36.8, -28.75);
        assertEquals(2421.13, Geometry2D.areaOf(a, b, c), DELTA);
    }

}
