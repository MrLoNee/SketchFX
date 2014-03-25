package com.ncl.sketch.agent.api;

import java.util.Collection;

/**
 * Result of the sketch recognition process.
 */
public interface RecognitionResult {

    /**
     * Returns the {@link Circle circle}s recognized from the sketch.
     * 
     * @return the {@link Circle circle}s recognized from the sketch
     */
    Collection<Circle> circles();

    /**
     * Returns the {@link Line line}s recognized from the sketch.
     * 
     * @return the {@link Line line}s recognized from the sketch
     */
    Collection<Line> lines();

}
