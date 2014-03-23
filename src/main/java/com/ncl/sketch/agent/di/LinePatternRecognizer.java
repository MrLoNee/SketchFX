package com.ncl.sketch.agent.di;

import java.util.logging.Logger;

import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

/**
 * A specialized {@link PatternRecognizer} that recognizes lines.
 */
final class LinePatternRecognizer implements PatternRecognizer {

    private static final Logger LOGGER = Logger.getLogger("DI-Agent");

    private final double minCorrelation;

    private final double maxAreaRatio;

    /**
     * Constructor.
     * 
     * @param minimumCorrelation a {@code double} in range <i>0.0</i> to
     *        <i>1.0</i> used to assess the fit of the computed regression line.
     *        The higher the value the more restrictive the fit will be. A value
     *        of <i>0.7</i> is a good comprise to ensure correct recognition
     * @param maximumAreaRatio max ratio (feature area of this stroke to the
     *        candidate line / candidate line area) above which the candidate
     *        line found by linear regression will be considered unacceptable. A
     *        good value is <i>1.0</i>
     */
    LinePatternRecognizer(final double minimumCorrelation, final double maximumAreaRatio) {
        minCorrelation = minimumCorrelation;
        maxAreaRatio = maximumAreaRatio;
    }

    @Override
    public final boolean recognize(final Stroke stroke, final StrokeRecognitionResult result) {
        LOGGER.fine("Processing stroke with " + stroke.size() + " points");
        final boolean lsrlAcceptable = leastSquaresRegressionLineAcceptable(stroke);
        final boolean isLine;
        if (lsrlAcceptable) {
            final Line candidate = new Line(stroke.get(0), stroke.get(stroke.size() - 1));
            final double featureArea = Strokes.featureArea(stroke, candidate);
            final double lineArea = Geometry2D.length(candidate) * stroke.width();
            final double areaRatio = featureArea / lineArea;
            isLine = areaRatio < maxAreaRatio;
            LOGGER.fine("Feature area: " + featureArea + "; Line area: " + lineArea + "; ratio: " + areaRatio);
            if (isLine) {
                LOGGER.info("Recognized line: " + candidate + " from stroke with " + stroke.size() + " points");
                result.add(candidate);
            }
        } else {
            isLine = false;
        }
        return isLine;
    }

    /**
     * @see {@link http://www.stat.purdue.edu/~xuanyaoh/stat350/xyApr6Lec26.pdf}
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

        /*
         * compute total sum of squares: sst
         */

        double sst = 0.0;
        for (int i = 0; i < strokeSize; i++) {
            sst += (ybar - y[i]) * (ybar - y[i]);
        }

        /*
         * compute coefficient of determination: r2
         */

        final double r2 = 1 - sse / sst;

        final StringBuilder msg = new StringBuilder("Least-squares regression result: y = ").append(a).append(" + ")
                .append(b).append("x; sse = ").append(sse).append("; sst = ").append(sst).append("; r2 = ").append(r2);
        LOGGER.fine(msg.toString());

        return Double.isNaN(r2) || r2 >= minCorrelation;

    }

}
