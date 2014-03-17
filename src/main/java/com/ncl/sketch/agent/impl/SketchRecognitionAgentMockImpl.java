package com.ncl.sketch.agent.impl;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import com.ncl.sketch.agent.RecognitionResult;
import com.ncl.sketch.agent.SketchRecognitionAgent;

public class SketchRecognitionAgentMockImpl implements SketchRecognitionAgent {


	@Override
	public RecognitionResult recognize(final List<Point2D> sketchData) {
		double maxX = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;

		for (Point2D point : sketchData) {
			maxX = Math.max(point.getX(), maxX);
			minX = Math.min(point.getX(), minX);
			maxY = Math.max(point.getY(), maxY);
			minY = Math.min(point.getY(), minY);

		}
		Rectangle boundingBox = new Rectangle(minX, minY, maxX - minX, maxY - minY);
		boundingBox.setFill(null);
		boundingBox.setStroke(Color.DARKGRAY);

		return new RecognitionResultImpl(boundingBox);
	}

}
