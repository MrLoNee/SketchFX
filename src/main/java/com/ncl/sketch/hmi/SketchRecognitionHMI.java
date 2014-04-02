package com.ncl.sketch.hmi;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.RecognitionResult;
import com.ncl.sketch.agent.api.Stroke;
import com.ncl.sketch.agent.di.DomainIndependentAgent;

public class SketchRecognitionHMI extends Application {

    private DomainIndependentAgent domainIndependentAgent;

    private ScheduledExecutorService executor;

    @Override
    public void start(final Stage stage) throws Exception {
	domainIndependentAgent = new DomainIndependentAgent();
	executor = Executors.newSingleThreadScheduledExecutor();

	final BorderPane borderPane = new BorderPane();
	final Pane container = new Pane();
	container.setPrefSize(500, 500);
	borderPane.setCenter(container);

	// if(System.getProperty("cheatMode",
	// "false").equalsIgnoreCase("true")){
	final Node parametersPanel = createParametersPanel();
	borderPane.setRight(parametersPanel);
	// }

	final Scene scene = new Scene(borderPane);

	final SketchListener sketchListener = new SketchListener(scene,
		container);
	sketchListener.onSketchDone(new EventHandler<SketchEvent>() {

	    @SuppressWarnings("synthetic-access")
	    @Override
	    public void handle(final SketchEvent sketchEvent) {
		executor.execute(new Runnable() {
		    @Override
		    public void run() {

			final RecognitionResult recognitionResult = domainIndependentAgent
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
				    lineFx.setStroke(Color.DARKBLUE);
				    container.getChildren().add(lineFx);
				}

				for (final Circle circle : circles) {
				    final javafx.scene.shape.Circle circleFx = new javafx.scene.shape.Circle(
					    circle.center().x(), circle
						    .center().y(), circle
						    .radius());
				    circleFx.setFill(Color.TRANSPARENT);
				    circleFx.setStroke(Color.DARKBLUE);
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

    private Node createParametersPanel() {
	final VBox box = new VBox();
	box.setPadding(new Insets(5, 0, 5, 0));
	box.setSpacing(10);
	box.setPadding(new Insets(0, 20, 10, 20));

	// k parameter
	final ParameterSlider kParamSlider = new ParameterSlider("Stroke k", 1, 10,
		domainIndependentAgent.k());
	kParamSlider
		.onParameterUpdate(new EventHandler<ParameterSliderEvent>() {
		    @Override
		    public void handle(final ParameterSliderEvent event) {
			domainIndependentAgent.k((int) (event.newValue()));
		    }
		});
	box.getChildren().add(kParamSlider);

	// line parameters
	final ParameterSlider lineMinCorrelationParamSlider = new ParameterSlider(
		"Line min correlation", 0.0, 100.0, domainIndependentAgent
			.lineRecognitionParameters().minCorrelation() * 100.0);
	lineMinCorrelationParamSlider
		.onParameterUpdate(new EventHandler<ParameterSliderEvent>() {
		    @Override
		    public void handle(final ParameterSliderEvent event) {
			domainIndependentAgent.lineRecognitionParameters()
				.minCorrelation(event.newValue());
		    }
		});
	box.getChildren().add(lineMinCorrelationParamSlider);

	final ParameterSlider lineMaxAreaRatioParamSlider = new ParameterSlider(
		"Line max area ratio", 0.0, 100.0, domainIndependentAgent
			.lineRecognitionParameters().maxAreaError() * 100.0);
	lineMinCorrelationParamSlider
		.onParameterUpdate(new EventHandler<ParameterSliderEvent>() {
		    @Override
		    public void handle(final ParameterSliderEvent event) {
			domainIndependentAgent.lineRecognitionParameters()
				.maxAreaError(event.newValue());
		    }
		});
	box.getChildren().add(lineMaxAreaRatioParamSlider);

	// circle parameters
	final ParameterSlider circleMinCorrelationParamSlider = new ParameterSlider(
		"Circle min correlation", 0.0, 100.0, domainIndependentAgent
			.circleRecognitionParameters().minCorrelation() * 100.0);
	circleMinCorrelationParamSlider
		.onParameterUpdate(new EventHandler<ParameterSliderEvent>() {
		    @Override
		    public void handle(final ParameterSliderEvent event) {
			domainIndependentAgent.circleRecognitionParameters()
				.minCorrelation(event.newValue());
		    }
		});
	box.getChildren().add(circleMinCorrelationParamSlider);

	final ParameterSlider circleMaxAreaErrorParamSlider = new ParameterSlider(
		"Circle max area error", 0.0, 100.0, domainIndependentAgent
			.circleRecognitionParameters().maxAreaError() * 100.0);
	circleMaxAreaErrorParamSlider
		.onParameterUpdate(new EventHandler<ParameterSliderEvent>() {
		    @Override
		    public void handle(final ParameterSliderEvent event) {
			domainIndependentAgent.circleRecognitionParameters()
				.maxAreaError(event.newValue());
		    }
		});
	box.getChildren().add(circleMaxAreaErrorParamSlider);

	final ParameterSlider circleMaxSlopeErrorParamSlider = new ParameterSlider(
		"Circle max slope error", 0.0, 100.0, domainIndependentAgent
			.circleRecognitionParameters().maxSlopeError() * 100.0);
	circleMaxSlopeErrorParamSlider
		.onParameterUpdate(new EventHandler<ParameterSliderEvent>() {
		    @Override
		    public void handle(final ParameterSliderEvent event) {
			domainIndependentAgent.circleRecognitionParameters()
				.maxSlopeError(event.newValue());
		    }
		});
	box.getChildren().add(circleMaxSlopeErrorParamSlider);

	return box;
    }

    private Stroke stroke(final List<Point2D> sketchPoints) {
	final Point[] result = new Point[sketchPoints.size()];
	int i = 0;
	for (final Point2D pt : sketchPoints) {
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

	return new Stroke(1.0, result);
    }

    public static void main(final String[] args) {
	Application.launch(args);
    }
}
