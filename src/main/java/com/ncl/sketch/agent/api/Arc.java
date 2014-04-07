package com.ncl.sketch.agent.api;

/**
 * A 2D arc in the (x, y) coordinate system. An arc is a partial section of a full circle.
 * <p>
 * An {@code Arc} defined by a center {@link Point point}, radius, start angle (in radians), and angular extent
 * (length of the arc in radians).
 */
public final class Arc {

    private final Point center;

    private final double radius;

    private final double startAngle;

    private final double length;

    /**
     * Constructor.
     * 
     * @param aCenter the center of the arc
     * @param aRadius the radius of the arc
     * @param aStartAngle the starting angle of the arc in <strong>radians</strong>
     * @param aLength the angular extent of the arc in <strong>radians</strong>
     */
    public Arc(final Point aCenter, final double aRadius, final double aStartAngle, final double aLength) {
        center = aCenter;
        radius = aRadius;
        startAngle = aStartAngle;
        length = aLength;
    }

    /**
     * Returns the center of the arc.
     * 
     * @return the center of the arc
     */
    public final Point center() {
        return center;
    }

    /**
     * Returns the angular extent of the arc in <strong>radians</strong>.
     * 
     * @return the angular extent of the arc in <strong>radians</strong>
     */
    public final double length() {
        return length;
    }

    /**
     * Returns the radius of the arc.
     * 
     * @return the radius of the arc
     */
    public final double radius() {
        return radius;
    }

    /**
     * Returns the starting angle of the arc in <strong>radians</strong>.
     * 
     * @return the starting angle of the arc in <strong>radians</strong>
     */
    public final double startAngle() {
        return startAngle;
    }
}
