package com.ncl.sketch.agent.di;

final class LinePatternRecognizer implements PatternRecognizer {

    private final double maxSse;

    private final double maxAreaRatio;

    /**
     * Constructor.
     * 
     * @param aMaxSse maximum error sum of squares above which the regression line will be
     *            considered unacceptable
     * @param aMaxAreaRatio max ratio (feature area of this stroke to the candidate line / candidate
     *            line area) above which the candidate line found by linear regression will be
     *            considered unacceptable. A good value is {@code 1.0}
     */
    LinePatternRecognizer(final double aMaxSse, final double aMaxAreaRatio) {
        maxSse = aMaxSse;
        maxAreaRatio = aMaxAreaRatio;
    }

    @Override
    public final boolean recognize(final Stroke stroke) {
        final boolean lsrlAcceptable = leastSquaresRegressionLineAcceptable(stroke);
        final boolean result;
        if (lsrlAcceptable) {
            final Line candidate = new Line(stroke.get(0), stroke.get(stroke.size() - 1));
            final double featureArea = StrokeGeometry.featureArea(stroke, candidate);
            final double lineArea = StrokeGeometry.length(candidate) * stroke.width();
            result = featureArea / lineArea < maxAreaRatio;
        } else {
            result = false;
        }
        return result;
    }

    /**
     * @see http://www.stat.purdue.edu/~xuanyaoh/stat350/xyApr6Lec26.pdf
     */
    private boolean leastSquaresRegressionLineAcceptable(final Stroke stroke) {
        final int strokeSize = stroke.size();
        final double[] x = new double[strokeSize];
        final double[] y = new double[strokeSize];

        /*
         * compute xbar and ybar.
         */

        double sumx = 0.0;
        double sumy = 0.0;
        for (int i = 0; i < strokeSize; i++) {
            final Point pt = stroke.get(i);
            x[i] = pt.x();
            y[i] = pt.y();
            sumx += x[i];
            sumy += y[i];
        }

        final double xbar = sumx / strokeSize;
        final double ybar = sumy / strokeSize;

        /*
         * compute a & b (equation of regression line is y = a + bx).
         */
        double xxbar = 0.0;
        double xybar = 0.0;
        for (int i = 0; i < strokeSize; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        final double b = xybar / xxbar;
        final double a = ybar - b * xbar;

        /*
         * compute error sum of squares: sse
         */

        double sse = 0.0;
        for (int i = 0; i < strokeSize; i++) {
            final double fit = a + b * x[i];
            sse += (fit - y[i]) * (fit - y[i]);
        }

        return sse < maxSse;

    }

}
