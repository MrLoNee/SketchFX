package com.ncl.sketch.agent.di;

/**
 * The Circle Pattern Recognizer parameters.
 * 
 */
public interface CirclePatternRecognizerParameters extends DefaultPatternRecognizerParameters {

    CirclePatternRecognizerParameters maxSlopeError(final double maxSlopeError);

    double maxSlopeError();
}
