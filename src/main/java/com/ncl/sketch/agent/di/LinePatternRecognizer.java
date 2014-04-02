package com.ncl.sketch.agent.di;

import java.util.logging.Logger;

import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Stroke;
import com.ncl.sketch.agent.di.api.LineRecognitionParameters;

/**
 * A specialized {@link PatternRecognizer} that recognizes {@link Line line}s.
 */
final class LinePatternRecognizer implements PatternRecognizer,
	LineRecognitionParameters {

    private static final Logger LOGGER = Logger.getLogger("DI-Agent");

    private double minCorrelation;

    private double maxAreaRatio;

    private final LeastSquares ls;

    /**
     * Constructor.
     *
     * @param minimumCorrelation
     *            a {@code double} in range <i>0.0</i> to <i>1.0</i> used to
     *            assess the fit of the computed regression line. The higher the
     *            value the more restrictive the fit will be. A value of
     *            <i>0.7</i> is a good comprise to ensure correct recognition
     * @param maximumAreaRatio
     *            max ratio (feature area of this stroke to the candidate line /
     *            candidate line area) above which the candidate line found by
     *            linear regression will be considered unacceptable. A good
     *            value is <i>1.0</i>
     */
    LinePatternRecognizer(final double minimumCorrelation,
	    final double maximumAreaRatio) {
	minCorrelation = minimumCorrelation;
	maxAreaRatio = maximumAreaRatio;
	ls = new LeastSquares();
    }

    @Override
    public double maxAreaError() {
	return maxAreaRatio;
    }

    @Override
    public double minCorrelation() {
	return minCorrelation;
    }

    @Override
    public final LineRecognitionParameters maxAreaError(
	    final double aMaxAreaRatio) {
	maxAreaRatio = aMaxAreaRatio;
	return this;
    }

    @Override
    public final LineRecognitionParameters minCorrelation(
	    final double aMinCorrelation) {
	minCorrelation = aMinCorrelation;
	return this;
    }

    @Override
    public final boolean recognize(final Stroke stroke,
	    final StrokeRecognitionResult result) {
	LOGGER.fine("Processing stroke with " + stroke.size() + " points");
	final boolean fitsLine = fitsLine(stroke);
	final boolean isLine;
	if (fitsLine) {
	    final Line candidate = new Line(stroke.get(0), stroke.get(stroke
		    .size() - 1));
	    final double featureArea = Strokes.featureArea(stroke, candidate);
	    final double candidateArea = Geometry2D.lengthOf(candidate)
		    * stroke.width();
	    final double areaRatio = featureArea / candidateArea;
	    isLine = areaRatio < maxAreaRatio;
	    LOGGER.fine("Feature area: " + featureArea + "; Candidate area: "
		    + candidateArea + "; ratio: " + areaRatio);
	    if (isLine) {
		LOGGER.info("Recognized line: " + candidate
			+ " from stroke with " + stroke.size() + " points");
		result.add(candidate);
	    }
	} else {
	    isLine = false;
	}
	return isLine;
    }

    private boolean fitsLine(final Stroke stroke) {
	final RegressionLine rl = ls.regressionLine(stroke);
	return rl.coefficientOfDetermination() >= minCorrelation;

    }
}
