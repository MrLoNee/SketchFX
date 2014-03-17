package com.ncl.sketch.agent;

import java.util.List;

import javafx.geometry.Point2D;

public interface SketchRecognitionAgentServices {

	RecognitionResult recognize(final List<Point2D> sketchData);
	
}
