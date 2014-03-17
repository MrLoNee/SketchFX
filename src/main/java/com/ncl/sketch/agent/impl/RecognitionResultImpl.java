package com.ncl.sketch.agent.impl;

import javafx.scene.shape.Shape;

import com.ncl.sketch.agent.RecognitionResult;

public class RecognitionResultImpl implements RecognitionResult {

	private final Shape shape;

	RecognitionResultImpl(final Shape aShape) {
		shape = aShape;
	}

	@Override
	public Shape getShape() {
		return shape;
	}

}
