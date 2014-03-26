package com.ncl.sketch.agent.di;

import java.util.logging.Logger;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Stroke;

/**
 * A specialized {@link PatternRecognizer} that recognizes circles.
 */
final class CirclePatternRecognizer implements PatternRecognizer,
	CirclePatternRecognizerParameters {

    private static final Logger LOGGER = Logger.getLogger("DI-Agent");

    private static final double TWO_PI = 2.0 * Math.PI;

    private final LeastSquares ls;

    private double minCorrelation;

    private double maxAreaRatio;

    private double maxSlopeError;

    /**
     * Constructor.
     *
     * @param minimumCorrelation
     *            a {@code double} in range <i>0.0</i> to <i>1.0</i> used to
     *            assess the fit of the computed regression line derived from
     *            the direction graph of the stroke. The higher the value the
     *            more restrictive the fit will be
     * @param maximumAreaRatio
     *            max ratio (feature area of this stroke to the candidate circle
     *            / candidate circle area) above which the candidate circle will
     *            be considered unacceptable. A good value is <i>1.0</i>
     */
    CirclePatternRecognizer(final double minimumCorrelation,
	    final double maximumAreaRatio, final double maximumSlopeError) {
	minCorrelation = minimumCorrelation;
	maxAreaRatio = maximumAreaRatio;
	maxSlopeError = maximumSlopeError;
	ls = new LeastSquares();
    }

    @Override
    public final boolean recognize(final Stroke stroke,
	    final StrokeRecognitionResult result) {
	LOGGER.fine("Processing stroke with " + stroke.size() + " points");
	final boolean fitsCircle = fitsCircle(stroke);
	final boolean isCircle;
	if (fitsCircle) {
	    final Circle candidate = Geometry2D.circle(stroke);
	    final double featureArea = Strokes.featureArea(stroke,
		    candidate.center());
	    final double candidateArea = Geometry2D.areaOf(candidate);
	    final double areaRatio = featureArea / candidateArea;
	    isCircle = areaRatio < maxAreaRatio;
	    LOGGER.fine("Feature area: " + featureArea + "; Candidate area: "
		    + candidateArea + "; ratio: " + areaRatio);
	    if (isCircle) {
		LOGGER.info("Recognized circle: " + candidate
			+ " from stroke with " + stroke.size() + " points");
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
	final double slopeError = Math.abs(rl.slope() - perfectSlope)
		/ perfectSlope;
	return rl.coefficientOfDetermination() >= minCorrelation
		&& slopeError < maxSlopeError;
    }

    @Override
    public DefaultPatternRecognizerParameters setMinCorrelation(
	    final double minCorrelationVal) {
	minCorrelation = minCorrelationVal;
	return this;
    }

    @Override
    public DefaultPatternRecognizerParameters setMaxAreaRatio(
	    final double maxAreaRatioVal) {
	maxAreaRatio = maxAreaRatioVal;
	return this;
    }

    @Override
    public double getMinCorrelation() {
	return minCorrelation;
    }

    @Override
    public double getMaxAreaRatio() {
	return maxAreaRatio;
    }

    @Override
    public CirclePatternRecognizerParameters maxSlopeError(
	    final double maxSlopeErrorVal) {
	maxSlopeError = maxSlopeErrorVal;
	return this;
    }

    @Override
    public double maxSlopeError() {
	return maxSlopeError;
    }
}
