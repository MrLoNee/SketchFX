package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

public final class LinePatternRecognizerTest {

    @Test
    public final void recognizesActualLine() {
        final LinePatternRecognizer recognizer = new LinePatternRecognizer(1.0);

        final double[] x = { 0, 1, 2, 3, 4 };
        final double[] y = { 0, 1, 2, 3, 4 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final Point first = stroke.get(0);
        final Point last = stroke.get(4);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertTrue(recognizer.recognize(stroke, result));
        assertEquals(1, result.lines().size());
        final Line line = result.lines().get(0);
        assertEquals(first, line.start());
        assertEquals(last, line.end());
    }

    @Test
    public final void recognizes() {
        final LinePatternRecognizer recognizer = new LinePatternRecognizer(1.0);
        final double[] x = { 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72 };
        final double[] y = { 115, 117, 120, 123, 126, 129, 132, 135, 139, 142, 146, 150, 154, 159, 164 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final Point first = stroke.get(0);
        final Point last = stroke.get(14);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertTrue(recognizer.recognize(stroke, result));
        assertEquals(1, result.lines().size());
        final Line line = result.lines().get(0);
        assertEquals(first, line.start());
        assertEquals(last, line.end());
    }

    @Test
    public final void recognizesHorizontal() {
        final LinePatternRecognizer recognizer = new LinePatternRecognizer(1.0);

        final double[] x = { 0, 1, 2, 3, 4 };
        final double[] y = { 0, 0, 0, 0, 0 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final Point first = stroke.get(0);
        final Point last = stroke.get(4);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertTrue(recognizer.recognize(stroke, result));
        assertEquals(1, result.lines().size());
        final Line line = result.lines().get(0);
        assertEquals(first, line.start());
        assertEquals(last, line.end());
    }

    @Test
    public final void recognizesVertical() {
        final LinePatternRecognizer recognizer = new LinePatternRecognizer(1.0);

        final double[] x = { 0, 0, 0, 0, 0 };
        final double[] y = { 0, 1, 2, 3, 4 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final Point first = stroke.get(0);
        final Point last = stroke.get(4);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertTrue(recognizer.recognize(stroke, result));
        assertEquals(1, result.lines().size());
        final Line line = result.lines().get(0);
        assertEquals(first, line.start());
        assertEquals(last, line.end());
    }

    @Test
    public final void doNotRecognize() {
        final LinePatternRecognizer recognizer = new LinePatternRecognizer(1.0);

        final double[] x = { 0, 0, 2.5, 5.0, 5.0 };
        final double[] y = { 0, 5, 7.5, 5.0, 0 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final StrokeRecognitionResult result = new StrokeRecognitionResult();

        assertFalse(recognizer.recognize(stroke, result));
        assertEquals(0, result.lines().size());
    }

}
