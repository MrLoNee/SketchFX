package com.ncl.sketch.hmi;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import com.ncl.sketch.agent.api.Circle;
import com.ncl.sketch.agent.api.Line;
import com.ncl.sketch.agent.api.Point;
import com.ncl.sketch.agent.api.RecognitionResult;
import com.ncl.sketch.agent.api.SketchRecognitionAgent;
import com.ncl.sketch.agent.api.Stroke;
import com.ncl.sketch.agent.di.DomainIndependentAgent;
import com.ncl.sketch.agent.di.api.DomainIndependentAgentParameters;

public class SketchRecognitionHMI extends Application {

    private SketchRecognitionAgent sketchRecognitionAgent;

    private DomainIndependentAgentParameters domainIndependantAgentParameters;

    private ScheduledExecutorService executor;

    @Override
    public void start(final Stage stage) throws Exception {
        final DomainIndependentAgent domainIndependentAgent = new DomainIndependentAgent();
        domainIndependantAgentParameters = domainIndependentAgent;
        sketchRecognitionAgent = domainIndependentAgent;
        executor = Executors.newSingleThreadScheduledExecutor();

        final BorderPane borderPane = new BorderPane();
        final Group container = new Group();

        // borderPane.setCenter(container);
        final Node parametersPanel = createParametersPanel();
        borderPane.setRight(parametersPanel);

        final Scene scene = new Scene(container);

        final SketchListener sketchListener = new SketchListener(scene, container);
        sketchListener.setOnSketchDone(new EventHandler<SketchEvent>() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void handle(final SketchEvent sketchEvent) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {

                        final RecognitionResult recognitionResult =
                                sketchRecognitionAgent.recognize(stroke(sketchEvent.getSketchPoints()));

                        final Collection<Line> lines = recognitionResult.lines();
                        final Collection<Circle> circles = recognitionResult.circles();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                sketchEvent.getSketchDrawing().setStroke(Color.LIGHTGRAY);

                                for (final Line line : lines) {
                                    final javafx.scene.shape.Line lineFx =
                                            new javafx.scene.shape.Line(line.start().x(), line.start().y(),
                                                                        line.end().x(), line.end().y());
                                    lineFx.setStroke(Color.GREEN);
                                    container.getChildren().add(lineFx);
                                }

                                for (final Circle circle : circles) {
                                    final javafx.scene.shape.Circle circleFx =
                                            new javafx.scene.shape.Circle(circle.center().x(),
                                                                          circle.center().y(), circle.radius());
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

    private Node createParametersPanel() {
        final FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setPrefWrapLength(170); // preferred width allows for two columns

        // k parameter
        final Slider kSlider = new Slider(1, 10, domainIndependantAgentParameters.k());
        kSlider.setBlockIncrement(1);
        kSlider.setMajorTickUnit(1);
        kSlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public Double fromString(final String arg0) {
                return 0d;
            }

            @Override
            public String toString(final Double n) {
                if (n < 2) {
                    return "k";
                }

                return "";
            }
        });
        kSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void changed(final ObservableValue<? extends Number> obsValue, final Number previousValue,
                    final Number newValue) {
                domainIndependantAgentParameters.k(newValue.intValue());
            }

        });
        flow.getChildren().add(kSlider);
        return flow;
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
