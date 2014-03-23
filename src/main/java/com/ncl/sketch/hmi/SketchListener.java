package com.ncl.sketch.hmi;

import java.util.ArrayList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polyline;

final class SketchListener implements EventHandler<MouseEvent> {

    private final List<Point2D> sketch;
    
    private Polyline sketchDrawing;

    private final List<EventHandler<SketchEvent>> listeners;
    
    private final Group container;

    SketchListener(final Scene scene, final Group aContainer) {
        sketch = new ArrayList<>();
        listeners = new ArrayList<>();

        scene.setOnMousePressed(this);
        scene.setOnMouseDragged(this);
        scene.setOnMouseReleased(this);
        container = aContainer;
    }

    @Override
    public final void handle(final MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            // initialize the sketch
            sketchDrawing = new Polyline();
            container.getChildren().add(sketchDrawing);
            final Point2D point = new Point2D(event.getX(), event.getY());
            sketch.add(point);
            sketchDrawing.getPoints().addAll(event.getX(), event.getY());
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            // update the sketch
            final Point2D point = new Point2D(event.getX(), event.getY());
            sketch.add(point);
            sketchDrawing.getPoints().addAll(event.getX(), event.getY());
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            // finalize the sketch and throw a sketch event
            final Point2D point = new Point2D(event.getX(), event.getY());
            sketch.add(point);
            sketchDrawing.getPoints().addAll(event.getX(), event.getY());

            final List<Point2D> notificationList = new ArrayList<>(sketch);
            sketch.clear();

            final SketchEvent sketchEvent = new SketchEvent(notificationList);
            for (final EventHandler<SketchEvent> listener : listeners) {
                listener.handle(sketchEvent);
            }
        }
    }

    final void setOnSketchDone(final EventHandler<SketchEvent> handler) {
        listeners.add(handler);
    }

}
