package com.ncl.sketch.agent.di;

/**
 * A line defined as <i>y = a + b * x</i>. The parameters <i>a</i> and <i>b</i> parameters are the
 * result of a regression analysis.
 */
final class RegressionLine {

    private final double yIntercept;

    private final double slope;

    private final double r2;

    /**
     * Constructor.
     * 
     * @param aYIntercept the y-intercept of this line
     * @param aSlope the slope of this line
     * @param coefficientOfDetermination the coefficient of determination of the regression line.
     *            This is a {@code double} in the range <i>0</i> to <i>1</i>
     */
    RegressionLine(final double aYIntercept, final double aSlope, final double coefficientOfDetermination) {
        yIntercept = aYIntercept;
        slope = aSlope;
        r2 = coefficientOfDetermination;
    }

    @Override
    public final String toString() {
        return "RegressionLine [yIntercept=" + yIntercept + ", slope=" + slope + ", r2=" + r2 + "]";
    }

    /**
     * Returns the coefficient of determination of the regression line. This is a {@code double} in
     * the range <i>0</i> to <i>1</i>. The closer it is to <i>1</i> the best this line fits the
     * actual data.
     * 
     * @return the coefficient of determination of the regression line. This is a {@code double} in
     *         the range <i>0</i> to <i>1</i>
     */
    final double coefficientOfDetermination() {
        return r2;
    }

    /**
     * Returns the slope of this line - this is the <i>b</i> parameter.
     * 
     * @return the slope of this line - this is the <i>b</i> parameter
     */
    final double slope() {
        return slope;
    }

    /**
     * Returns the y-intercept of this line - this is the <i>a</i> parameter.
     * 
     * @return the y-intercept of this line - this is the <i>a</i> parameter
     */
    final double yIntercept() {
        return yIntercept;
    }
}
