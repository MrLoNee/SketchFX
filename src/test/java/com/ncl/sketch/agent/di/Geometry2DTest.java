package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;

public final class Geometry2DTest {

    private static final double DELTA = 0.00001;

    @Test
    public final void angleOf() {
        final Point reference = GeometricElements.point(15, 45);
        Point point = GeometricElements.point(15, 55);
        assertEquals(Math.PI / 2.0, Geometry2D.angleOf(point, reference), DELTA);
        point = GeometricElements.point(-15, 45);
        assertEquals(Math.PI, Geometry2D.angleOf(point, reference), DELTA);
        point = GeometricElements.point(15, 35);
        assertEquals(3 * Math.PI / 2.0, Geometry2D.angleOf(point, reference), DELTA);
        point = GeometricElements.point(15, 45);
        assertEquals(0.0, Geometry2D.angleOf(point, reference), DELTA);
    }

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
    public final void intersectionOf() throws CoincidentLineException {
        final LineEquation line1 = LineEquation.line(1, 3);
        final LineEquation line2 = LineEquation.line(-2, 12);
        final Point i = Geometry2D.intersectionOf(line1, line2);
        assertEquals(3, i.x(), DELTA);
        assertEquals(6, i.y(), DELTA);
    }

    @Test
    public final void intersectionOfBothHorizontal() throws CoincidentLineException {
        final LineEquation line1 = LineEquation.horizontalLine(3);
        final LineEquation line2 = LineEquation.horizontalLine(4);
        assertNull(Geometry2D.intersectionOf(line1, line2));
    }

    @Test(expected = CoincidentLineException.class)
    public final void intersectionOfBothHorizontalCoincident() throws CoincidentLineException {
        final LineEquation line1 = LineEquation.horizontalLine(4);
        final LineEquation line2 = LineEquation.horizontalLine(4);
        Geometry2D.intersectionOf(line1, line2);
    }

    @Test
    public final void intersectionOfBothVertical() throws CoincidentLineException {
        final LineEquation line1 = LineEquation.verticalLine(5);
        final LineEquation line2 = LineEquation.verticalLine(6);
        assertNull(Geometry2D.intersectionOf(line1, line2));
    }

    @Test(expected = CoincidentLineException.class)
    public final void intersectionOfBothVerticalCoincident() throws CoincidentLineException {
        final LineEquation line1 = LineEquation.verticalLine(5);
        final LineEquation line2 = LineEquation.verticalLine(5);
        Geometry2D.intersectionOf(line1, line2);
    }

    @Test(expected = CoincidentLineException.class)
    public final void intersectionOfCoincident() throws CoincidentLineException {
        final LineEquation line1 = LineEquation.line(1, 3);
        final LineEquation line2 = LineEquation.line(1.00000009, 3.0000009);
        Geometry2D.intersectionOf(line1, line2);
    }

    @Test
    public final void intersectionOfLineAndHorizontalLine() throws CoincidentLineException {
        final LineEquation line1 = LineEquation.line(1, 3);
        final LineEquation line2 = LineEquation.horizontalLine(5);
        final Point i = Geometry2D.intersectionOf(line1, line2);
        assertEquals(2, i.x(), DELTA);
        assertEquals(5, i.y(), DELTA);
    }

    @Test
    public final void intersectionOfLineAndVerticalLine() throws CoincidentLineException {
        final LineEquation line1 = LineEquation.line(1, 3);
        final LineEquation line2 = LineEquation.verticalLine(-7);
        final Point i = Geometry2D.intersectionOf(line1, line2);
        assertEquals(-7, i.x(), DELTA);
        assertEquals(-4, i.y(), DELTA);
    }

    @Test
    public final void intersectionOfParallel() throws CoincidentLineException {
        final LineEquation line1 = LineEquation.line(1, 3);
        final LineEquation line2 = LineEquation.line(1, 4);
        assertNull(Geometry2D.intersectionOf(line1, line2));
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
        final LineEquation eq = Geometry2D.lineEquationOf(p1, p2);
        assertEquals(-1.0 / 3.0, eq.slope(), DELTA);
        assertEquals(5.666666666, eq.yIntercept(), DELTA);
        assertEquals(17.0, eq.xIntercept(), DELTA);
        assertFalse(eq.isVertical());
    }

