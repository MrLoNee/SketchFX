package com.ncl.sketch.hmi;

import javafx.event.Event;
import javafx.event.EventType;

public class ParameterSliderEvent extends Event {
    /**
     *
     */
    private static final long serialVersionUID = -420434395517757301L;

    /** The Parameter slider Event type. */
    private static final EventType<Event> EVENT_TYPE = new EventType<>("ParameterSliderEvent");

    private final double newValue;

    /**
     * Constructor.
     *
     * @param sketchPointsVal The Sketch points list
     * @param sketchDrawingVal the Sketch displayed polyline
     */
    ParameterSliderEvent(final double newValue) {
        super(EVENT_TYPE);
        this.newValue = newValue;
    }

    double newValue() {
        return newValue;
    }
}
