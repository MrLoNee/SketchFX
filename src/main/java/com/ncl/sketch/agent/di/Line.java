package com.ncl.sketch.agent.di;

/**
 * A 2D line segment in the (x, y) coordinate system.
 */
public interface Line {

    /**
     * Returns the end {@link Point point} of this line.
     * 
     * @return the end {@link Point point} of this line
     */
    Point end();

    /**
     * Returns the start {@link Point point} of this line.
     * 
     * @return the start {@link Point point} of this line
     */
    Point start();

}
