package com.ncl.sketch.agent.di;

/**
 * Default pattern recognizer parameters.
 */
public interface DefaultPatternRecognizerParameters {

    /**
     * Returns the maximum area ratio. The max ratio (feature area of this stroke to the candidate pattern /
     * candidate line pattern) above which the candidate pattern found by linear regression will be considered
     * unacceptable. A good value is <i>1.0</i>.
     * 
     * @return the maximum area ratio
     */
    double getMaxAreaRatio();

    /**
     * Returns the minimum correlation. Minimum correlation is a {@code double} in range <i>0.0</i> to <i>1.0</i>
     * used to assess the fit of the computed regression line. The higher the value the more restrictive the fit
     * will be. A value of <i>0.7</i> is a good comprise to ensure correct recognition.
     * 
     * @return the minimum correlation
     */
    double getMinCorrelation();

    /**
     * Sets the maximum area ratio. The max ratio (feature area of this stroke to the candidate pattern / candidate
     * pattern area) above which the candidate pattern found by linear regression will be considered unacceptable.
     * A good value is <i>1.0</i>.
     * 
     * @param maxAreaRatio the maximum area ratio
     * @return the {@link DefaultPatternRecognizerParameters}
     */
    DefaultPatternRecognizerParameters setMaxAreaRatio(final double maxAreaRatio);

    /**
     * Sets the minimum correlation. Minimum correlation is a {@code double} in range <i>0.0</i> to <i>1.0</i> used
     * to assess the fit of the computed regression line. The higher the value the more restrictive the fit will
     * be. A value of <i>0.7</i> is a good comprise to ensure correct recognition.
     * 
     * @param minCorrelation the minimum correlation
     * @return the {@link DefaultPatternRecognizerParameters}
     */
    DefaultPatternRecognizerParameters setMinCorrelation(final double minCorrelation);

}
