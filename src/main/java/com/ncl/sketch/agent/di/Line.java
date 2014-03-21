package com.ncl.sketch.agent.di;

/**
 * A 2D line segment in the (x, y) coordinate system.
 */
public final class Line {

    private final Point start;

    private final Point end;

    /**
     * Constructor.
     * 
     * @param aStart start {@link Point point} of this line
     * @param anEnd end {@link Point point} of this line
     */
    public Line(final Point aStart, final Point anEnd) {
        start = aStart;
        end = anEnd;
    }

    /**
     * Returns the end {@link Point point} of this line.
     * 
     * @return the end {@link Point point} of this line
     */
    public final Point end() {
        return end;
    }

    /**
     * Returns the start {@link Point point} of this line.
     * 
     * @return the start {@link Point point} of this line
     */
    public final Point start() {
        return start;
    }

}
