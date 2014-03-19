package com.ncl.sketch.hmi;

import java.util.List;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;

public class SketchEvent extends Event {

    private static final long serialVersionUID = 6298317566196151642L;

    private final List<Point2D> sketch;

    public SketchEvent(final List<Point2D> sketchData) {
        super(new EventType<>("SketchEvent"));
        sketch = sketchData;
    }

    public final List<Point2D> getSketchData() {
        return sketch;
    }

}
