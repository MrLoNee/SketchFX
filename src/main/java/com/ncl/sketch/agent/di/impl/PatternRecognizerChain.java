package com.ncl.sketch.agent.di.impl;

import java.util.ArrayList;
import java.util.List;

import com.ncl.sketch.agent.api.Stroke;

/**
 * A chain of {@link PatternRecognizer}. Patterns are executed in the order they are
 * {@link #add(PatternRecognizer) added} to this chain. The execution stops as soon as one pattern has been
 * recognized.
 */
final class PatternRecognizerChain implements PatternRecognizer {

    private final List<PatternRecognizer> chain;

    /**
     * Constructor.
     */
    PatternRecognizerChain() {
        chain = new ArrayList<PatternRecognizer>();
    }

    @Override
    public final boolean recognize(final Stroke stroke, final StrokeRecognitionResult result) {
        boolean recognized = false;
        for (final PatternRecognizer recognizer : chain) {
            recognized = recognizer.recognize(stroke, result);
            if (recognized) {
                break;
            }
        }
        return recognized;
    }

    /**
     * Adds the specified {@link PatternRecognizer} at the end of this chain
     * 
     * @param recognizer the {@link PatternRecognizer} to add
     * @return this {@link PatternRecognizerChain} for chainable class
     */
    final PatternRecognizerChain add(final PatternRecognizer recognizer) {
        chain.add(recognizer);
        return this;
    }

}