    @Test
    public final void lineEquationOfHorizontalLine() {
        final Point p1 = GeometricElements.point(2, 5);
        final Point p2 = GeometricElements.point(8, 5);
        final LineEquation eq = Geometry2D.lineEquationOf(p1, p2);
        assertEquals(0, eq.slope(), DELTA);
        assertEquals(5, eq.yIntercept(), DELTA);
        assertTrue(Double.isInfinite(eq.xIntercept()));
        assertFalse(eq.isVertical());
    }

    @Test
    public final void lineEquationOfVerticalLine() {
        final Point p1 = GeometricElements.point(2, 5);
        final Point p2 = GeometricElements.point(2, 3);
        final LineEquation eq = Geometry2D.lineEquationOf(p1, p2);
        assertTrue(Double.isInfinite(eq.slope()));
        assertTrue(Double.isInfinite(eq.yIntercept()));
        assertEquals(2, eq.xIntercept(), DELTA);
        assertTrue(eq.isVertical());
    }

    @Test
    public final void perpendicularBisectorOf() {
        final Point p1 = GeometricElements.point(2, 5);
        final Point p2 = GeometricElements.point(8, 3);
        final LineEquation eq = Geometry2D.perpendicularBisectorOf(p1, p2);
        assertEquals(3, eq.slope(), DELTA);
        assertEquals(-11, eq.yIntercept(), DELTA);
        assertEquals(3.6666666667, eq.xIntercept(), DELTA);
        assertFalse(eq.isVertical());

    }

    @Test
    public final void perpendicularBisectorOfHorizontalLine() {
        final Point p1 = GeometricElements.point(2, 5);
        final Point p2 = GeometricElements.point(8, 5);
        final LineEquation eq = Geometry2D.perpendicularBisectorOf(p1, p2);
        assertTrue(Double.isInfinite(eq.slope()));
        assertTrue(Double.isInfinite(eq.yIntercept()));
        assertEquals(5, eq.xIntercept(), DELTA);
        assertTrue(eq.isVertical());
    }

    @Test
    public final void perpendicularBisectorOfVerticalLine() {
        final Point p1 = GeometricElements.point(2, 5);
        final Point p2 = GeometricElements.point(2, 3);
        final LineEquation eq = Geometry2D.perpendicularBisectorOf(p1, p2);
        assertEquals(0, eq.slope(), DELTA);
        assertEquals(4, eq.yIntercept(), DELTA);
        assertTrue(Double.isInfinite(eq.xIntercept()));
        assertFalse(eq.isVertical());
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

    @Test
    public final void withinRange() {
        final Point pt = GeometricElements.point(3, 6);
        final Point from = GeometricElements.point(2, 5);
        final Point to = GeometricElements.point(4, 7);
        assertTrue(Geometry2D.withinRange(pt, from, to));
    }

    @Test
    public final void withinRangeAboveX() {
        final Point pt = GeometricElements.point(5, 6);
        final Point from = GeometricElements.point(2, 5);
        final Point to = GeometricElements.point(4, 7);
        assertFalse(Geometry2D.withinRange(pt, from, to));
    }

    @Test
    public final void withinRangeAboveY() {
        final Point pt = GeometricElements.point(3, 4);
        final Point from = GeometricElements.point(2, 5);
        final Point to = GeometricElements.point(4, 7);
        assertFalse(Geometry2D.withinRange(pt, from, to));
    }

    @Test
    public final void withinRangeBelowX() {
        final Point pt = GeometricElements.point(1, 6);
        final Point from = GeometricElements.point(2, 5);
        final Point to = GeometricElements.point(4, 7);
        assertFalse(Geometry2D.withinRange(pt, from, to));
    }

    @Test
    public final void withinRangeBelowY() {
        final Point pt = GeometricElements.point(3, 8);
        final Point from = GeometricElements.point(2, 5);
        final Point to = GeometricElements.point(4, 7);
        assertFalse(Geometry2D.withinRange(pt, from, to));
    }

}
