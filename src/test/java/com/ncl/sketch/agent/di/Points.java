package com.ncl.sketch.agent.di;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ncl.sketch.agent.api.Point;

final class Points {

    private Points() {

    }

    static final Point point(final double x, final double y) {
        final Point pt = mock(Point.class);
        when(pt.x()).thenReturn(x);
        when(pt.y()).thenReturn(y);
        when(pt.toString()).thenReturn("[" + x + ", " + y + "]");
        return pt;
    }

}
