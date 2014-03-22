package com.ncl.sketch.agent.api;

import java.util.Arrays;
import java.util.List;

/**
 * A part of a sketch used as an input to a recognition agent. A stroke is
 * defined by a list of {@link Point point}s.
 */
public final class Stroke {

    private final double width;

    private final List<Point> points;

    /**
     * Constructor.
     * 
     * @param aWidth
     *            stroke width
     * @param somePoints
     *            points defining the stroke
     */
    public Stroke(final double aWidth, final Point... somePoints) {
        width = aWidth;
        points = Arrays.asList(somePoints);
    }

    /**
     * Returns the {@link Point point} at the specified index in this stroke.
     * 
     * @param index
     *            index of the {@link Point point} to be returned
     * @return the {@link Point point} at the specified index in this stroke
     */
    public final Point get(final int index) {
        return points.get(index);
    }

    /**
     * Returns the number of {@link Point point}s in this stroke.
     * 
     * @return the number of {@link Point point}s in this stroke
     */
    public final int size() {
        return points.size();
    }

    /**
     * Returns the width of this stroke.
     * 
     * @return the width of this stroke
     */
    public final double width() {
        return width;
    }

}
