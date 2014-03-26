package com.ncl.sketch.agent.di;

/**
 * Default pattern recognizer parameters.
 */
public interface DefaultPatternRecognizerParameters {

    /**
     * Set the minimum correlation. Minimum correlation is a {@code double} in
     * range <i>0.0</i> to <i>1.0</i> used to assess the fit of the computed
     * regression line. The higher the value the more restrictive the fit will
     * be. A value of <i>0.7</i> is a good comprise to ensure correct
     * recognition.
     *
     * @return the {@link DefaultPatternRecognizerParameters}
     */
    public DefaultPatternRecognizerParameters setMinCorrelation(
	    final double minCorrelation);

    /**
     * Set the maximum area ratio. The max ratio (feature area of this stroke to
     * the candidate pattern / candidate pattern area) above which the candidate
     * pattern found by linear regression will be considered unacceptable. A
     * good value is <i>1.0</i>.
     *
     * @return the {@link DefaultPatternRecognizerParameters}
     */
    public DefaultPatternRecognizerParameters setMaxAreaRatio(
	    final double maxAreaRatio);

    /**
     * Get the minimum correlation. Minimum correlation is a {@code double} in
     * range <i>0.0</i> to <i>1.0</i> used to assess the fit of the computed
     * regression line. The higher the value the more restrictive the fit will
     * be. A value of <i>0.7</i> is a good comprise to ensure correct
     * recognition.
     */
    public double getMinCorrelation();

    /**
     * Get the maximum area ratio. The max ratio (feature area of this stroke to
     * the candidate pattern / candidate line pattern) above which the candidate
     * pattern found by linear regression will be considered unacceptable. A
     * good value is <i>1.0</i>.
     */
    public double getMaxAreaRatio();

}
