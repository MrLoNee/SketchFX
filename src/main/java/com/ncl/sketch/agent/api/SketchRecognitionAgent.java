package com.ncl.sketch.agent.api;

/**
 * An object that identifies geometric patterns in a sketch.
 */
public interface SketchRecognitionAgent {

    RecognitionResult recognize(final Stroke stroke);

}
