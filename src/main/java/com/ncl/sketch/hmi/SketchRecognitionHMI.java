package com.ncl.sketch.hmi;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.RecognitionResult;
import com.ncl.sketch.agent.api.SketchRecognitionAgent;
import com.ncl.sketch.agent.api.Stroke;
import com.ncl.sketch.agent.di.DomainIndependentAgent;

public class SketchRecognitionHMI extends Application {

    private SketchRecognitionAgent sketchRecognitionAgent;

    private ScheduledExecutorService executor;

    @Override
    public void start(final Stage stage) throws Exception {
	sketchRecognitionAgent = new DomainIndependentAgent();
	executor = Executors.newSingleThreadScheduledExecutor();

	final Group container = new Group();
	final Scene scene = new Scene(container);

	final SketchListener sketchListener = new SketchListener(scene,
		container);
	sketchListener.setOnSketchDone(new EventHandler<SketchEvent>() {

	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void handle(final SketchEvent sketchEvent) {
		executor.execute(new Runnable() {
		    @Override
		    public void run() {

			final RecognitionResult recognitionResult = sketchRecognitionAgent
				.recognize(stroke(sketchEvent.getSketchPoints()));

			final Collection<Line> lines = recognitionResult
				.lines();
			final Collection<Circle> circles = recognitionResult
				.circles();
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
				sketchEvent.getSketchDrawing().setStroke(
					Color.LIGHTGRAY);

				for (final Line line : lines) {
				    final javafx.scene.shape.Line lineFx = new javafx.scene.shape.Line(
					    line.start().x(), line.start().y(),
					    line.end().x(), line.end().y());
				    lineFx.setStroke(Color.GREEN);
				    container.getChildren().add(lineFx);
				}

				for (final Circle circle : circles) {
				    final javafx.scene.shape.Circle circleFx = new javafx.scene.shape.Circle(
					    circle.center().x(), circle
						    .center().y(), circle
						    .radius());
				    circleFx.setFill(Color.TRANSPARENT);
				    circleFx.setStroke(Color.GREEN);
				    container.getChildren().add(circleFx);
				}

			    }
			});
		    }

		});
		sketchEvent.consume();
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

    private Stroke stroke(final List<Point2D> sketchPoints) {
	final Point[] result = new Point[sketchPoints.size()];
	int i = 0;
	for (final Point2D pt : sketchPoints) {
	    result[i] = new Point() {

		@Override
		public final double x() {
		    return pt.getX();
		}

		@Override
		public final double y() {
		    return pt.getY();
		}

		@Override
		public final String toString() {
		    return "[" + x() + ", " + y() + "]";
		}
	    };
	    i++;
	}

	return new Stroke(1.0, result);
    }

    public static void main(final String[] args) {
	Application.launch(args);
    }
}
