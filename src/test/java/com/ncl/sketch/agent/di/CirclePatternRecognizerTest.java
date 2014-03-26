package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Stroke;

public final class CirclePatternRecognizerTest {

    private static final double DELTA = 0.00001;

    @Test
    public final void recognizeAntiClockwiseCircle() {
        final double[] x = { 101, 102, 103, 103, 102, 101, 100, 100 };
        final double[] y = { 100, 100, 101, 102, 103, 103, 102, 101 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final CirclePatternRecognizer recognizer = new CirclePatternRecognizer(0.9, 1.0, 0.15);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertTrue(recognizer.recognize(stroke, result));
        final List<Circle> circles = result.circles();
        assertEquals(1, circles.size());
        final Circle circle = circles.get(0);
        assertEquals(1.5811388300841895, circle.radius(), DELTA);
        assertEquals(101.5, circle.center().x(), DELTA);
        assertEquals(101.5, circle.center().y(), DELTA);
    }

    @Test
    public final void recognizeClockwiseCircle() {
        final double[] x = { 100, 100, 101, 102, 103, 103, 102, 101 };
        final double[] y = { 101, 102, 103, 103, 102, 101, 100, 100 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final CirclePatternRecognizer recognizer = new CirclePatternRecognizer(0.9, 1.0, 0.15);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertTrue(recognizer.recognize(stroke, result));
        final List<Circle> circles = result.circles();
        assertEquals(1, circles.size());
        final Circle circle = circles.get(0);
        assertEquals(1.5811388300841895, circle.radius(), DELTA);
        assertEquals(101.5, circle.center().x(), DELTA);
        assertEquals(101.5, circle.center().y(), DELTA);
    }

}
