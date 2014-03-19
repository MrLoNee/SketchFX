package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public final class StrokeGeometryTest {

    private static final double DELTA = 0.00001;

    @Test
    public final void curvature() {
        final List<Point> stroke = stroke();
        assertEquals(0.27604535, StrokeGeometry.curvature(stroke, 1, 2), 0.00001);
        assertEquals(0.12711770, StrokeGeometry.curvature(stroke, 4, 2), 0.00001);
    }

    @Test
    public final void direction() {
        final List<Point> stroke = stroke();
        assertEquals(0.785398163, StrokeGeometry.direction(stroke, 1), 0.00001);
        assertEquals(Math.PI, StrokeGeometry.direction(stroke, 4), DELTA);
    }

    @Test
    public final void featureAreaLine() {
        final List<Point> stroke = stroke();
        final Point start = newPoint2D(0, 8);
        final Point end = newPoint2D(8, 10);
        final Line line = mock(Line.class);
        when(line.start()).thenReturn(start);
        when(line.end()).thenReturn(end);
        assertEquals(66.5441176, StrokeGeometry.featureArea(stroke, line), DELTA);
    }

    @Test
    public final void featureAreaPoint() {
        final List<Point> stroke = stroke();
        final Point reference = newPoint2D(2.5, 2.5);
        assertEquals(25.0, StrokeGeometry.featureArea(stroke, reference), DELTA);
    }

    private static Point newPoint2D(final double x, final double y) {
        final Point pt = mock(Point.class);
        when(pt.x()).thenReturn(x);
        when(pt.y()).thenReturn(y);
        return pt;
    }

    private static List<Point> stroke() {
        final List<Point> stroke = new ArrayList<>();
        stroke.add(newPoint2D(0.0, 0.0));
        stroke.add(newPoint2D(0.0, 5.0));
        stroke.add(newPoint2D(2.5, 7.5));
        stroke.add(newPoint2D(5.0, 5.0));
        stroke.add(newPoint2D(5.0, 0.0));
        return stroke;
    }

}
