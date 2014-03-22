package com.ncl.sketch.agent.di;

import com.ncl.sketch.agent.api.RecognitionResult;
import com.ncl.sketch.agent.api.SketchRecognitionAgent;
import com.ncl.sketch.agent.api.Stroke;

/**
 * A domain-independent {@link SketchRecognitionAgent}. This agent makes on no
 * assumption on the context in which the sketch was drawn.
 */
public final class DomainIndependentAgent implements SketchRecognitionAgent {

    private final PatternRecognizer lineRecognizer;

    /**
     * Constructor.
     */
    public DomainIndependentAgent() {
        lineRecognizer = new LinePatternRecognizer(1.0);
    }

    @Override
    public final RecognitionResult recognize(final Stroke stroke) {
        final StrokeRecognitionResult result = new StrokeRecognitionResult();
        lineRecognizer.recognize(stroke, result);
        return result;
    }

}
