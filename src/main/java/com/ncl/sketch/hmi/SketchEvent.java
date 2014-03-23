package com.ncl.sketch.hmi;

import java.util.List;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;

import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.Stroke;

final class SketchEvent extends Event {

    private static final EventType<Event> EVENT_TYPE = new EventType<>("SketchEvent");

    private static final long serialVersionUID = 6298317566196151642L;

    private final Stroke stroke;

    SketchEvent(final List<Point2D> sketchData) {
        super(EVENT_TYPE);
        stroke = new Stroke(1.0, points(sketchData));
    }

    final Stroke getSketchData() {
        return stroke;
    }

    private static Point[] points(final List<Point2D> sketchData) {
        final Point[] result = new Point[sketchData.size()];
        int i = 0;
        for (final Point2D pt : sketchData) {
            result[i] = new Point() {

                @Override
                public final String toString() {
                    return "[" + x() + ", " + y() + "]";
                }

                @Override
                public final double x() {
                    return pt.getX();
                }

                @Override
                public final double y() {
                    return pt.getY();
                }
            };
            i++;
        }
        return result;
    }

}
