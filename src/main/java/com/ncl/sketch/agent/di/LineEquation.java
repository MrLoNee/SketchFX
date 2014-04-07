package com.ncl.sketch.agent.di;

/**
 * Parametric equation of a 2D line: <i>y = m * x + b</i>, where <i>m</i> is the slope and <i>b</i> the
 * y-intercept. This equation fits all but vertical lines. For vertical lines the parametric equation becomes <i>x
 * = a</i> where a is the x-intercept.
 */
final class LineEquation {

    private final double a;

    private final double b;

    private final double m;

    /**
     * Constructor.
     * 
     * @param xIntercept x-intercept
     * @param yIntercept y-intercept
     * @param slope slope
     */
    private LineEquation(final double xIntercept, final double yIntercept, final double slope) {
        a = xIntercept;
        b = yIntercept;
        m = slope;
    }

    @Override
    public final String toString() {
        final String result;
        if (isVertical()) {
            result = "x = " + xIntercept();
        } else if (slope() == 0.0) {
            result = "y = " + yIntercept();
        } else {
            result = "y = " + slope() + " * x + " + yIntercept();
        }
        return result;
    }

    /**
     * Returns <code>true</code> if the line described by this equation is vertical - in which case the equation is
     * <i>x = a</i> and only the {@link #xIntercept()} should be used.
     * 
     * @return <code>true</code> if the line described by this equation is vertical
     */
    final boolean isVertical() {
        return Double.isInfinite(m);
    }

    /**
     * Returns the slope of the line described by this equation.
     * 
     * @return the slope of the line described by this equation
     */
    final double slope() {
        return m;
    }

    /**
     * Returns the x-intercept of the line described by this equation.
     * 
     * @return the x-intercept of the line described by this equation
     */
    final double xIntercept() {
        return a;
    }

    /**
     * Returns the y-intercept of the line described by this equation.
     * 
     * @return the y-intercept of the line described by this equation
     */
    final double yIntercept() {
        return b;
    }

    /**
     * Returns a new {@link LineEquation parametric equation} that fits an horizontal line with the specified
     * y-intercept.
     * 
     * @param yIntercept the y-intercept of the line
     * @return a new {@link LineEquation parametric equation} that fits an horizontal line with the specified slope
     *         and y-intercept
     */
    static final LineEquation horizontalLine(final double yIntercept) {
        return new LineEquation(Double.POSITIVE_INFINITY, yIntercept, 0);
    }

    /**
     * Returns a new {@link LineEquation parametric equation} that fits a line with the specified slope and
     * y-intercept.
     * 
     * @param slope the slope of the line
     * @param yIntercept the y-intercept of the line
     * @return a new {@link LineEquation parametric equation} that fits a line with the specified slope and
     *         y-intercept
     */
    static final LineEquation line(final double slope, final double yIntercept) {
        /*
         * x-intercept is the value of x when y = 0
         * 
         * m * x + b = 0 -> x = -b / m
         */
        final double xIntercept = -yIntercept / slope;
        return new LineEquation(xIntercept, yIntercept, slope);
    }

    /**
     * Returns a new {@link LineEquation parametric equation} that fits a vertical line with the specified slope
     * and y-intercept.
     * 
     * @param xIntercept the x-intercept of the line
     * @return a new {@link LineEquation parametric equation} that fits a vertical line with the specified slope
     *         and y-intercept
     */
    static final LineEquation verticalLine(final double xIntercept) {
        return new LineEquation(xIntercept, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

}
