package com.ncl.sketch.hmi;

import java.util.List;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;

public class SketchEvent extends Event {

	private static final long serialVersionUID = 6298317566196151642L;

	private List<Point2D> sketchData;

	public SketchEvent(final List<Point2D> sketchData) {
		super(new EventType<>("SketchEvent"));
		this.sketchData = sketchData;
	}

	public List<Point2D> getSketchData() {
		return sketchData;
	}

}
