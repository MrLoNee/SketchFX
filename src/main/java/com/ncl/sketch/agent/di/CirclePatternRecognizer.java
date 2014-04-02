package com.ncl.sketch.agent.di;

import java.util.logging.Logger;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Stroke;
import com.ncl.sketch.agent.di.api.CircleRecognitionParameters;

/**
 * A specialized {@link PatternRecognizer} that recognizes circles.
 */
final class CirclePatternRecognizer implements PatternRecognizer, CircleRecognitionParameters {

    private static final Logger LOGGER = Logger.getLogger("DI-Agent");

    private static final double TWO_PI = 2.0 * Math.PI;

    private final LeastSquares ls;

    private double minCorrelation;

    private double maxAreaError;

    private double maxSlopeError;

    /**
     * Constructor.
     *
     * @param minimumCorrelation a {@code double} in range <i>0.0</i> to <i>1.0</i> used to assess the fit of the
     *            computed regression line derived from the direction graph of the stroke. The higher the value the
     *            more restrictive the fit will be
     * @param maximumAreaError the maximum error between the feature area of this stroke to the candidate circle
     *            center and the candidate circle area above which the candidate circle will be considered
     *            unacceptable. This is a {@code double} in range <i>0.0</i> to <i>1.0</i> with a low value
     *            (typically <i>0.1</i>)
     */
    CirclePatternRecognizer(final double minimumCorrelation, final double maximumAreaError,
            final double maximumSlopeError) {
        minCorrelation = minimumCorrelation;
        maxAreaError = maximumAreaError;
        maxSlopeError = maximumSlopeError;
        ls = new LeastSquares();
    }

    @Override
    public final double minCorrelation() {
        return minCorrelation;
    }

    @Override
    public final double maxSlopeError() {
        return maxSlopeError;
    }

    @Override
    public final CircleRecognitionParameters maxSlopeError(final double maxSlopeErrorVal) {
        maxSlopeError = maxSlopeErrorVal;
        return this;
    }

    @Override
    public final CircleRecognitionParameters minCorrelation(final double minCorrelationVal) {
        minCorrelation = minCorrelationVal;
        return this;
    }

    @Override
    public CircleRecognitionParameters maxAreaError(final double maxAreaError) {
	this.maxAreaError = maxAreaError;
	return this;
    }

    @Override
    public double maxAreaError() {
	return maxAreaError;
    }


    @Override
    public final boolean recognize(final Stroke stroke, final StrokeRecognitionResult result) {
        LOGGER.fine("Processing stroke with " + stroke.size() + " points");
        final boolean fitsCircle = fitsCircle(stroke);
        final boolean isCircle;
        if (fitsCircle) {
            final Circle candidate = Strokes.circle(stroke);
            final double featureArea = Strokes.featureArea(stroke, candidate.center());
            final double candidateArea = Geometry2D.areaOf(candidate);
            final double areaError = error(featureArea, candidateArea);
            isCircle = areaError < maxAreaError;
            LOGGER.info("Feature area: "
                + featureArea
                + "; Candidate area: "
                + candidateArea
                + "; error: "
                + areaError);
            if (isCircle) {
                LOGGER.info("Recognized circle: " + candidate + " from stroke with " + stroke.size() + " points");
                result.add(candidate);
            }
        } else {
            isCircle = false;
        }
        return isCircle;
    }

    private boolean fitsCircle(final Stroke stroke) {
        final int strokeSize = stroke.size();
        final double[] directionGraph = Strokes.directionGraph(stroke);
        final double x[] = new double[strokeSize - 1];
        for (int i = 0; i < strokeSize - 1; i++) {
            x[i] = i;
        }
        final RegressionLine rl = ls.regressionLine(x, directionGraph);
        final double perfectSlope = TWO_PI / strokeSize;
        final double absActualSlope = Math.abs(rl.slope());
        final double slopeError = error(absActualSlope, perfectSlope);
        return rl.coefficientOfDetermination() >= minCorrelation && slopeError < maxSlopeError;
    }

    private static double error(final double actual, final double expected) {
        return Math.abs(actual - expected) / expected;
    }

}
