package com.ncl.sketch.agent.di.api;

/**
 * The Circle Pattern Recognizer parameters.
 *
 */
public interface CircleRecognitionParameters extends LineRecognitionParameters {

    /**
     * Default circle recognition minimum correlation parameter value.
     */
    public static final double DEFAULT_MIN_CORRELATION = 0.95;

    /**
     * Default circle recognition maximum area error parameter value.
     */
    public static final double DEFAULT_MAX_AREA_ERROR = 0.1;

    /**
     * Default circle recognition maximum slope error parameter value.
     */
    public static final double DEFAULT_MAX_SLOPE_ERROR = 0.2;

    /**
     * Returns the maximum error between the feature area of this stroke to the
     * candidate circle center and the candidate circle area above which the
     * candidate circle will be considered unacceptable. This is a
     * {@code double} in range <i>0.0</i> to <i>1.0</i> with a low value
     * (typically <i>0.1</i>)
     */
    @Override
    double maxAreaError();

    /**
     * Returns the minimum correlation. Minimum correlation is a {@code double}
     * in range <i>0.0</i> to <i>1.0</i> used to assess the fit of the computed
     * regression line derived from the direction graph of the stroke. The
     * higher the value the more restrictive the fit will be
     *
     * @return the minimum correlation
     */
    @Override
    double minCorrelation();

    /**
     * Sets the maximum error between the feature area of this stroke to the
     * candidate circle center and the candidate circle area above which the
     * candidate circle will be considered unacceptable. This is a
     * {@code double} in range <i>0.0</i> to <i>1.0</i> with a low value
     * (typically <i>0.1</i>)
     *
     * @param maxAreaError
     *            the maximum area error
     * @return the {@link CircleRecognitionParameters}
     */
    @Override
    CircleRecognitionParameters maxAreaError(final double maxAreaError);

    /**
     * Sets the minimum correlation. Minimum correlation is Minimum correlation
     * is a {@code double} in range <i>0.0</i> to <i>1.0</i> used to assess the
     * fit of the computed regression line derived from the direction graph of
     * the stroke. The higher the value the more restrictive the fit will be
     *
     * @param minCorrelation
     *            the minimum correlation
     * @return the {@link CircleRecognitionParameters}
     */
    @Override
    CircleRecognitionParameters minCorrelation(final double minCorrelation);

    double maxSlopeError();

    CircleRecognitionParameters maxSlopeError(final double maxSlopeError);
}
