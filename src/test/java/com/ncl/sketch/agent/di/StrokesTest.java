package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

public final class StrokesTest {

    private static final double DELTA = 0.00001;

    private static Stroke stroke() {
        final double[] x = { 0, 0, 2.5, 5, 5 };
        final double[] y = { 0, 5, 7.5, 5, 0 };
        return GeometricElements.stroke(x, y);
    }

    @Test
    public final void direction() {
        final Stroke stroke = stroke();
        final double[] directionGraph = Strokes.directionGraph(stroke);
        assertEquals(4, directionGraph.length);
        assertEquals(Math.PI / 2, directionGraph[0], DELTA);
        assertEquals(0.785398163, directionGraph[1], DELTA);
        assertEquals(2 * Math.PI - 0.785398163, directionGraph[2], DELTA);
        assertEquals(2 * Math.PI - Math.PI / 2, directionGraph[3], DELTA);
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

    @Test
    public final void indexOfMaxCurvature() {
        final Stroke stroke = stroke();
        assertEquals(2, Strokes.indexOfMaxCurvature(stroke, 2));
    }

    @Test
    public final void indexOfMaxCurvature3Points() {
        final double[] x = { 0, 0, 2.5 };
        final double[] y = { 0, 5, 7.5 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        assertEquals(1, Strokes.indexOfMaxCurvature(stroke, 2));
    }
}
