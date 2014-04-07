package com.ncl.sketch.agent.di.impl;

import com.ncl.sketch.agent.api.Stroke;

/**
 * An object that recognizes geometric patterns in {@link Stroke stroke}.
 */
interface PatternRecognizer {

    /**
     * Tries and recognizes geometric patterns in the specified {@link Stroke stroke}. The recognized patterns are
     * added to the specified {@link StrokeRecognitionResult result}.
     * 
     * @param stroke the stroke on which to perform the recognition process
     * @param result the {@link StrokeRecognitionResult result} to which the recognized patterns are added
     * @return <code>true</code> iff at least on pattern has been recognized
     */
    boolean recognize(final Stroke stroke, final StrokeRecognitionResult result);
}
