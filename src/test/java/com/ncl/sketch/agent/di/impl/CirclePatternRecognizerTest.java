package com.ncl.sketch.agent.di.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Stroke;
import com.ncl.sketch.agent.di.impl.CirclePatternRecognizer;
import com.ncl.sketch.agent.di.impl.StrokeRecognitionResult;

public final class CirclePatternRecognizerTest {

    private static final double DELTA = 0.00001;

    @Test
    public final void doNotRecognizeLine() {
        final double[] x = { 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72 };
        final double[] y = { 115, 117, 120, 123, 126, 129, 132, 135, 139, 142, 146, 150, 154, 159, 164 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final CirclePatternRecognizer recognizer = new CirclePatternRecognizer(0.95, 0.1, 0.15);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertFalse(recognizer.recognize(stroke, result));
    }

    @Test
    public final void doNotRecognizeRectangle() {
        final int height = 50;
        final int width = 10;
        final double[] x = new double[2 * (height + width)];
        final double[] y = new double[2 * (height + width)];

        int index = 0;
        for (int i = 0; i < height; i++) {
            x[index] = 0;
            y[index] = i;
            index++;
        }
        for (int i = 0; i < width; i++) {
            x[index] = i;
            y[index] = height;
            index++;
        }
        for (int i = 0; i < height; i++) {
            x[index] = width;
            y[index] = height - i;
            index++;
        }
        for (int i = 0; i < width; i++) {
            x[index] = width - i;
            y[index] = 0;
            index++;
        }

        final Stroke stroke = GeometricElements.stroke(x, y);
        final CirclePatternRecognizer recognizer = new CirclePatternRecognizer(0.95, 0.1, 0.15);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertFalse(recognizer.recognize(stroke, result));
    }

    @Test
    public final void doNotRecognizeSquare() {
        final int height = 50;
        final int width = 50;
        final double[] x = new double[2 * (height + width)];
        final double[] y = new double[2 * (height + width)];

        int index = 0;
        for (int i = 0; i < height; i++) {
            x[index] = 0;
            y[index] = i;
            index++;
        }
        for (int i = 0; i < width; i++) {
            x[index] = i;
            y[index] = height;
            index++;
        }
        for (int i = 0; i < height; i++) {
            x[index] = width;
            y[index] = height - i;
            index++;
        }
        for (int i = 0; i < width; i++) {
            x[index] = width - i;
            y[index] = 0;
            index++;
        }

        final Stroke stroke = GeometricElements.stroke(x, y);
        final CirclePatternRecognizer recognizer = new CirclePatternRecognizer(0.95, 0.1, 0.15);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertFalse(recognizer.recognize(stroke, result));
    }

    @Test
    public final void recognizeClockwiseCircle() {
        // FIXME : add noise and shift center to any point but (0, 0).
        final int size = 36;
        final double[] x = new double[size];
        final double[] y = new double[size];
        for (int index = 0; index < size; index++) {
            final double angle = Math.toRadians(10 * (size - index - 1));
            x[index] = Math.cos(angle);
            y[index] = Math.sin(angle);
        }
        final Stroke stroke = GeometricElements.stroke(x, y);
        final CirclePatternRecognizer recognizer = new CirclePatternRecognizer(0.9, 0.1, 0.15);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertTrue(recognizer.recognize(stroke, result));
        final List<Circle> circles = result.circles();
        assertEquals(1, circles.size());
        final Circle circle = circles.get(0);
        assertEquals(1.0, circle.radius(), DELTA);
        assertEquals(0.0, circle.center().x(), DELTA);
        assertEquals(0.0, circle.center().y(), DELTA);
    }

    @Test
    public final void recognizeCounterClockwiseCircle() {
        // FIXME : add noise and shift center to any point but (0, 0).
        final int size = 36;
        final double[] x = new double[size];
        final double[] y = new double[size];
        for (int index = 0; index < size; index++) {
            final double angle = Math.toRadians(10 * index);
            x[index] = Math.cos(angle);
            y[index] = Math.sin(angle);
        }
        final Stroke stroke = GeometricElements.stroke(x, y);
        final CirclePatternRecognizer recognizer = new CirclePatternRecognizer(0.9, 0.1, 0.15);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertTrue(recognizer.recognize(stroke, result));
        final List<Circle> circles = result.circles();
        assertEquals(1, circles.size());
        final Circle circle = circles.get(0);
        assertEquals(1.0, circle.radius(), DELTA);
        assertEquals(0.0, circle.center().x(), DELTA);
        assertEquals(0.0, circle.center().y(), DELTA);
    }

}
