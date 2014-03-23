package com.ncl.sketch.agent.api;

/**
 * An object that identifies geometric patterns in a sketch.
 * <p>
 * Each {@link Stroke stroke} of the sketch shall be individually matched
 * against the patterns known to this agent by calling
 * {@link #recognize(Stroke)}. Once the sketch is completed the
 * {@link #postProcess() post-processing} phase may be called to further refine
 * the recognition process.
 */
public interface SketchRecognitionAgent {

    RecognitionResult postProcess();

    RecognitionResult recognize(final Stroke stroke);

}
