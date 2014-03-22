package com.ncl.sketch.agent.di;

import java.util.ArrayList;
import java.util.List;

import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.RecognitionResult;

/**
 * {@link RecognitionResult Result} of the recognition process applied to a
 * stroke.
 */
final class StrokeRecognitionResult implements RecognitionResult {

    private final List<Line> lines;

    /**
     * Constructor.
     */
    StrokeRecognitionResult() {
        lines = new ArrayList<>();
    }

    @Override
    public final List<Line> lines() {
        return lines;
    }

    /**
     * Adds the specified {@link Line line} to the list of recognized lines.
     * 
     * @param line
     *            the line to add
     * @return this {@link StrokeRecognitionResult} for chainable calls
     */
    final StrokeRecognitionResult add(final Line line) {
        lines.add(line);
        return this;
    }

}
