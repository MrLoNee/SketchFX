package com.ncl.sketch.agent.di;

import com.ncl.sketch.agent.api.Stroke;

final class ArcPatternRecognizer implements PatternRecognizer {

    ArcPatternRecognizer() {
        // empty.
    }

    @Override
    public final boolean recognize(final Stroke stroke, final StrokeRecognitionResult result) {
        throw new UnsupportedOperationException();
    }

}
