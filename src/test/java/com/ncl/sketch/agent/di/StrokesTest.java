package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

public final class StrokesTest {

    private static final double DELTA = 0.00001;

    @Test
    public final void curvature() {
        final Stroke stroke = stroke();
        assertEquals(0.27604535, Strokes.curvature(stroke, 1, 2), 0.00001);
        assertEquals(0.12711770, Strokes.curvature(stroke, 4, 2), 0.00001);
    }

    @Test
    public final void direction() {
        final Stroke stroke = stroke();
        assertEquals(0.785398163, Strokes.direction(stroke, 1), 0.00001);
        assertEquals(Math.PI, Strokes.direction(stroke, 4), DELTA);
    }

    @Test
    public final void featureAreaLine() {
        final Stroke stroke = stroke();
        final Point start = GeometricElements.point(0, 8);
        final Point end = GeometricElements.point(8, 10);
        final Line line = new Line(start, end);
        assertEquals(25.2205882, Strokes.featureArea(stroke, line), DELTA);
    }

    @Test
    public final void featureAreaPoint() {
        final Stroke stroke = stroke();
        final Point reference = GeometricElements.point(2.5, 2.5);
        assertEquals(25.0, Strokes.featureArea(stroke, reference), DELTA);
    }

    private static Stroke stroke() {
        final double[] x = { 0, 0, 2.5, 5, 5 };
        final double[] y = { 0, 5, 7.5, 5, 0 };
        return GeometricElements.stroke(x, y);
    }
}
