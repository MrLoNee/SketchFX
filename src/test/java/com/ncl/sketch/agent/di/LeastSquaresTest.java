package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LeastSquaresTest {

    private static final double DELTA = 0.00001;

    @Test
    public final void regressionLine() {
        final double[] x = { 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72 };
        final double[] y = { 115, 117, 120, 123, 126, 129, 132, 135, 139, 142, 146, 150, 154, 159, 164 };
        final LeastSquares ls = new LeastSquares();
        final RegressionLine rl = ls.regressionLine(x, y);
        assertEquals(-87.516666666, rl.yIntercept(), DELTA);
        assertEquals(3.45, rl.slope(), DELTA);
        assertEquals(0.9910098, rl.coefficientOfDetermination(), DELTA);
    }

    @Test
    public final void regressionLineHorizontal() {
        final double[] x = { 1, 2, 3, 4, 5 };
        final double[] y = { 5, 5, 5, 5, 5 };
        final LeastSquares ls = new LeastSquares();
        final RegressionLine rl = ls.regressionLine(x, y);
        assertEquals(5.0, rl.yIntercept(), DELTA);
        assertEquals(0.0, rl.slope(), DELTA);
        assertEquals(1.0, rl.coefficientOfDetermination(), DELTA);
    }

    @Test
    public final void regressionLineVertical() {
        final double[] x = { 5, 5, 5, 5, 5 };
        final double[] y = { 1, 2, 3, 4, 5 };
        final LeastSquares ls = new LeastSquares();
        final RegressionLine rl = ls.regressionLine(x, y);
        assertEquals(0.0, rl.yIntercept(), DELTA);
        assertEquals(1.0, rl.slope(), DELTA);
        assertEquals(1.0, rl.coefficientOfDetermination(), DELTA);
    }

}
