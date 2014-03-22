package com.ncl.sketch.agent.di;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

final class GeometricElements {

    private GeometricElements() {

    }

    static final Stroke stroke(final double[] x, final double[] y) {
        if (x.length != y.length) {
            fail();
        }
        final Point[] points = new Point[x.length];
        for (int i = 0; i < x.length; i++) {
            points[i] = point(x[i], y[i]);
        }
        return new Stroke(1.0, points);
    }

    static final Point point(final double x, final double y) {
        final Point pt = mock(Point.class);
        when(pt.x()).thenReturn(x);
        when(pt.y()).thenReturn(y);
        when(pt.toString()).thenReturn("[" + x + ", " + y + "]");
        return pt;
    }

}
