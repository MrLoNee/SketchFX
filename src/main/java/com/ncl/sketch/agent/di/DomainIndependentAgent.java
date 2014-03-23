package com.ncl.sketch.agent.di;

import java.util.logging.Logger;

import com.ncl.sketch.agent.api.RecognitionResult;
import com.ncl.sketch.agent.api.SketchRecognitionAgent;
import com.ncl.sketch.agent.api.Stroke;

/**
 * A domain-independent {@link SketchRecognitionAgent}. This agent makes no
 * assumption about the context in which the sketch was drawn.
 */
public final class DomainIndependentAgent implements SketchRecognitionAgent {

    private static final Logger LOGGER = Logger.getLogger("DI-Agent");

    private final PatternRecognizer lineRecognizer;
    
    private final int k;

    /**
     * Constructor.
     */
    public DomainIndependentAgent() {
        lineRecognizer = new LinePatternRecognizer(0.7, 1.0);
        k = 2;
    }

    @Override
    public final RecognitionResult postProcess() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final RecognitionResult recognize(final Stroke stroke) {
        final StrokeRecognitionResult result = new StrokeRecognitionResult();
        final boolean recognized = lineRecognizer.recognize(stroke, result);
        if (!recognized) {
            recognize(stroke, result);
        }
        return result;
    }

    private void recognize(final Stroke stroke, final StrokeRecognitionResult result) {
        final int strokeSize = stroke.size();
        LOGGER.info("Entering recursive recognition phase for stroke with " + strokeSize + " points");
        if (strokeSize > 1) {
            
            final int index = Strokes.indexOfMaxCurvature(stroke, k);
            if (index > 0 && index < strokeSize - 1) {
                
                LOGGER.info("Splitting index = " + index);
                
                /*
                 * split the stroke
                 */
                
                final Stroke first = stroke.subStroke(0, index + 1);
                final Stroke second = stroke.subStroke(index, strokeSize);
                
                /*
                 * Apply recognition to both sub strokes.
                 */
                
                final boolean firstRecognized = lineRecognizer.recognize(first, result);
                final boolean secondRecognized = lineRecognizer.recognize(second, result);
                
                /*
                 * Recurse if needed.
                 */
                
                if (!firstRecognized) {
                    recognize(first, result);
                }
                
                if (!secondRecognized) {
                    recognize(second, result);
                }
            }
        }
    }

}
