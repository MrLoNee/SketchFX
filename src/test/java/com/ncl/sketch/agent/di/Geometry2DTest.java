package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

public final class Geometry2DTest {

    private static final double DELTA = 0.00001;

    @Test
    public final void curvature() {
        final Stroke stroke = stroke();
        assertEquals(0.27604535, Geometry2D.curvature(stroke, 1, 2), 0.00001);
        assertEquals(0.12711770, Geometry2D.curvature(stroke, 4, 2), 0.00001);
    }

    @Test
    public final void direction() {
        final Stroke stroke = stroke();
        assertEquals(0.785398163, Geometry2D.direction(stroke, 1), 0.00001);
        assertEquals(Math.PI, Geometry2D.direction(stroke, 4), DELTA);
    }

    @Test
    public final void featureAreaLine() {
        final Stroke stroke = stroke();
        final Point start = Points.point(0, 8);
        final Point end = Points.point(8, 10);
        final Line line = new Line(start, end);
        assertEquals(66.5441176, Geometry2D.featureArea(stroke, line), DELTA);
    }

    @Test
    public final void featureAreaPoint() {
        final Stroke stroke = stroke();
        final Point reference = Points.point(2.5, 2.5);
        assertEquals(25.0, Geometry2D.featureArea(stroke, reference), DELTA);
    }

    private static Stroke stroke() {
        return new Stroke(0.0, Points.point(0.0, 0.0), Points.point(0.0, 5.0), Points.point(2.5, 7.5), Points.point(
                5.0, 5.0), Points.point(5.0, 0.0));
    }
}
