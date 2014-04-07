package com.ncl.sketch.agent.di.impl;

import java.util.ArrayList;
import java.util.List;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.RecognitionResult;

/**
 * {@link RecognitionResult Result} of the recognition process applied to a stroke.
 */
final class StrokeRecognitionResult implements RecognitionResult {

    private final List<Line> lines;

    private final List<Circle> circles;

    /**
     * Constructor.
     */
    StrokeRecognitionResult() {
        lines = new ArrayList<Line>();
        circles = new ArrayList<Circle>();
    }

    @Override
    public final List<Circle> circles() {
        return circles;
    }

    @Override
    public final List<Line> lines() {
        return lines;
    }

    /**
     * Adds the specified {@link Circle circle} to the list of recognized circles.
     * 
     * @param circle the circle to add
     * @return this {@link StrokeRecognitionResult} for chainable calls
     */
    final StrokeRecognitionResult add(final Circle circle) {
        circles.add(circle);
        return this;
    }

    /**
     * Adds the specified {@link Line line} to the list of recognized lines.
     * 
     * @param line the line to add
     * @return this {@link StrokeRecognitionResult} for chainable calls
     */
    final StrokeRecognitionResult add(final Line line) {
        lines.add(line);
        return this;
    }

}
