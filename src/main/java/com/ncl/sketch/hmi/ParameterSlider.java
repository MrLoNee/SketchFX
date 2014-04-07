package com.ncl.sketch.hmi;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class ParameterSlider extends VBox {

    private final List<EventHandler<ParameterSliderEvent>> listeners;

    private final Label label;

    public ParameterSlider(final String paramName, final double minValue, final double maxValue,
            final double initialValue) {
        this(paramName, minValue, maxValue, initialValue, true);
    }

    public ParameterSlider(final String paramName, final int minValue, final int maxValue, final int initialValue) {
        this(paramName, minValue, maxValue, initialValue, false);
    }

    private ParameterSlider(final String paramName, final double minValue, final double maxValue,
            final double initialValue, final boolean percentage) {
        setAlignment(Pos.CENTER);
        setPrefWidth(200);
        listeners = new ArrayList<>();
        label = new Label(paramName + " : " + initialValue + (percentage ? "%" : ""));
        getChildren().add(label);

        final Slider slider = new Slider();
        slider.setMin(minValue);
        slider.setMax(maxValue);
        slider.setValue(initialValue);
        slider.setShowTickLabels(true);
        getChildren().add(slider);

        slider.setShowTickLabels(true);
        slider.setBlockIncrement(1);

        if (!percentage) {
            slider.setBlockIncrement(1);
            slider.setMajorTickUnit(3);
            slider.setMinorTickCount(1);
            slider.valueProperty().addListener(new ChangeListener<Number>() {

                @SuppressWarnings("synthetic-access")
                @Override
                public void changed(final ObservableValue<? extends Number> arg0, final Number arg1,
                        final Number newValue) {
                    label.setText(paramName + " : " + newValue.intValue());
                    for (final EventHandler<ParameterSliderEvent> listener : listeners) {
                        listener.handle(new ParameterSliderEvent(newValue.intValue()));
                    }
                }

            });
        } else {
            slider.setBlockIncrement(10);
            slider.setMajorTickUnit(25);
            slider.setMinorTickCount(10);
            slider.valueProperty().addListener(new ChangeListener<Number>() {

                @SuppressWarnings("synthetic-access")
                @Override
                public void changed(final ObservableValue<? extends Number> arg0, final Number arg1,
                        final Number newValue) {
                    label.setText(paramName + " : " + newValue.intValue() + "%");
                    for (final EventHandler<ParameterSliderEvent> listener : listeners) {
                        listener.handle(new ParameterSliderEvent(newValue.intValue()));
                    }
                }

            });

        }
        slider.setBlockIncrement(1);
    }

    public void onParameterUpdate(final EventHandler<ParameterSliderEvent> handler) {
        listeners.add(handler);
    }

}
