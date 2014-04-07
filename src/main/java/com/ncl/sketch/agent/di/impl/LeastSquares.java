package com.ncl.sketch.agent.di.impl;

import java.util.logging.Logger;

import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

/**
 * Implementation of the least-squares method.
 * <p>
 * The method of least squares is a standard approach to the approximate solution of overdetermined systems, i.e.,
 * sets of equations in which there are more equations than unknowns. "Least squares" means that the overall
 * solution minimizes the sum of the squares of the errors made in the results of every single equation.
 * 
 * @see <a href="http://www.stat.purdue.edu/~xuanyaoh/stat350/xyApr6Lec26.pdf">lecture on least squares</a>
 * @see <a href="http://en.wikipedia.org/wiki/Least_squares">wikipedia: least squares</a>
 * @see <a href="http://en.wikipedia.org/wiki/Coefficient_of_determination">wikipedia: coefficient of
 *      determination</a>
 */
final class LeastSquares {

    private static final Logger LOGGER = Logger.getLogger("DI-Agent");

    /**
     * Constructor.
     */
    LeastSquares() {
        // empty.
    }

    RegressionLine regressionLine(final double[] x, final double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("x and y must contain the same number of samplings");
        }

        final int nbSamplings = x.length;

        /*
         * compute xbar and ybar.
         */

        double sumx = 0.0;
        double sumy = 0.0;
        for (int i = 0; i < nbSamplings; i++) {
            sumx += x[i];
            sumy += y[i];
        }

        final double xbar = sumx / nbSamplings;
        final double ybar = sumy / nbSamplings;

        /*
         * compute total sum of squares: sst
         */

        double sst = 0.0;
        for (int i = 0; i < nbSamplings; i++) {
            sst += (ybar - y[i]) * (ybar - y[i]);
        }

        final RegressionLine rl;
        if (sst == 0.0) {
            /*
             * Actual data fit an horizontal line no need to compute regression
             */
            rl = new RegressionLine(ybar, 0.0, 1.0);

        } else {

            /*
             * compute a & b (equation of regression line is y = a + bx).
             */
            double xxbar = 0.0;
            double xybar = 0.0;
            for (int i = 0; i < nbSamplings; i++) {
                xxbar += (x[i] - xbar) * (x[i] - xbar);
                xybar += (x[i] - xbar) * (y[i] - ybar);
            }

            if (xxbar == 0.0) {
                /*
                 * Actual data fit a vertical line no need to compute regression
                 */
                rl = new RegressionLine(0.0, 1.0, 1.0);

            } else {

                final double b = xybar / xxbar;
                final double a = ybar - b * xbar;

                /*
                 * compute error sum of squares: sse
                 */

                double sse = 0.0;
                for (int i = 0; i < nbSamplings; i++) {
                    final double fit = a + b * x[i];
                    sse += (fit - y[i]) * (fit - y[i]);
                }

                /*
                 * compute coefficient of determination: r2.
                 */

                final double r2 = 1 - sse / sst;

                rl = new RegressionLine(a, b, r2);
            }
        }

        LOGGER.fine(rl.toString());

        return rl;
    }

    RegressionLine regressionLine(final Stroke stroke) {
        final int strokeSize = stroke.size();
        final double[] x = new double[strokeSize];
        final double[] y = new double[strokeSize];

        for (int i = 0; i < strokeSize; i++) {
            final Point pt = stroke.get(i);
            x[i] = pt.x();
            y[i] = pt.y();
        }
        return regressionLine(x, y);
    }

}
