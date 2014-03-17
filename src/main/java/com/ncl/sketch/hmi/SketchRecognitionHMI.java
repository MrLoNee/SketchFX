package com.ncl.sketch.hmi;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.ncl.sketch.agent.RecognitionResult;
import com.ncl.sketch.agent.SketchRecognitionAgent;
import com.ncl.sketch.agent.impl.SketchRecognitionAgentMockImpl;

public class SketchRecognitionHMI extends Application {

	private SketchRecognitionAgent sketchRecognitionAgent;

	private ScheduledExecutorService executor;

	@Override
	public void start(final Stage stage) throws Exception {
		sketchRecognitionAgent = new SketchRecognitionAgentMockImpl();
		executor = Executors.newSingleThreadScheduledExecutor();

		final Group container = new Group();
		final Scene scene = new Scene(container);

		final SketchListener sketchListener = new SketchListener(scene, container);
		sketchListener.setOnSketchDone(new EventHandler<SketchEvent>() {

			@Override
			public void handle(final SketchEvent sketchEvent) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						final RecognitionResult recognitionResult = sketchRecognitionAgent.recognize(sketchEvent.getSketchData());

						final Shape shape = recognitionResult.getShape();
						if (shape != null) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									container.getChildren().add(shape);
								}
							});
						}
					}
				});
			}
		});

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(final WindowEvent event) {
				System.exit(0);
			}
		});
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.setWidth(800);
		stage.setHeight(600);
		stage.toFront();
		stage.setTitle("NCL Sketch Recognition Alpha Prototype");
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
