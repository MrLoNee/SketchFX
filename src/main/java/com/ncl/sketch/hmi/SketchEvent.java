package com.ncl.sketch.hmi;

import java.util.List;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polyline;

/**
 * Sketch Event raised by the Sketch Listener when a Sketch is finished.
 */
final class SketchEvent extends Event {

    /** The serial version uid. */
    private static final long serialVersionUID = 6298317566196151642L;

    /** The Sketch Event type. */
    private static final EventType<Event> EVENT_TYPE = new EventType<>(
	    "SketchEvent");

    /** The Sketch points list. */
    private final List<Point2D> sketchPoints;

    /** the Sketch displayed polyline in the JavaFX container. */
    private final Polyline sketchDrawing;

    /**
     * Constructor.
     *
     * @param sketchPointsVal
     *            The Sketch points list
     * @param sketchDrawingVal
     *            the Sketch displayed polyline
     */
    SketchEvent(final List<Point2D> sketchPointsVal,
	    final Polyline sketchDrawingVal) {
	super(EVENT_TYPE);
	sketchPoints = sketchPointsVal;
	sketchDrawing = sketchDrawingVal;
    }

    /**
     * Get the Sketch points list.
     *
     * @return the Sketch points list
     */
    final List<Point2D> getSketchPoints() {
	return sketchPoints;
    }

    /**
     * Get the Sketch displayed polyline.
     *
     * @return the Sketch displayed polyline
     */
    final Polyline getSketchDrawing() {
	return sketchDrawing;
    }

}
