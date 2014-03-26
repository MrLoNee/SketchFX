package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.RecognitionResult;
import com.ncl.sketch.agent.api.Stroke;

public final class DomainIndependentAgentTest {

    private static final double DELTA = 0.00001;

    @Test
    public final void recognizeClockwiseCircle() {
        final int size = 36;
        final double[] x = new double[size];
        final double[] y = new double[size];
        for (int index = 0; index < size; index++) {
            final double angle = Math.toRadians(10 * (size - index - 1));
            x[index] = Math.cos(angle);
            y[index] = Math.sin(angle);
        }
        final Stroke stroke = GeometricElements.stroke(x, y);

        final DomainIndependentAgent agent = new DomainIndependentAgent();
        final RecognitionResult result = agent.recognize(stroke);

        assertLinesEquals(result);
        final Circle expected = new Circle(GeometricElements.point(0, 0), 1);
        assertCirclesEquals(result, expected);
    }

    @Test
    public final void recognizeCounterClockwiseCircle() {
        final int size = 36;
        final double[] x = new double[size];
        final double[] y = new double[size];
        for (int index = 0; index < size; index++) {
            double angle = Math.toRadians(10 * index);
            x[index] = Math.cos(angle);
            y[index] = Math.sin(angle);
        }
        final Stroke stroke = GeometricElements.stroke(x, y);

        final DomainIndependentAgent agent = new DomainIndependentAgent();
        final RecognitionResult result = agent.recognize(stroke);

        assertLinesEquals(result);
        final Circle expected = new Circle(GeometricElements.point(0, 0), 1);
        assertCirclesEquals(result, expected);
    }

    @Test
    public final void recognizeOneLine() {
        final double[] x = { 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72 };
        final double[] y = { 115, 117, 120, 123, 126, 129, 132, 135, 139, 142, 146, 150, 154, 159, 164 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final Point first = stroke.get(0);
        final Point last = stroke.get(14);

        final DomainIndependentAgent agent = new DomainIndependentAgent();
        final RecognitionResult result = agent.recognize(stroke);
        assertLinesEquals(result, first, last);
        assertCirclesEquals(result);
    }

    @Test
    public final void recognizeThreeLines() {
        final double[] x = { 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72 };
        final double[] y = { 115, 117, 120, 123, 126, 129, 132, 135, 139, 142, 146, 150, 164, 169, 174 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final Point first1 = stroke.get(0);
        final Point last1 = stroke.get(2);
        final Point first2 = stroke.get(2);
        final Point last2 = stroke.get(9);
        final Point first3 = stroke.get(9);
        final Point last3 = stroke.get(14);

        final DomainIndependentAgent agent = new DomainIndependentAgent();
        final RecognitionResult result = agent.recognize(stroke);
        assertLinesEquals(result, first1, last1, first2, last2, first3, last3);
        assertCirclesEquals(result);
    }

    private void assertCirclesEquals(final RecognitionResult actual, final Circle... expecteds) {
        int i = 0;
        final int index = 0;
        final int expectedSize = expecteds.length;
        for (final Circle circle : actual.circles()) {
            i++;
            if (i > expectedSize) {
                fail("Too many actual circles.");
            }

            final Circle expected = expecteds[index];
            assertPointsEquals(expected.center(), circle.center());
            assertEquals(expected.radius(), circle.radius(), DELTA);
        }
    }

    private void assertLinesEquals(final RecognitionResult actual, final Point... expecteds) {
        int i = 0;
        int ptIndex = 0;
        final int expectedLines = expecteds.length / 2;
        for (final Line line : actual.lines()) {
            i++;
            if (i > expectedLines) {
                fail("Too many actual lines.");
            }
            assertEquals(expecteds[ptIndex], line.start());
            assertEquals(expecteds[ptIndex + 1], line.end());
            ptIndex += 2;
        }
        assertEquals(expectedLines, i);
    }

    private void assertPointsEquals(final Point expected, final Point actual) {
        assertEquals(expected.x(), actual.x(), DELTA);
        assertEquals(expected.y(), actual.y(), DELTA);
    }
}
