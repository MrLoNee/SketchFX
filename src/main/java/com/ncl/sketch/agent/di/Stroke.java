package com.ncl.sketch.agent.di;

/**
 * A part of a sketch used as an input to a recognition agent. A stroke is defined by a list of
 * {@link Point point}s.
 */
public interface Stroke {

    /**
     * Returns the {@link Point point} at the specified index in this stroke.
     * 
     * @param index index of the {@link Point point} to be returned
     * @return the {@link Point point} at the specified index in this stroke
     */
    Point get(final int index);

    /**
     * Returns the number of {@link Point point}s in this stroke.
     * 
     * @return the number of {@link Point point}s in this stroke
     */
    int size();

    /**
     * Returns the width of this stroke.
     * 
     * @return the width of this stroke
     */
    double width();

}
