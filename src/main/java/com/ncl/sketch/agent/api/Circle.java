package com.ncl.sketch.agent.api;

/**
 * A 2D circle in the (x, y) coordinate system.
 */
public final class Circle {

    private final Point center;

    private final double radius;

    /**
     * Constructor.
     * 
     * @param aCenter the center of the circle
     * @param aRadius the radius of the circle
     */
    public Circle(final Point aCenter, final double aRadius) {
        center = aCenter;
        radius = aRadius;
    }

    /**
     * Returns the center of the circle.
     * 
     * @return the center of the circle
     */
    public final Point center() {
        return center;
    }

    /**
     * Returns the radius of the circle.
     * 
     * @return the radius of the circle
     */
    public final double radius() {
        return radius;
    }

    @Override
    public final String toString() {
        return "Circle [center=" + center + ", radius=" + radius + "]";
    }

}
