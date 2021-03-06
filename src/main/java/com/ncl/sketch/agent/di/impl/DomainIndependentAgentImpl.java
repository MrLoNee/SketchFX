package com.ncl.sketch.agent.di.impl;

import java.util.logging.Logger;

import com.ncl.sketch.agent.api.RecognitionResult;
import com.ncl.sketch.agent.api.SketchRecognitionAgent;
import com.ncl.sketch.agent.api.Stroke;
import com.ncl.sketch.agent.di.api.CircleRecognitionParameters;
import com.ncl.sketch.agent.di.api.DomainIndependentAgent;
import com.ncl.sketch.agent.di.api.LineRecognitionParameters;

/**
 * A domain-independent {@link SketchRecognitionAgent}. This agent makes no assumption about the context in which
 * the sketch was drawn.
 * 
 * @see <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.89.3800&amp;rep=rep1&amp;type=pdf">A
 *      Domain-Independent System for Sketch Recognition</a>
 */
public final class DomainIndependentAgentImpl implements DomainIndependentAgent {

    private static final Logger LOGGER = Logger.getLogger("DI-Agent");

    private final PatternRecognizerChain recgonizers;

    private int k;

    private final LinePatternRecognizer lineRecognizer;

    private final CirclePatternRecognizer circleRecognizer;

    /**
     * Constructor.
     */
    public DomainIndependentAgentImpl() {
        recgonizers = new PatternRecognizerChain();
        lineRecognizer =
                new LinePatternRecognizer(LineRecognitionParameters.DEFAULT_MIN_CORRELATION,
                                          LineRecognitionParameters.DEFAULT_MAX_AREA_RATIO);
        circleRecognizer =
                new CirclePatternRecognizer(CircleRecognitionParameters.DEFAULT_MIN_CORRELATION,
                                            CircleRecognitionParameters.DEFAULT_MAX_AREA_ERROR,
                                            CircleRecognitionParameters.DEFAULT_MAX_SLOPE_ERROR);
        recgonizers.add(lineRecognizer).add(circleRecognizer);
        k = 2;
    }

    @Override
    public final CircleRecognitionParameters circleRecognitionParameters() {
        return circleRecognizer;
    }

    @Override
    public final int k() {
        return k;
    }

    @Override
    public final void k(final int kVal) {
        k = kVal;
    }

    @Override
    public final LineRecognitionParameters lineRecognitionParameters() {
        return lineRecognizer;
    }

    @Override
    public final RecognitionResult postProcess() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final RecognitionResult recognize(final Stroke stroke) {
        final StrokeRecognitionResult result = new StrokeRecognitionResult();
        recognize(stroke, result);
        return result;
    }

    private void recognize(final Stroke stroke, final StrokeRecognitionResult result) {
        final boolean recognized = recgonizers.recognize(stroke, result);
        if (!recognized) {

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
                    recognize(first, result);
                    recognize(second, result);
                }
            }
        }
    }

}
