package com.ncl.sketch.agent.api;

import java.util.Arrays;
import java.util.List;

/**
 * A collection of contiguous {@link Point point}s.
 */
public final class Stroke {

    private final double width;

    private final List<Point> points;

    /**
     * Constructor.
     * 
     * @param aWidth stroke width
     * @param somePoints points defining the stroke
     */
    public Stroke(final double aWidth, final Point... somePoints) {
        this(aWidth, Arrays.asList(somePoints));
    }

    private Stroke(final double aWidth, final List<Point> somePoints) {
        width = aWidth;
        points = somePoints;
    }

    /**
     * Returns the {@link Point point} at the specified index in this stroke.
     * 
     * @param index index of the {@link Point point} to be returned
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
     * Returns a view of the portion of this stroke between the specified
     * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive. If
     * <tt>fromIndex</tt> and <tt>toIndex</tt> are equal, the returned stroke is
     * empty.
     * 
     * @see List#subList(int, int)
     * @param fromIndex
     * @param toIndex
     * @return
     */
    public final Stroke subStroke(final int fromIndex, final int toIndex) {
        final List<Point> sub = points.subList(fromIndex, toIndex);
        return new Stroke(width, sub);
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
