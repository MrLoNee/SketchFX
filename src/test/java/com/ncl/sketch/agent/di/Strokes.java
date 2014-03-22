package com.ncl.sketch.agent.di;

import static org.junit.Assert.fail;

import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

final class Strokes {

    private Strokes() {

    }

    static final Stroke stroke(final double[] x, final double[] y) {
        if (x.length != y.length) {
            fail();
        }
        final Point[] points = new Point[x.length];
        for (int i = 0; i < x.length; i++) {
            points[i] = Points.point(x[i], y[i]);
        }
        return new Stroke(1.0, points);
    }

}
