package com.ncl.sketch.agent.di;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

public final class StrokesTest {

    private static final double DELTA = 0.00001;

    private static Stroke stroke() {
        final double[] x = { 0, 0, 2.5, 5, 5 };
        final double[] y = { 0, 5, 7.5, 5, 0 };
        return GeometricElements.stroke(x, y);
    }

    @Test
    public final void directionGraph() {
        final Stroke stroke = stroke();
        final double[] directionGraph = Strokes.directionGraph(stroke);
        assertEquals(4, directionGraph.length);
        assertEquals(Math.PI / 2, directionGraph[0], DELTA);
        assertEquals(0.785398163, directionGraph[1], DELTA);
        assertEquals(-0.785398163, directionGraph[2], DELTA);
        assertEquals(-Math.PI / 2, directionGraph[3], DELTA);
    }

    @Test
    public final void directionGraphClockwiseCircle() {
        final double[] x = { 100, 100, 101, 102, 103, 103, 102, 101 };
        final double[] y = { 101, 102, 103, 103, 102, 101, 100, 100 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final double[] directionGraph = Strokes.directionGraph(stroke);
        assertEquals(7, directionGraph.length);
        assertEquals(1.5707963267948966, directionGraph[0], DELTA);
        assertEquals(0.7853981633974483, directionGraph[1], DELTA);
        assertEquals(0.0, directionGraph[2], DELTA);
        assertEquals(-0.7853981633974483, directionGraph[3], DELTA);
        assertEquals(-1.5707963267948966, directionGraph[4], DELTA);
        assertEquals(-2.356194490192345, directionGraph[5], DELTA);
        assertEquals(-3.141592653589793, directionGraph[6], DELTA);
    }

    @Test
    public final void directionGraphCounterClockwiseCircle() {
        final double[] x = { 101, 102, 103, 103, 102, 101, 100, 100 };
        final double[] y = { 100, 100, 101, 102, 103, 103, 102, 101 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final double[] directionGraph = Strokes.directionGraph(stroke);
        assertEquals(7, directionGraph.length);
        assertEquals(0, directionGraph[0], DELTA);
        assertEquals(0.7853981633974483, directionGraph[1], DELTA);
        assertEquals(1.5707963267948966, directionGraph[2], DELTA);
        assertEquals(2.356194490192345, directionGraph[3], DELTA);
        assertEquals(3.141592653589793, directionGraph[4], DELTA);
        assertEquals(3.9269908169872414, directionGraph[5], DELTA);
        assertEquals(4.71238898038469, directionGraph[6], DELTA);
    }

    public final void directionGraphSquare() {
        final double[] x = { 100, 100, 100, 101, 102, 102, 102, 101 };
        final double[] y = { 100, 101, 102, 102, 102, 101, 100, 100 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        final double[] directionGraph = Strokes.directionGraph(stroke);
        assertEquals(7, directionGraph.length);
        assertEquals(1.5707963267948966, directionGraph[0], DELTA);
        assertEquals(1.5707963267948966, directionGraph[1], DELTA);
        assertEquals(0.0, directionGraph[2], DELTA);
        assertEquals(0.0, directionGraph[3], DELTA);
        assertEquals(-1.5707963267948966, directionGraph[4], DELTA);
        assertEquals(-1.5707963267948966, directionGraph[5], DELTA);
        assertEquals(-3.141592653589793, directionGraph[6], DELTA);
    }

    @Test
    public final void directionRectangle() {
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
        final double[] directionGraph = Strokes.directionGraph(stroke);

        assertEquals(119, directionGraph.length);
        index = 0;
        for (int i = 0; i < height; i++) {
            assertEquals(Math.PI / 2, directionGraph[index], DELTA);
            index++;
        }
        for (int i = 0; i < width; i++) {
            assertEquals(0.0, directionGraph[index], DELTA);
            index++;
        }
        for (int i = 0; i < height; i++) {
            assertEquals(-Math.PI / 2, directionGraph[index], DELTA);
            index++;
        }
        for (int i = 0; i < width - 1; i++) {
            assertEquals(-Math.PI, directionGraph[index], DELTA);
            index++;
        }
    }

    @Test
    public final void directionSquare() {
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
        final double[] directionGraph = Strokes.directionGraph(stroke);

        assertEquals(199, directionGraph.length);
        index = 0;
        for (int i = 0; i < height; i++) {
            assertEquals(Math.PI / 2, directionGraph[index], DELTA);
            index++;
        }
        for (int i = 0; i < width; i++) {
            assertEquals(0.0, directionGraph[index], DELTA);
            index++;
        }
        for (int i = 0; i < height; i++) {
            assertEquals(-Math.PI / 2, directionGraph[index], DELTA);
            index++;
        }
        for (int i = 0; i < width - 1; i++) {
            assertEquals(-Math.PI, directionGraph[index], DELTA);
            index++;
        }
    }

    @Test
    public final void featureAreaLine() {
        final Stroke stroke = stroke();
        final Point start = GeometricElements.point(0, 8);
        final Point end = GeometricElements.point(8, 10);
        final Line line = new Line(start, end);
        assertEquals(25.2205882, Strokes.featureArea(stroke, line), DELTA);
    }

    @Test
    public final void featureAreaPoint() {
        final Stroke stroke = stroke();
        final Point reference = GeometricElements.point(2.5, 2.5);
        assertEquals(25.0, Strokes.featureArea(stroke, reference), DELTA);
    }

    @Test
    public final void indexOfMaxCurvature() {
        final Stroke stroke = stroke();
        assertEquals(2, Strokes.indexOfMaxCurvature(stroke, 2));
    }

    @Test
    public final void indexOfMaxCurvature3Points() {
        final double[] x = { 0, 0, 2.5 };
        final double[] y = { 0, 5, 7.5 };
        final Stroke stroke = GeometricElements.stroke(x, y);
        assertEquals(1, Strokes.indexOfMaxCurvature(stroke, 2));
    }
}
